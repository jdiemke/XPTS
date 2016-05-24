/* 3x3 sobel filter
 * read more somewher on my harddrive ;)
 * 
 */
package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.Vector3D;

@AtsOperatorMetadata(operatorDesignation = "refraction", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsRefractionOperator extends AtsProcessOperator {

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

    // TODO: read chapter about convolution and point operations
    // slights from nvidea about "per pixel lighting intro"
    // TODO: think about a scale that doesnt destroy the normal map
    //
    // !!!!!!!!!
    // FIXME: DONT JUST USE RED FOR NORMALMAP!!!
    public void process() {
        float scale = scale_.value / 255.0f * 255;
        AtsProceduralTexture source1 = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();

        int[] H_x = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };

        int[] H_y = { -1, -2, -1, 0, 0, 0, 1, 2, 1 };

        Vector3D vectorX = new Vector3D();
        Vector3D vectorY = new Vector3D();
        Vector3D normal = new Vector3D();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float slopex = (float) convolution(H_x, 0.0, 8.0, x, y, source2);
                float slopey = (float) convolution(H_y, 0.0, 8.0, x, y, source2);

                int filteredTexel = source1.getBilinearFilteredPixel(x + slopex * scale, y + slopey * scale);

                texture_.setPixel(x, y, filteredTexel);
            }

    }

    public double convolution(int[] filter, double bias, double divide, int x, int y, AtsProceduralTexture texture) {

        int gridCounter = 0;
        double finalr = 0, finalg = 0, finalb = 0;

        for (int y2 = -1; y2 <= 1; y2++)
            for (int x2 = -1; x2 <= 1; x2++) {
                int offset = (((x + x2) & 0xff) + (((y + y2) & 0xff) << 8));

                finalr += AtsUtil.extractR(texture.texels_[offset]) / 255.0 * filter[gridCounter] / divide;
                finalg += AtsUtil.extractG(texture.texels_[offset]) * filter[gridCounter] / divide;
                finalb += AtsUtil.extractB(texture.texels_[offset]) * filter[gridCounter] / divide;

                gridCounter++;
            }

        finalr += bias;
        finalg += bias;
        finalb += bias;

        return finalr;

    }

}
