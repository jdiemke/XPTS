package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.Vector3D;

@AtsOperatorMetadata(operatorDesignation = "dirblur", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsDirectionalBlurOperator extends AtsProcessOperator {

    AtsInteger direction_ = new AtsInteger(255);
    AtsInteger scale_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("direction", direction_);
        container.attachParam("scale", scale_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("direction", direction_);
        propertyContainer.attachParam("scale", scale_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // TODO: FIX
    public void process() {
        int direction = direction_.value;
        // TODO: somethign useful

        int scale = scale_.value;
        if (scale < 1)
            scale = 1;

        Vector3D directionVector = new Vector3D(Math.sin(2 * Math.PI / 255 * direction) * 0.5,
                Math.cos(2 * Math.PI / 255 * direction) * 0.5, 0);

        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int red = 0, green = 0, blue = 0;
                for (int i = 0; i < scale; i++) {
                    Vector3D offsetVector = directionVector.mult(i);
                    int filteredTexel = source.getBilinearFilteredPixel((float) (offsetVector.x + x),
                            (float) (offsetVector.y + y));
                    red += AtsUtil.extractR(filteredTexel);
                    green += AtsUtil.extractG(filteredTexel);
                    blue += AtsUtil.extractB(filteredTexel);
                }
                texture_.setPixel(x, y, red / scale, green / scale, blue / scale, 255);
            }
    }

}
