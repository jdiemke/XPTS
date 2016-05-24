package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "polar", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsPolarDistortionOperator extends AtsProcessOperator {

    public void edit(AtsPropertyContainer container) {
    }

    public void edit(PropertyContainer propertyContainer) {
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        double theta, phi, phi2;
        float i2;
        int i, j;

        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int color1;
        int color2;
        int color3;
        float scale = 0.3f;

        for (j = 0; j < 256; j++) {
            theta = Math.PI * (j - (256 - 1) / 2.0) / (double) (256 - 1);
            for (i = 0; i < 256; i++) {
                phi = Math.PI / 2 * (i - 256 / 2.0) / (double) 256;
                phi2 = phi * Math.cos(theta);
                i2 = (float) (phi2 * 256 / Math.PI * 2 + 256 / 2);

                int filteredTexel = source.getBilinearFilteredPixel(i2, j);
                texture_.setPixel(i, j, filteredTexel);

            }
        }

    }

}
