package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "multiply", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Multiply extends AtsProcessOperator {

    AtsInteger scale_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("scale", scale_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("scale", scale_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() > 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();
        int scale = scale_.value * 2;

        float fractionR, fractionG, fractionB;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float red = 1.0f;
                float green = 1.0f;
                float blue = 1.0f;
                for (int i = 0; i < inputs_.size(); i++) {
                    red *= (float) (((AtsUtil.extractR(inputs_.get(i).getTexture().getPixel(x, y)) / 255.f)));
                    green *= (float) (((AtsUtil.extractG(inputs_.get(i).getTexture().getPixel(x, y)) / 255.f)));
                    blue *= (float) (((AtsUtil.extractB(inputs_.get(i).getTexture().getPixel(x, y)) / 255.f)));
                }

                int redi = AtsUtil.clamp((int) (red * 255));
                int greeni = AtsUtil.clamp((int) (green * 255));
                int bluei = AtsUtil.clamp((int) (blue * 255));

                texture_.setPixel(x, y, redi, greeni, bluei, 255);

            }

    }

}
