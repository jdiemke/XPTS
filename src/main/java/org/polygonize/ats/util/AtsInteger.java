package org.polygonize.ats.util;

public class AtsInteger implements Cloneable, Parameter {

    public int value;
    public int defaultValue;
    public int min;
    public int max;

    @Override
    public Object clone() throws CloneNotSupportedException {
        AtsInteger integer = (AtsInteger) super.clone();
        return integer;
    }

    public AtsInteger(int value) {
        this.value = value;
        this.defaultValue = value;
        this.min = 0;
        this.max = 255;
    }

    public AtsInteger(int value, int min, int max) {
        this.value = value;
        this.defaultValue = value;
        this.min = min;
        this.max = max;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void parse(String string) {
        if (string.length() > 0)
            value = Integer.parseInt(string);
    }

    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public void copy(Parameter param) {
        AtsInteger integer = (AtsInteger) param;
        this.value = integer.value;

    }

}
