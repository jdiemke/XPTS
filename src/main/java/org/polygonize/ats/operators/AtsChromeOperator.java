package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "chrome", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsChromeOperator extends AtsProcessOperator {

    AtsInteger scale_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("Red:", scale_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("scale", scale_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        float number = scale_.value / 255.f * 5;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int color1;
        int color2;
        int color3;
        float scale = 0.3f;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                color1 = (int) Math
                        .abs(Math.sin(2 * Math.PI * (AtsUtil.extractR(source.getPixel(x, y)) / 255.f) * number) * 255);
                color2 = (int) Math
                        .abs(Math.sin(2 * Math.PI * (AtsUtil.extractR(source.getPixel(x, y)) / 255.f) * number) * 255);
                color3 = (int) Math
                        .abs(Math.sin(2 * Math.PI * (AtsUtil.extractR(source.getPixel(x, y)) / 255.f) * number) * 255);

                texture_.setPixel(x, y, color1, color2, color3, 255);
            }
    }

}
