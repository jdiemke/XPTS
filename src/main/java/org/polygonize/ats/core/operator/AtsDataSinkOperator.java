package org.polygonize.ats.core.operator;

import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.core.ui.ImageFactory.ImageType;

public class AtsDataSinkOperator extends AtsOperator {

    public AtsDataSinkOperator() {
        // super(,xpos,ypos);
        operator = ImageFactory.getInstance().getImage(ImageType.SINK_OPERATOR);
        operatorSelected = ImageFactory.getInstance().getImage(ImageType.SINK_OPERATOR_SELECTED);
        operatorActive = ImageFactory.getInstance().getImage(ImageType.SINK_OPERATOR_ACTIVE);
        operatorSelectedActive = ImageFactory.getInstance().getImage(ImageType.SINK_OPERATOR_SELECTED_AND_ACTIVE);
    }

}
