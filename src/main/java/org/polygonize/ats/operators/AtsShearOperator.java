package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "shear", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsShearOperator extends AtsProcessOperator {

    AtsInteger displacement_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("displacement", displacement_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("displacement", displacement_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        float displacement = displacement_.value / 255.f;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        float scale = 0.3f;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int filteredTexel = source.getBilinearFilteredPixel(x - y * displacement, y);

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
