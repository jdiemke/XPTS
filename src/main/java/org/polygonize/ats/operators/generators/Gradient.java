package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector2d;
import org.polygonize.ats.util.math.Vector3f;

@AtsOperatorMetadata(operatorDesignation = "gradient", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
public class Gradient extends AtsDataSourceOperator {

    AtsColor color0 = new AtsColor(0, 0, 0, 255);
    AtsColor color1 = new AtsColor(255, 255, 255, 255);
    AtsInteger angle = new AtsInteger(0, 0, 360);
    AtsInteger width = new AtsInteger(0);
    AtsPosition2D pos = new AtsPosition2D(127, 127);
    AtsEnumeration mode = new AtsEnumeration(new String[] { "linear", "sine" });

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color 0", color0);
        propertyContainer.attachParam("color 1", color1);
        propertyContainer.attachParam("angle", angle);
        propertyContainer.attachParam("width", width);
        propertyContainer.attachParam("pos", pos);
        propertyContainer.attachParam("mode", mode);
    }

    /**
     * http://mathforum.org/mathimages/index.php/Procedural_Image
     * http://http.developer.nvidia.com/GPUGems/gpugems_ch25.html
     */
    public void process() {

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                float s = x / 256.0f - 0.5f - (pos.x.value / (255.f) - 0.5f);
                float t = y / 256.0f - 0.5f - (pos.y.value / (255.f) - 0.5f);

                Vector2d normal = new Vector2d(Math.cos(angle.value / 360.0 * 2 * Math.PI),
                        Math.sin(angle.value / 360.0 * 2 * Math.PI));

                float dist = (float) normal.dot(new Vector2d(s, t));

                Vector3f color = colorize(
                        new Vector3f(color0.red.value / 255.0f, color0.green.value / 255.0f,
                                color0.blue.value / 255.0f),
                        new Vector3f(color1.red.value / 255.0f, color1.green.value / 255.0f,
                                color1.blue.value / 255.0f),
                        (dist / (width.value / 255.f)) + 0.5f);

                texture_.setPixel(x, y, (int) (255 * color.x), (int) (255 * color.y), (int) (255 * color.z), 255);
            }
    }

    Vector3f colorize(Vector3f color1, Vector3f color2, float alpha) {
        if (alpha > 1)
            alpha = 1;
        if (alpha < 0)
            alpha = 0;
        if (mode.selected == 1) {
            alpha = (float) Math.sin(alpha * Math.PI);
        }
        return color2.multiply(alpha).add(color1.multiply(1 - alpha));

    }

}