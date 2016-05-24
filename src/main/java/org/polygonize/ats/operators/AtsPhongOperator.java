package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.Vector3D;

@AtsOperatorMetadata(operatorDesignation = "phong", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsPhongOperator extends AtsProcessOperator {

    AtsInteger specPower_ = new AtsInteger(255);
    AtsInteger xPosition_ = new AtsInteger(0);
    AtsInteger yPosition_ = new AtsInteger(0);
    AtsColor ambient = new AtsColor(255, 255, 255, 255);
    AtsColor diffuse = new AtsColor(10, 10, 10, 255);
    AtsColor specular = new AtsColor(255, 255, 255, 255);

    public void edit(AtsPropertyContainer container) {

        container.attachParam("ambient", ambient);
        container.attachParam("diffuse", diffuse);
        container.attachParam("specular", specular);
        container.attachParam("specPower", specPower_);

        container.attachParam("light pos x", xPosition_);
        container.attachParam("light pos y", yPosition_);
    }

    public void edit(PropertyContainer container) {
        container.attachParam("ambient", ambient);
        container.attachParam("diffuse", diffuse);
        container.attachParam("specular", specular);

        container.attachParam("specPower", specPower_);
        container.attachParam("xPosition", xPosition_);
        container.attachParam("yPosition", yPosition_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    public void process() {
        AtsProceduralTexture source1 = inputs_.get(0).getTexture();
        AtsProceduralTexture source2 = inputs_.get(1).getTexture();
        int n = specPower_.getValue();
        Vector3D ambientLight = new Vector3D(0.1, 0.1, 0.1);

        Vector3D finalPixel = new Vector3D();
        Vector3D ambientColor = new Vector3D(ambient.red.value / 255f, ambient.green.value / 255f,
                ambient.blue.value / 255f);
        Vector3D diffuseColor = new Vector3D(diffuse.red.value / 255f, diffuse.green.value / 255f,
                diffuse.blue.value / 255f);
        Vector3D specularColor = new Vector3D(specular.red.value / 255f, specular.green.value / 255f,
                specular.blue.value / 255f);
        Vector3D surfacePixel = new Vector3D();
        Vector3D surfaceNormal = new Vector3D();
        Vector3D lightVector2 = new Vector3D((xPosition_.value / 255f * 2 - 1) * 5,
                (yPosition_.value / 255f * 2 - 1) * 5, 1.0);
        Vector3D viewer = new Vector3D(0, 0, 1.0);
        Vector3D reflection = new Vector3D();
        Vector3D lightVector = new Vector3D(0, 0, 0);
        Vector3D iA = new Vector3D();
        Vector3D iD = new Vector3D();
        Vector3D iS = new Vector3D();
        lightVector.normalize(lightVector2);

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                // calculating the normal
                int normal = source2.getPixel(x, y);
                surfaceNormal.set((AtsUtil.extractR(normal) / 255f) * 2 - 1, (AtsUtil.extractG(normal) / 255f) * 2 - 1,
                        (AtsUtil.extractB(normal) / 255f) * 2 - 1);
                surfaceNormal.normalize(surfaceNormal);

                // getting the pixel
                int pixel = source1.getPixel(x, y);
                surfacePixel.set(AtsUtil.extractR(pixel) / 255f, AtsUtil.extractG(pixel) / 255f,
                        AtsUtil.extractB(pixel) / 255f);

                reflection = surfaceNormal.mult((2 * surfaceNormal.dot(lightVector))).sub(lightVector);

                float diff = (float) Math.max(surfaceNormal.dot(lightVector), 0);
                float spec = (float) Math.pow(Math.max(reflection.dot(viewer), 0), n);

                iA.mult(surfacePixel, ambientColor);
                iD.mult(surfacePixel, diffuseColor.mult(diff));

                finalPixel = iA.add(iD).add(specularColor.mult(spec));

                texture_.setPixel(x, y, AtsUtil.clamp(((int) (finalPixel.x * 255.f))),
                        AtsUtil.clamp(((int) (finalPixel.y * 255.f))), AtsUtil.clamp(((int) (finalPixel.z * 255.f))),
                        255);
            }
    }

}
