package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector3f;

@AtsOperatorMetadata(operatorDesignation = "ripple", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Ripple extends AtsProcessOperator {

    AtsPosition2D amplitude = new AtsPosition2D(127, 127);
    AtsPosition2D periods = new AtsPosition2D(1, 1);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("amplitude", amplitude);
        propertyContainer.attachParam("periods", periods);

    }

    public void process() {
        AtsProceduralTexture input = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {

                double ax = amplitude.x.value;
                double ay = amplitude.y.value;

                double u = x + ax * Math.sin(2 * Math.PI * y / 256.f * periods.x.value);
                double v = y + ay * Math.sin(2 * Math.PI * x / 256.f * periods.y.value);

                int pixel = input.getBilinearFilteredPixel((float) u, (float) v);
                texture_.setPixel(x, y, pixel);

            }
        }

    }

    Vector3f colorize(Vector3f color1, Vector3f color2, float alpha) {
        if (alpha > 1)
            alpha = 1;
        if (alpha < 0)
            alpha = 0;

        return color2.multiply(alpha).add(color1.multiply(1 - alpha));

    }

}