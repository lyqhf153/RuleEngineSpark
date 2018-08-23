package com.engine.rule.mapper;

import com.engine.rule.domain.SignalData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.function.Function;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class SignalDataMapper implements Function<ConsumerRecord<Integer, String>, SignalData> {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public SignalData call(ConsumerRecord<Integer, String> record) throws Exception {
         mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(record.value(), SignalData.class);
    }
}