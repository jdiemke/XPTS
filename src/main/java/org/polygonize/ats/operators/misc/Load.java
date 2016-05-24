package org.polygonize.ats.operators.misc;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.core.ui.ImageFactory.ImageType;

@AtsOperatorMetadata(operatorDesignation = "load", category = "misc", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
public class Load extends AtsDataSourceOperator {

    public Load() {
        operator = ImageFactory.getInstance().getImage(ImageType.LOAD_OPERATOR);
        operatorSelected = ImageFactory.getInstance().getImage(ImageType.LOAD_OPERATOR_SELECTED);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        texture_.copy(source);
    }
}
