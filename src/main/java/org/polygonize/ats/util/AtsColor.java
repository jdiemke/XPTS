package org.polygonize.ats.util;

public class AtsColor implements Cloneable, Parameter {

    public AtsInteger red;
    public AtsInteger green;
    public AtsInteger blue;
    public AtsInteger alpha;

    @Override
    public Object clone() throws CloneNotSupportedException {
        AtsColor color = (AtsColor) super.clone();
        color.red = (AtsInteger) red.clone();
        color.green = (AtsInteger) green.clone();
        color.blue = (AtsInteger) blue.clone();
        color.alpha = (AtsInteger) alpha.clone();
        return color;
    }

    public AtsColor(int red, int green, int blue, int alpha) {
        this.red = new AtsInteger(red);
        this.green = new AtsInteger(green);
        this.blue = new AtsInteger(blue);
        this.alpha = new AtsInteger(alpha);
    }

    public AtsInteger getRed() {
        return red;
    }

    public AtsInteger getGreen() {
        return green;
    }

    public AtsInteger getBlue() {
        return blue;
    }

    public AtsInteger getAlpha() {
        return alpha;
    }

    @Override
    public void parse(String string) {

        int result = (int) (Long.parseLong(string.substring(1), 16) & 0xffffffff);
        red.setValue((result >> (3 * 8)) & 0xff);
        green.setValue((result >> (2 * 8)) & 0xff);
        blue.setValue((result >> (1 * 8)) & 0xff);
        alpha.setValue((result) & 0xff);

    }

    public String toString() {
        int i = (getRed().value << (3 * 8)) | (getGreen().value << (2 * 8)) | (getBlue().value << (8))
                | (getAlpha().value);
        return String.format("#%08X", (0xFFFFFFFF & i));
    }

    @Override
    public void copy(Parameter param) {
        AtsColor color = (AtsColor) param;
        this.red.copy(color.red);
        this.green.copy(color.green);
        this.blue.copy(color.blue);
        this.alpha.copy(color.alpha);
    }

}