package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "add", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsAddOperator extends AtsProcessOperator {

    public void edit(PropertyContainer propertyContainer) {

    }

    @Override
    public boolean isInputAccepted() {
        return inputs_.size() > 0;
    }

    public void process() {

        int red = 0, green = 0, blue = 0;

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {
                red = green = blue = 0;
                for (int i = 0; i < inputs_.size(); i++) {
                    // texel = inputs_.get(i).getTexture()[x + (y << 8)];
                    red += AtsUtil.extractR(inputs_.get(i).getTexture().getPixel(x, y));
                    green += AtsUtil.extractG(inputs_.get(i).getTexture().getPixel(x, y));
                    blue += AtsUtil.extractB(inputs_.get(i).getTexture().getPixel(x, y));

                }
                red /= inputs_.size();
                green /= inputs_.size();
                blue /= inputs_.size();
                texture_.texels_[x + (y << 8)] = (red << 16) | (green << 8) | (blue);
                texture_.setPixel(x, y, red, green, blue, 255);
            }
        }
    }
}
