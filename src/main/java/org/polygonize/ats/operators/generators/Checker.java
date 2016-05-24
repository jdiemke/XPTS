package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "checker", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
public class Checker extends AtsDataSourceOperator {

    AtsPosition2D quantity = new AtsPosition2D(4, 4);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("quantity", quantity);
    }

    /**
     * http://mathforum.org/mathimages/index.php/Procedural_Image
     * http://http.developer.nvidia.com/GPUGems/gpugems_ch25.html
     */
    public void process() {

        int quantityx = quantity.x.value;
        int quantityy = quantity.y.value;

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                float s = x / 256.0f;
                float t = y / 256.0f;

                int ss = (int) (s * 2 * quantityx);
                int tt = (int) (t * 2 * quantityy);

                int color = ((ss + tt) % 2) * 255;

                texture_.setPixel(x, y, color, color, color, 255);
            }
    }

}
