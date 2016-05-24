package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "gamma", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsGammaCorrectionOperator extends AtsProcessOperator {

    AtsInteger gamma_ = new AtsInteger(128);
    AtsInteger amp = new AtsInteger(128);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("gamma", gamma_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("gamma", gamma_);
        propertyContainer.attachParam("amplify", amp);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        int scale = gamma_.value * 2;
        float gamma = gamma_.value * 8 / 255f;
        float amply = amp.value / 255f * 32;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int red = AtsUtil
                        .clamp((int) (amply * 255 * Math.pow((AtsUtil.extractR(source.getPixel(x, y)) / 255f), gamma)));
                int green = AtsUtil
                        .clamp((int) (amply * 255 * Math.pow((AtsUtil.extractG(source.getPixel(x, y)) / 255f), gamma)));
                int blue = AtsUtil
                        .clamp((int) (amply * 255 * Math.pow((AtsUtil.extractB(source.getPixel(x, y)) / 255f), gamma)));

                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

}
