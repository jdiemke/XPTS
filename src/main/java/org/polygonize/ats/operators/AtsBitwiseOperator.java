package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "xor", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsBitwiseOperator extends AtsDataSourceOperator {

    private AtsInteger red_ = new AtsInteger(255);
    private AtsInteger green_ = new AtsInteger(255);
    private AtsInteger blue_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer propertyContainer) {
        propertyContainer.attachParam("Red:", red_);
        propertyContainer.attachParam("Green:", green_);
        propertyContainer.attachParam("Blue:", blue_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("red", red_);
        propertyContainer.attachParam("green", green_);
        propertyContainer.attachParam("blue", blue_);
    }

    public void process() {
        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float color = (x ^ y) / 255.0f; // change to & ^ | bitwise ops
                int red = (int) (red_.getValue() * color);
                int green = (int) (green_.getValue() * color);
                int blue = (int) (blue_.getValue() * color);
                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

}