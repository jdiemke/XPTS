package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "rotozoom", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsRotoZoomOperator extends AtsProcessOperator {

    AtsInteger rotate = new AtsInteger(0, 0, 360);
    AtsInteger zoom = new AtsInteger(255, 1, 255);
    AtsPosition2D center = new AtsPosition2D(127, 127);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("angle", rotate);
        propertyContainer.attachParam("zoom", zoom);
        propertyContainer.attachParam("center", center);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        AtsProceduralTexture source = inputs_.get(0).getTexture();
        int r1 = rotate.value;
        int g1 = zoom.value;

        double W = 2 * Math.PI / 360 * r1;
        float scale = 64f / g1;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                float v = (float) (((x - 127) * scale * Math.cos(W) - (y - 127) * scale * Math.sin(W)) + 127 - 127
                        + center.x.value);
                float u = (float) (((x - 127) * scale * Math.sin(W) + (y - 127) * scale * Math.cos(W)) + 127 - 127
                        + center.y.value);

                int filteredTexel = source.getBilinearFilteredPixel(v, u);

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
