package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "perlin marble", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class MarblePerlin extends AtsProcessOperator {

    AtsInteger period = new AtsInteger(3);
    AtsInteger distortion = new AtsInteger(3);
    AtsPosition2D scale = new AtsPosition2D(1, 1);

    public void edit(AtsPropertyContainer container) {
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("per", period);
        propertyContainer.attachParam("dist", distortion);
        propertyContainer.attachParam("scale", scale);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // http://electric.ity.org/6620/05/05.html
    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int color = (int) (AtsUtil.extractR(source.getBilinearFilteredPixel((x), (y))) * 0.3
                        + AtsUtil.extractG(source.getBilinearFilteredPixel((x), (y))) * 0.59
                        + AtsUtil.extractB(source.getBilinearFilteredPixel((x), (y))) * 0.11);
                int periods = period.value;
                float dist = distortion.value / 255.f * 0.1f;
                float inten = (float) (scale.y.value / 255f * 2
                        * Math.pow(Math.abs(Math.sin(x / 256.f * Math.PI * periods + periods * dist * color)),
                                scale.x.value / 255f * 2));

                int color2 = AtsUtil.clamp((int) (inten * 255));
                texture_.setPixel(x, y, color2, color2, color2, 255);
            }
    }

}
