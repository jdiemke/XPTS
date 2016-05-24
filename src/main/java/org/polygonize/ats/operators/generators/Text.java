package org.polygonize.ats.operators.generators;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsBoolean;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsText;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "text", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Text extends AtsProcessOperator {

    AtsPosition2D pos = new AtsPosition2D(0, 0);

    AtsInteger height = new AtsInteger(20);
    AtsText text = new AtsText("text");
    AtsColor color = new AtsColor(255, 255, 255, 255);
    AtsText font = new AtsText("Arial");
    AtsBoolean bold = new AtsBoolean(false);
    AtsBoolean italic = new AtsBoolean(false);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color", color);
        propertyContainer.attachParam("height", height);
        propertyContainer.attachParam("position", pos);
        propertyContainer.attachParam("text", text);
        propertyContainer.attachParam("font", font);
        propertyContainer.attachParam("bold", bold);
        propertyContainer.attachParam("italic", italic);

    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        AtsProceduralTexture source = inputs_.get(0).getTexture();

        texture_.copy(source);
        BufferedImage image = texture_.getBufferedImage();
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(color.red.value / 255.f, color.green.value / 255.f, color.blue.value / 255.f,
                color.alpha.value / 255.f));

        int style = Font.PLAIN;
        if (bold.value_)
            style |= Font.BOLD;
        if (italic.value_)
            style |= Font.ITALIC;
        Font f = new Font(font.value_, style, height.value); /* Font.DIALOG */
        g2.setFont(f);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawString(text.value_, pos.x.value, pos.y.value + height.value);
        texture_.setBufferedImage(image);
        // System.exit(0);
    }

}
