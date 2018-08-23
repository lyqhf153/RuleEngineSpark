package com.engine.rule.domain;

import com.engine.rule.constant.Constant;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by manoj on 23/08/18.
 */
public class SignalData implements Serializable {
    private String signal;

    private String value_type;

    private String value;

    public String getSignal() {
        return signal;
    }

    public DataType getValueType() {
        return DataType.valueOf(getValue_type().toUpperCase());
    }

    public String getValue_type() {
        return value_type;
    }

    public Object getValue() {
        switch (getValueType()) {
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
                    throw new IllegalArgumentException("Date is incorrect : Date format should be " + Constant.dateFormat , e);
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

        SignalData that = (SignalData) o;

        if (!signal.equals(that.signal)) return false;
        if (!value_type.equals(that.value_type)) return false;
        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = signal.hashCode();
        result = 31 * result + value_type.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SignalData{" +
                "signal='" + signal + '\'' +
                ", value_type='" + value_type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
