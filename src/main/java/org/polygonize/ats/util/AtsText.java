package org.polygonize.ats.util;

public class AtsText implements Parameter {

    public String value_ = "test";

    public AtsText(String value) {
        value_ = value;
    }

    public void setValue(String value) {
        value_ = value;
    }

    String getValue() {
        return value_;
    }

    public void parse(String string) {
        if (string.length() > 0)
            value_ = string;
    }

    public String toString() {
        return value_;
    }

    @Override
    public void copy(Parameter param) {
        AtsText text = (AtsText) param;
        this.value_ = new String(text.value_);
    }
}
