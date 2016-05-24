package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "glow", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsEnvironmentMapOperator extends AtsDataSourceOperator {

    AtsColor color = new AtsColor(255, 255, 255, 255);
    AtsPosition2D pos = new AtsPosition2D(127, 127);
    AtsPosition2D size = new AtsPosition2D(127, 127);
    AtsInteger gamma = new AtsInteger(0);
    // AtsBoolean wrap = new AtsBoolean(true);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color", color);
        propertyContainer.attachParam("position", pos);
        propertyContainer.attachParam("size", size);
        propertyContainer.attachParam("gamma", gamma);

    }

    public void process() {
        float radius = 127;
        double value, dx, dy;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                dx = (pos.x.value - x) * (127.f / size.x.value);
                dy = (pos.y.value - y) * (127.f / size.y.value);
                value = (Math.sqrt(dx * dx + dy * dy) / radius);
                // if (r>1) r=0;
                value = 1 - value;

                if (value < 0)
                    value = 0;

                value = Math.pow(value, (gamma.value) * 16 / 255f);

                value = (1 - AtsUtil.cosineInterpolate2(0, 1,
                        (float) (Math.sqrt(dx * dx + dy * dy)
                                - (radius - (1.5 * (127.f / Math.min(size.x.value, size.y.value)))))
                                / (1.5f * (127.f / Math.min(size.x.value, size.y.value)))))
                        * value;

                texture_.setPixel(x, y, (int) (value * color.red.value), (int) (value * color.green.value),
                        (int) (value * color.blue.value), 255);
            }
    }

}
