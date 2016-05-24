package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSinkOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.core.ui.ImageFactory.ImageType;
import org.polygonize.ats.util.AtsInteger;

@AtsOperatorMetadata(operatorDesignation = "texture", category = "misc", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsTextureOperator extends AtsDataSinkOperator {

    private AtsInteger red_ = new AtsInteger(255);
    private AtsInteger green_ = new AtsInteger(255);
    private AtsInteger blue_ = new AtsInteger(255);
    public AtsProceduralTexture normalMap = new AtsProceduralTexture();

    public boolean isInputAccepted() {
        return inputs_.size() == 2;
    }

    public AtsTextureOperator() {

        operator = ImageFactory.getInstance().getImage(ImageType.SINK_TEXTURE);
        operatorSelected = ImageFactory.getInstance().getImage(ImageType.SINK_TEXTURE_SELECTED);
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        AtsProceduralTexture normal = inputs_.get(1).getTexture();

        texture_.copy(source);
        normalMap.copy(normal);
    }

}