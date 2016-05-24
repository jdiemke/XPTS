package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector3f;

@AtsOperatorMetadata(operatorDesignation = "rotate mul", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class RotateMul extends AtsProcessOperator {

    AtsInteger angle = new AtsInteger(0);
    AtsPosition2D pos = new AtsPosition2D(127, 127);
    AtsPosition2D scale = new AtsPosition2D(new AtsInteger(1, 1, 255), new AtsInteger(1, 1, 255));
    AtsColor adjust = new AtsColor(255, 255, 255, 255);
    AtsInteger count = new AtsInteger(0);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("angle", angle);
        propertyContainer.attachParam("position", pos);
        propertyContainer.attachParam("scale", scale);
        propertyContainer.attachParam("pre adjust", adjust);
        propertyContainer.attachParam("count", count);

    }

    public void process() {
        AtsProceduralTexture input = inputs_.get(0).getTexture();

        int count = this.count.value;

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {

                double r = 0;
                double g = 0;
                double b = 0;

                for (int i = 0; i <= count; i++) {

                    double angle = Math.toRadians(this.angle.value / 255.f * 90) * i;

                    double u = (x - 127) / (scale.x.value / 255f * 127) + 127;
                    double v = (y - 127) / (scale.y.value / 255f * 127) + 127;

                    u = (u - 127) * Math.cos(angle) - (v - 127) * Math.sin(angle) + 127;
                    v = (u - 127) * Math.sin(angle) + (v - 127) * Math.cos(angle) + 127;

                    u = u - i * ((pos.x.value / 255f - 0.5f) * 2 * 256);
                    v = v + i * ((pos.y.value / 255f - 0.5f) * 2 * 256);

                    int pixel = input.getBilinearFilteredPixel((float) u, (float) v);

                    int ri = AtsUtil.extractR(pixel);
                    int gi = AtsUtil.extractG(pixel);
                    int bi = AtsUtil.extractB(pixel);

                    r += ri * adjust.red.value / 255f;
                    g += gi * adjust.green.value / 255f;
                    b += bi * adjust.blue.value / 255f;
                }

                texture_.setPixel(x, y, AtsUtil.clamp((int) r), AtsUtil.clamp((int) g), AtsUtil.clamp((int) b), 255);

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