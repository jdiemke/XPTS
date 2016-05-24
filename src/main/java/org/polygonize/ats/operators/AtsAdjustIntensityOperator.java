package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "intensity", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsAdjustIntensityOperator extends AtsProcessOperator {

    AtsInteger red_ = new AtsInteger(255);
    AtsInteger green_ = new AtsInteger(255);
    AtsInteger blue_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("Red:", red_);
        container.attachParam("Green:", green_);
        container.attachParam("Blue:", blue_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("red", red_);
        propertyContainer.attachParam("green", green_);
        propertyContainer.attachParam("blue", blue_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int red2 = red_.value;
        int green2 = green_.value;
        int blue2 = blue_.value;
        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int red = AtsUtil.clamp(AtsUtil.extractR(source.getPixel(x, y)) + red2);
                int green = AtsUtil.clamp(AtsUtil.extractG(source.getPixel(x, y)) + green2);
                int blue = AtsUtil.clamp(AtsUtil.extractB(source.getPixel(x, y)) + blue2);

                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

}
