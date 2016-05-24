package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "blend", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsAlphaBlendOperator extends AtsProcessOperator {

    AtsInteger alpha_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("alpha", alpha_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("alpha", alpha_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    public void process() {
        AtsProceduralTexture source1 = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();

        float alpha = alpha_.value / 255f;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int red = (int) (AtsUtil.extractR(source1.getPixel(x, y)) * (1 - alpha)
                        + AtsUtil.extractR(source2.getPixel(x, y)) * alpha);
                int green = (int) (AtsUtil.extractG(source1.getPixel(x, y)) * (1 - alpha)
                        + AtsUtil.extractG(source2.getPixel(x, y)) * alpha);
                int blue = (int) (AtsUtil.extractB(source1.getPixel(x, y)) * (1 - alpha)
                        + AtsUtil.extractB(source2.getPixel(x, y)) * alpha);
                texture_.setPixel(x, y, red, green, blue, 255);

            }
    }

}
