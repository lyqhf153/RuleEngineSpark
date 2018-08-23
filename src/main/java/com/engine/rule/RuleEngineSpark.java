package com.engine.rule;

import com.engine.rule.domain.Rule;
import com.engine.rule.domain.SignalData;
import com.engine.rule.mapper.SignalDataMapper;
import com.engine.rule.predicate.FilterPredicate;
import com.engine.rule.util.Util;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkFiles;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.io.IOException;
import java.util.*;

/**
 * Created by manoj on 23/08/18.
 */
public class RuleEngineSpark {
    public static void main(String[] args) throws InterruptedException, IOException {
        SparkConf conf = new SparkConf().setAppName("RuleEngineSpark");
        conf.setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sc, new Duration(30000));
        Collection<String> topics = Arrays.asList("signal-topic");

        String ruleFile = SparkFiles.get("rule.json");
        Map<String, List<Rule>> signalRuleMap = Util.getSignalRuleMap(ruleFile);
        Broadcast<Map<String, List<Rule>>> signalRuleMapBroadCast = sc.broadcast(signalRuleMap);

        Map<String, Object> kafkaProps = new HashMap<>();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("group.id", "rule-engine");
        kafkaProps.put("key.deserializer", IntegerDeserializer.class);
        kafkaProps.put("value.deserializer", StringDeserializer.class);

        JavaInputDStream<ConsumerRecord<Integer, String>> javaInputDStream =
                KafkaUtils.createDirectStream(
                        javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<Integer, String>Subscribe(topics, kafkaProps));


        Broadcast<FilterPredicate> filterPredicateBroadcast = sc.broadcast(new FilterPredicate(signalRuleMap));

        javaInputDStream.foreachRDD(rdd -> {
                    JavaRDD<SignalData> signalDataJavaRDD = rdd.map(new SignalDataMapper());
                    FilterPredicate filterPredicate = filterPredicateBroadcast.getValue();
                    JavaRDD<SignalData> invalid = signalDataJavaRDD.filter(filterPredicate);

                    System.out.println("Sigals which are invalid as per rule are:- ");
                    invalid.map(signalData -> signalData.getSignal()).distinct().foreach(s -> {
                        System.out.println(s);
                    });
                }
        );

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

}