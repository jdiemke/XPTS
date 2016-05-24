package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "distort", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsDistortOperator extends AtsProcessOperator {

    AtsInteger scale_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("scale", scale_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("scale", scale_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    // TODO: maybe different scale for x and y
    public void process() {

        AtsProceduralTexture source = inputs_.get(0).getTexture();
        AtsProceduralTexture distortionMap = inputs_.get(1).getTexture();

        float scale = scale_.value / 255.f * 2;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                float u = x + (AtsUtil.extractR(distortionMap.getPixel(x, y)) - 127) * scale;
                float v = y + (AtsUtil.extractG(distortionMap.getPixel(x, y)) - 127) * scale;

                int filteredTexel = source.getBilinearFilteredPixel(u, v);
                // source.getPixel(AtsUtil.repeat((int)u),
                // AtsUtil.repeat((int)v));

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
