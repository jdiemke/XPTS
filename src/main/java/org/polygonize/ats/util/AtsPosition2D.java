package org.polygonize.ats.util;

import java.util.StringTokenizer;

public class AtsPosition2D implements Cloneable, Parameter {

    public AtsInteger x;
    public AtsInteger y;

    @Override
    public Object clone() throws CloneNotSupportedException {
        AtsPosition2D position = (AtsPosition2D) super.clone();
        position.x = (AtsInteger) x.clone();
        position.y = (AtsInteger) y.clone();
        return position;
    }

    public AtsPosition2D(int x, int y) {
        this.x = new AtsInteger(x);
        this.y = new AtsInteger(y);

    }

    public AtsPosition2D(AtsInteger atsInteger, AtsInteger atsInteger2) {
        this.x = atsInteger;
        this.y = atsInteger2;
    }

    public AtsInteger getX() {
        return x;
    }

    public AtsInteger getY() {
        return y;
    }

    @Override
    public void parse(String string) {

        StringTokenizer tokenizer = new StringTokenizer(string, ",", false);

        String str1 = tokenizer.nextToken();
        String str2 = tokenizer.nextToken();

        this.x.value = Integer.parseInt(str1);
        this.y.value = Integer.parseInt(str2);

    }

    public String toString() {
        return this.x + "," + this.y;
    }

    @Override
    public void copy(Parameter param) {
        AtsPosition2D pos = (AtsPosition2D) param;
        this.x.copy(pos.x);
        this.y.copy(pos.y);
    }

}
