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

@AtsOperatorMetadata(operatorDesignation = "normal", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsNormalMapOperator extends AtsProcessOperator {

    AtsInteger scale_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("Red:", scale_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("scale", scale_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // TODO: read chapter about convolution and point operations
    // slights from nvidea about "per pixel lighting intro"
    // http://stackoverflow.com/questions/9567882/sobel-filter-kernel-of-large-size
    // TODO: think about a scale that doesnt destroy the normal map
    //
    // !!!!!!!!!
    // FIXME: DONT JUST USE RED FOR NORMALMAP!!!
    public void process() {
        double scale = scale_.value / 255.0 * 16;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int[] H_x = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };

        int[] H_x2 = { -1, -2, 0, 2, 1, -4, -8, 0, 8, 4, -6, -12, 0, 12, 6, -4, -8, 0, 8, 4, -1, -2, 0, 2, 1 };

        int[] H_y = { -1, -2, -1, 0, 0, 0, 1, 2, 1 };

        int[] H_y2 = { -1, -4, -6, -4, -1, -2, -8, -12, -8, -2, 0, 0, 0, 0, 0, 2, 8, 12, 8, 2, 1, 4, 6, 4, 1 };

        Vector3D vectorX = new Vector3D();
        Vector3D vectorY = new Vector3D();
        Vector3D normal = new Vector3D();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                double slopex = convolution(H_x2, 0.0, 48.0 * 2, x, y, source);
                vectorX.set(1.0, 0.0, slopex * scale);
                double slopey = convolution(H_y2, 0.0, 48 * 2, x, y, source);
                vectorY.set(0.0, 1.0, slopey * scale);
                normal.cross(vectorX, vectorY);
                normal = normal.getNormalized();
                // normal.set(slopex*scale, slopey*scale, 1);
                //
                // slopex*=scale;
                // slopey*=scale;
                // double z = Math.sqrt(1 - slopex*slopex + slopey*slopey);
                // normal.set(slopex, slopey, z);

                float dz = (float) Math.sqrt(1d - (slopex * slopex + slopey * slopey) * 0.5);

                texture_.setPixel(x, y, AtsUtil.clamp((int) ((normal.x + 1.0) * 0.5 * 255)),
                        AtsUtil.clamp((int) ((normal.y + 1.0) * 0.5 * 255)),
                        AtsUtil.clamp((int) ((normal.z + 1.0) * 0.5 * 255)), 255);

                texture_.setPixel(x, y, AtsUtil.clamp((int) ((normal.x + 1.0) * 0.5 * 255)),
                        AtsUtil.clamp((int) ((-normal.y + 1.0) * 0.5 * 255)),
                        AtsUtil.clamp((int) ((normal.z + 1.0) * 0.5 * 255)), 255);
            }

    }

    public double convolution(int[] filter, double bias, double divide, int x, int y, AtsProceduralTexture texture) {

        int gridCounter = 0;
        double finalr = 0, finalg = 0, finalb = 0;

        for (int y2 = -2; y2 <= 2; y2++)
            for (int x2 = -2; x2 <= 2; x2++) {
                int offset = (((x + x2) & 0xff) + (((y + y2) & 0xff) << 8));

                finalr += texture.getPixelIntensity(offset) / 255.0 * filter[gridCounter] / divide;
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
