package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector3f;

@AtsOperatorMetadata(operatorDesignation = "twirl", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Twirl extends AtsProcessOperator {

    AtsPosition2D pos = new AtsPosition2D(127, 127);
    AtsInteger radius = new AtsInteger(255, 1, 255);
    AtsInteger degree = new AtsInteger(255, 0, 255);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("center", pos);
        propertyContainer.attachParam("radius", radius);
        propertyContainer.attachParam("degree", degree);

    }

    public void process() {
        AtsProceduralTexture input = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {

                int xc = pos.x.value;
                int yc = pos.y.value;

                double rmax = radius.value / 255.f * 127;
                double alpha = Math.toRadians(degree.value / 255f * 360);

                double dx = x - xc;
                double dy = y - yc;

                double r = Math.sqrt(dx * dx + dy * dy);

                double beta = Math.atan2(dy, dx) + alpha * ((rmax - r) / rmax);

                double u = r <= rmax ? xc + r * Math.cos(beta) : x;
                double v = r <= rmax ? yc + r * Math.sin(beta) : y;

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