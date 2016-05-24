package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "grayscale", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Grayscale extends AtsProcessOperator {

    public void edit(AtsPropertyContainer container) {
    }

    public void edit(PropertyContainer propertyContainer) {
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int color = (int) (AtsUtil.extractR(source.getPixel(x, y)) * 0.3
                        + AtsUtil.extractG(source.getPixel(x, y)) * 0.59
                        + AtsUtil.extractB(source.getPixel(x, y)) * 0.11);

                texture_.setPixel(x, y, color, color, color, 255);
            }
    }

}
