package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector3f;

@AtsOperatorMetadata(operatorDesignation = "pixels", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Pixels extends AtsDataSourceOperator {

    private AtsInteger seed = new AtsInteger(0);
    private AtsInteger count = new AtsInteger(50);
    AtsColor color1 = new AtsColor(0, 0, 0, 255);
    AtsColor color2 = new AtsColor(255, 255, 255, 255);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color 1", color1);
        propertyContainer.attachParam("color 2", color2);
        propertyContainer.attachParam("random seed", seed);
        propertyContainer.attachParam("quantity", count);
    }

    public void process() {
        int count = this.count.value * 100;
        AtsUtil.srand(seed.value);
        texture_.clear(0, 0, 0, 255);
        for (int i = 0; i < count; i++) {
            int xpos = AtsUtil.rand() % 256;
            int ypos = AtsUtil.rand() % 256;

            Vector3f color = colorize(
                    new Vector3f(color1.red.value / 255.0f, color1.green.value / 255.0f, color1.blue.value / 255.0f),
                    new Vector3f(color2.red.value / 255.0f, color2.green.value / 255.0f, color2.blue.value / 255.0f),
                    (AtsUtil.rand() % 256) / 256.f);

            texture_.setPixel(xpos, ypos, (int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255), 255);
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