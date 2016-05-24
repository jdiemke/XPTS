package org.polygonize.ats.util;

public interface Parameter {

    public void parse(String string);

    public String toString();

    public void copy(Parameter param);

}
