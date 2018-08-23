package com.engine.rule.domain;

import com.engine.rule.constant.Constant;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by manoj on 23/08/18.
 */
public class Rule implements Serializable{
    private String type;
    private String operator;
    private String value;

    public DataType getType() {
        return DataType.valueOf(type.toUpperCase());
    }

    public Operator getOperator() {
         return Operator.findBySymbol(operator);
    }

    public Object getValue() {
        switch (getType()) {
            case STRING: {
                return LevelType.valueOf(value.toUpperCase());
            }
            case INTEGER: {
                return Double.valueOf(value);
            }
            case DATETIME: {
                try {
                    return Constant.simpleDateFormat.parse(value);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Date is incorrect : Should need to be in " + Constant.dateFormat  + " format" , e);
                }
            }
            default: {
                return value;
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (type != null ? !type.equals(rule.type) : rule.type != null) return false;
        if (operator != null ? !operator.equals(rule.operator) : rule.operator != null) return false;
        return value != null ? value.equals(rule.value) : rule.value == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "type='" + type + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
