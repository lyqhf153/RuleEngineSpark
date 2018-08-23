package com.engine.rule.util;

import com.engine.rule.domain.Rule;
import com.engine.rule.domain.SignalData;
import com.engine.rule.domain.SignalRule;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;

/**
 * Created by manoj on 23/08/18.
 */
public class Util {

    public static Map<String, List<Rule>> getSignalRuleMap(String rulePath) throws IOException {
        Map<String, List<Rule>> signalRuleMap = new HashMap<>();
        List<SignalRule> signalRules = Util.readJsonStream(SignalRule.class, new FileInputStream(new File(rulePath)));
        signalRules.parallelStream().forEach(signalRule -> {
            signalRuleMap.put(signalRule.getSignal(), signalRule.getRules());
        });
        return signalRuleMap;
    }

    public static <T> List<T> readJsonStream(Class<T> tClass, InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Gson gson = new Gson();
        List<T> messages = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            T message = gson.fromJson(reader, tClass);
            messages.add(message);
        }
        reader.endArray();
        reader.close();
        return messages;
    }

    public static boolean checkString(SignalData signalData, Rule rule) {
        switch (rule.getOperator()) {
            case EQUAL: {
                return signalData.getValue() == rule.getValue() ? true : false;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean checkInteger(SignalData signalData, Rule rule) {
        double data = (double) signalData.getValue();
        double dataFromRule = (double) rule.getValue();
        switch (rule.getOperator()) {
            case EQUAL: {
                return data == dataFromRule ? true : false;
            }
            case GREATER: {
                return data > dataFromRule ? true : false;
            }
            case LESS: {
                return data < dataFromRule ? true : false;
            }
            default: {
                return false;
            }

        }
    }

    public static boolean checkDateTime(SignalData signalData, Rule rule) {
        Date data = (Date) signalData.getValue();
        Date dataFromRule = (Date) rule.getValue();
        switch (rule.getOperator()) {
            case EQUAL: {
                return data.equals(dataFromRule) ? true : false;
            }
            case GREATER: {
                return data.after(dataFromRule) ? true : false;
            }
            case LESS: {
                return data.before(dataFromRule) ? true : false;
            }
            default: {
                return false;
            }
        }
    }


}
