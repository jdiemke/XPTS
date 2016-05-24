package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "marble", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsMarbleDistortOperator extends AtsProcessOperator {

    AtsInteger scale_ = new AtsInteger(255);
    AtsInteger turbolence_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("scale", scale_);
        container.attachParam("turbolence", turbolence_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("scale", scale_);
        propertyContainer.attachParam("turbolence", turbolence_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();

        float scale = scale_.value * 0.2f;
        float turbolence = turbolence_.value / 255f;
        float[] cval = new float[256];
        float[] sval = new float[256];

        for (int i = 0; i < 256; i++) {
            sval[i] = (float) (-scale * Math.sin(2 * Math.PI / 256 * i * turbolence));
            cval[i] = (float) (scale * Math.cos(2 * Math.PI / 256 * i * turbolence));
        }

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float xx = (x + sval[source2.getPixelIntensity(x, y)]);
                float yy = (y + cval[source2.getPixelIntensity(x, y)]);

                int filteredTexel = source.getBilinearFilteredPixel(xx, yy);

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
