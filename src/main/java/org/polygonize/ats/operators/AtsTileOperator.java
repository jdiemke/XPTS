package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "tile", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsTileOperator extends AtsProcessOperator {

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
                int filteredTexel = source.getBilinearFilteredPixel(x * 2 - 0.5f, y * 2 - 0.5f);
                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
