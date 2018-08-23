package com.engine.rule.domain;

import java.util.Arrays;

/**
 * Created by manoj on 23/08/18.
 */
public enum Operator {
    EQUAL("="),
    GREATER(">"),
    LESS("<");

    private final String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public static Operator findBySymbol(String operator){
        return Arrays.stream(values()).filter(value -> value.getOperator().equals(operator)).findFirst().orElse(null);
    }
}
