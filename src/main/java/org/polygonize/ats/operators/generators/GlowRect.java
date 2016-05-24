package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "glowrect", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class GlowRect extends AtsDataSourceOperator {

    AtsPosition2D center = new AtsPosition2D(127, 127);
    AtsPosition2D size = new AtsPosition2D(0, 0);
    AtsPosition2D radius = new AtsPosition2D(0, 0);
    AtsColor color = new AtsColor(255, 255, 255, 255);
    AtsInteger softness = new AtsInteger(1);
    AtsInteger blend = new AtsInteger(1);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("center", center);
        propertyContainer.attachParam("radius", radius);
        propertyContainer.attachParam("size", size);
        propertyContainer.attachParam("color", color);
        propertyContainer.attachParam("blend", blend);
        propertyContainer.attachParam("softness", softness);

    }

    public void process() {
        float radius = 127;
        double value, dx, dy;

        texture_.clear(0, 0, 0, 255);

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                float closestPointX = x;
                float closestPointY = y;

                closestPointX = Math.max(closestPointX, center.x.value - size.x.value / 2.f);
                closestPointX = Math.min(closestPointX, center.x.value + size.x.value / 2.f);

                closestPointY = Math.max(closestPointY, center.y.value - size.y.value / 2.f);
                closestPointY = Math.min(closestPointY, center.y.value + size.y.value / 2.f);

                dx = (closestPointX - x) * (127.f / this.radius.x.value);
                dy = (closestPointY - y) * (127.f / this.radius.y.value);
                value = (Math.sqrt(dx * dx + dy * dy) / radius);
                // if (r>1) r=0;
                value = 1 - value;

                if (value < 0)
                    value = 0;
                // 20 == gamma
                // value =Math.pow(value, (softness.value)*16/255f);
                value = 1 - Math.pow(1 - value, (255 - softness.value) * 16 / 255f);

                value = (1
                        - AtsUtil.cosineInterpolate2(0, 1,
                                (float) (Math.sqrt(dx * dx + dy * dy) - (radius
                                        - (1.5 * (127.f / Math.min(this.radius.x.value, this.radius.y.value))))) / (1.5f
                                                * (127.f / Math.min(this.radius.x.value, this.radius.y.value)))))
                        * value;

                texture_.setPixel(x, y, (int) (value * color.red.value), (int) (value * color.green.value),
                        (int) (value * color.blue.value), 255);
            }
    }

}
