package org.polygonize.ats.core.operator;

import java.awt.Graphics2D;

import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.core.ui.ImageFactory.ImageType;

public class AtsDataSourceOperator extends AtsOperator {

    public boolean isInputAccepted() {
        return true;
    }

    @Override
    void drawInputNotAccepted(Graphics2D graphics2D) {
        graphics2D.drawLine(positionX_ + 0 + 1 + 9, positionY_ + 0 - 1, positionX_ + width_ - 1 - 1 - 9,
                positionY_ + 0 - 1);
    }

    public AtsDataSourceOperator(String name) {
        super(name, 0, 0);

        operator = ImageFactory.getInstance().getImage(ImageType.SOURCE_OPERATOR);
        operatorSelected = ImageFactory.getInstance().getImage(ImageType.SOURCE_OPERATOR_SELECTED);
        operatorActive = ImageFactory.getInstance().getImage(ImageType.SOURCE_OPERATOR_ACTIVE);
        operatorSelectedActive = ImageFactory.getInstance().getImage(ImageType.SOURCE_OPERATOR_SELECTED_AND_ACTIVE);
    }

    public AtsDataSourceOperator() {
        this("");
    }

}
