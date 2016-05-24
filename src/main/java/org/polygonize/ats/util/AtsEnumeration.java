package org.polygonize.ats.util;

import java.util.Vector;

public class AtsEnumeration implements Parameter {

    public Vector<String> value_ = new Vector<String>();
    public int selected;

    public AtsEnumeration(String[] enumeration) {
        for (String i : enumeration) {
            value_.add(i);
        }
    }

    public void setValue(String[] value) {
        value_.clear();
        for (String i : value) {
            value_.add(i);
        }
    }

    public Vector<String> getValue() {
        return value_;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void parse(String string) {
        // if(string.length()>0)
        // value_ = Integer.parseInt(string);
        // for(int i=0; i < value_.l)

        if (isInteger(string))
            selected = Integer.parseInt(string);

    }

    public String toString() {
        return Integer.toString(selected);
    }

    @Override
    public void copy(Parameter param) {
        AtsEnumeration enu = (AtsEnumeration) param;
        this.selected = enu.selected;
    }
}