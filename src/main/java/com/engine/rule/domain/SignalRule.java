package com.engine.rule.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by manoj on 23/08/18.
 */
public class SignalRule implements Serializable {
    String signal;
    @SerializedName("rule")
    List<Rule> rules;

    public String getSignal() {
        return signal;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignalRule that = (SignalRule) o;

        if (signal != null ? !signal.equals(that.signal) : that.signal != null) return false;
        return rules != null ? rules.equals(that.rules) : that.rules == null;

    }

    @Override
    public int hashCode() {
        int result = signal != null ? signal.hashCode() : 0;
        result = 31 * result + (rules != null ? rules.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalRule{" +
                "signal='" + signal + '\'' +
                ", rules=" + rules +
                '}';
    }
}
