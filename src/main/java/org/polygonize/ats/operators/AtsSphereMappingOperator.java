package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.Vector3D;

@AtsOperatorMetadata(operatorDesignation = "spheremap", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsSphereMappingOperator extends AtsProcessOperator {

    AtsInteger x_ = new AtsInteger(0);
    AtsInteger y_ = new AtsInteger(0);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("yPosition", x_);
        container.attachParam("xPosition", y_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("xPosition", x_);
        propertyContainer.attachParam("yPosition", y_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    public void process() {
        AtsProceduralTexture source1 = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();

        Vector3D ambientLight = new Vector3D(0.1, 0.1, 0.1);

        Vector3D finalPixel = new Vector3D();
        Vector3D surfacePixel = new Vector3D();
        Vector3D surfaceNormal = new Vector3D();
        Vector3D lightVector = new Vector3D(x_.value / 255f * 2 - 1 * 10, y_.value / 255f * 2 - 1 * 10, 1.0);
        // lightVector.normalize(lightVector);

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                // calculating the normal
                int normal = source1.getPixel(x, y);
                surfaceNormal.set((AtsUtil.extractR(normal) / 255f) * 2 - 1, (AtsUtil.extractG(normal) / 255f) * 2 - 1,
                        (AtsUtil.extractB(normal) / 255f) * 2 - 1);
                surfaceNormal.normalize(surfaceNormal);

                float u = (float) ((surfaceNormal.x * 255f / 2) + (255f / 2));
                float v = (float) ((surfaceNormal.y * 255f / 2) + (255f / 2));

                int filteredTexel = source2.getBilinearFilteredPixel(u, v);

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
