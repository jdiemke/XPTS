package org.polygonize.ats.util;

public class AtsBoolean implements Parameter {

    public boolean value_;

    public AtsBoolean(boolean value) {
        value_ = value;
    }

    public void setValue(boolean value) {
        value_ = value;
    }

    public boolean getValue() {
        return value_;
    }

    public void parse(String string) {
        if (string.length() > 0)
            value_ = Boolean.parseBoolean(string);
    }

    public String toString() {
        return Boolean.toString(value_);
    }

    @Override
    public void copy(Parameter param) {
        AtsBoolean bool = (AtsBoolean) param;
        this.value_ = bool.value_;

    }

}
