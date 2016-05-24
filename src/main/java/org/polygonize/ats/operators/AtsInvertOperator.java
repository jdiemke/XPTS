package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsBoolean;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "invert", category = "legacy", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsInvertOperator extends AtsProcessOperator {

    AtsBoolean alpha = new AtsBoolean(false);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("invert alpha", alpha);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        boolean invertAlpha = alpha.value_;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int oldColor = source.getPixel(x, y);
                int color = (~oldColor & 0xffffff00) | (oldColor & ~0xffffff00);
                if (invertAlpha) {
                    color = ~oldColor;
                }
                texture_.setPixel(x, y, color);
            }
    }

}
