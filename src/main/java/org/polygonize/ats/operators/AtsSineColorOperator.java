package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "sinecolor", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsSineColorOperator extends AtsProcessOperator {

    AtsInteger red_ = new AtsInteger(255);
    AtsInteger green_ = new AtsInteger(255);
    AtsInteger blue_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("red repetition", red_);
        container.attachParam("green repition", green_);
        container.attachParam("blue repition", blue_);
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

        float faktor1 = red_.value / 255f * 4;
        float faktor2 = green_.value / 255f * 5;
        float faktor3 = blue_.value / 255f * 4;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int red = (int) (255
                        * ((1 + Math.sin(AtsUtil.extractR(source.getPixel(x, y)) / 255f * faktor1 * Math.PI)) / 2));
                int green = (int) (255
                        * ((1 + Math.sin(AtsUtil.extractG(source.getPixel(x, y)) / 255f * faktor2 * Math.PI)) / 2));
                int blue = (int) (255
                        * ((1 + Math.sin(AtsUtil.extractB(source.getPixel(x, y)) / 255f * faktor3 * Math.PI)) / 2));

                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

}
