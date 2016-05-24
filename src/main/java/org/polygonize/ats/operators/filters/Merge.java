package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "merge", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/**
 * 
 * @see http://blackpawn.com/blog/photoshop-blend-modes/
 * @see http://www.pegtop.net/delphi/articles/blendmodes/
 */
public class Merge extends AtsProcessOperator {

    AtsEnumeration mode = new AtsEnumeration(
            new String[] { "normal", "add", "substract", "multiply", "screen", "darken", "lighten", "difference",
                    "negation", "exclusion", "overlay", "hard light", "soft light", "dodge", "burn" });

    AtsInteger alpha = new AtsInteger(255);

    public void edit(PropertyContainer container) {
        container.attachParam("blend mode", mode);
        container.attachParam("alpha", alpha);
    }

    public boolean isInputAccepted() {
        return inputs_.size() > 1;
    }

    public void process() {

        int startr, startg, startb, tempr, tempg, tempb, inputr, inputg, inputb;

        float alpha = this.alpha.value / 255.f;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                startr = tempr = AtsUtil.extractR(inputs_.get(0).getTexture().getPixel(x, y));
                startg = tempg = AtsUtil.extractG(inputs_.get(0).getTexture().getPixel(x, y));
                startb = tempb = AtsUtil.extractB(inputs_.get(0).getTexture().getPixel(x, y));

                for (int i = 1; i < inputs_.size(); i++) {

                    inputr = AtsUtil.extractR(inputs_.get(i).getTexture().getPixel(x, y));
                    inputg = AtsUtil.extractG(inputs_.get(i).getTexture().getPixel(x, y));
                    inputb = AtsUtil.extractB(inputs_.get(i).getTexture().getPixel(x, y));

                    switch (mode.selected) {

                    case 0: // normal
                        tempr = (int) (inputr);
                        tempg = (int) (inputg);
                        tempb = (int) (inputb);
                        break;
                    case 1: // add
                        tempr = (int) (tempr + inputr);
                        tempg = (int) (tempg + inputg);
                        tempb = (int) (tempb + inputb);
                        break;

                    case 2: // substract
                        tempr = (int) (tempr - inputr);
                        tempg = (int) (tempg - inputg);
                        tempb = (int) (tempb - inputb);
                        break;
                    case 3: // multiply
                        tempr = (int) (tempr * inputr / 255.f);
                        tempg = (int) (tempg * inputg / 255.f);
                        tempb = (int) (tempb * inputb / 255.f);
                        break;
                    case 4: // screen
                        tempr = (int) (255 - ((255 - tempr) * (255 - inputr) / 255.f));
                        tempg = (int) (255 - ((255 - tempg) * (255 - inputg) / 255.f));
                        tempb = (int) (255 - ((255 - tempb) * (255 - inputb) / 255.f));
                        break;
                    case 5: // darken
                        tempr = (int) (tempr < inputr ? tempr : inputr);
                        tempg = (int) (tempg < inputg ? tempg : inputg);
                        tempb = (int) (tempb < inputb ? tempb : inputb);
                        break;
                    case 6: // lighten
                        tempr = (int) (tempr > inputr ? tempr : inputr);
                        tempg = (int) (tempg > inputg ? tempg : inputg);
                        tempb = (int) (tempb > inputb ? tempb : inputb);
                        break;

                    case 7: // difference
                        tempr = (int) Math.abs(tempr - inputr);
                        tempg = (int) Math.abs(tempg - inputg);
                        tempb = (int) Math.abs(tempb - inputb);
                        break;
                    case 8: // negation
                        tempr = (int) (255 - Math.abs(255 - tempr - inputr));
                        tempg = (int) (255 - Math.abs(255 - tempg - inputg));
                        tempb = (int) (255 - Math.abs(255 - tempb - inputb));
                        break;
                    case 9: // exclusion
                        tempr = (int) (tempr + inputr - (tempr * inputr / 255.f * 2));
                        tempg = (int) (tempg + inputg - (tempg * inputg / 255.f * 2));
                        tempb = (int) (tempb + inputb - (tempb * inputb / 255.f * 2));
                        break;
                    case 10: // overlay
                        tempr = (int) (tempr < 128 ? (tempr * inputr / 255.f * 2)
                                : (255 - ((255 - tempr) * (255 - inputr) / 255.f * 2)));
                        tempg = (int) (tempg < 128 ? (tempg * inputg / 255.f * 2)
                                : (255 - ((255 - tempg) * (255 - inputg) / 255.f * 2)));
                        tempb = (int) (tempb < 128 ? (tempb * inputb / 255.f * 2)
                                : (255 - ((255 - tempb) * (255 - inputb) / 255.f * 2)));
                        break;
                    case 11: // hard light
                        tempr = (int) (inputr < 128 ? (tempr * inputr / 255.f * 2)
                                : (255 - ((255 - tempr) * (255 - inputr) / 255.f * 2)));
                        tempg = (int) (inputg < 128 ? (tempg * inputg / 255.f * 2)
                                : (255 - ((255 - tempg) * (255 - inputg) / 255.f * 2)));
                        tempb = (int) (inputb < 128 ? (tempb * inputb / 255.f * 2)
                                : (255 - ((255 - tempb) * (255 - inputb) / 255.f * 2)));
                        break;
                    case 12: // soft light
                        tempr = (int) (inputr < 128
                                ? (2 * tempr * inputr / 255.f + inputr * inputr / 255.f * (255 - inputr * 2) / 255.f)
                                : (Math.sqrt(tempr / 255.f) * (2 * inputr - 255)
                                        + (2 * tempr) * (255 - inputb) / 255.f));
                        tempg = (int) (inputg < 128
                                ? (2 * tempg * inputr / 255.f + inputg * inputg / 255.f * (255 - inputg * 2) / 255.f)
                                : (Math.sqrt(tempg / 255.f) * (2 * inputg - 255)
                                        + (2 * tempg) * (255 - inputg) / 255.f));
                        tempb = (int) (inputb < 128
                                ? (2 * tempb * inputr / 255.f + inputb * inputb / 255.f * (255 - inputb * 2) / 255.f)
                                : (Math.sqrt(tempb / 255.f) * (2 * inputb - 255)
                                        + (2 * tempb) * (255 - inputb) / 255.f));
                        break;
                    case 13: // dodge
                        tempr = (int) (inputr == 255 ? (255) : (tempr * 255.f / (255 - inputr)));
                        tempg = (int) (inputg == 255 ? (255) : (tempg * 255.f / (255 - inputg)));
                        tempb = (int) (inputb == 255 ? (255) : (tempb * 255.f / (255 - inputb)));
                        break;
                    case 14: // burn
                        tempr = (int) (inputr == 0 ? (0) : (255 - ((255 - tempr) * 255f / inputr)));
                        tempg = (int) (inputg == 0 ? (0) : (255 - ((255 - tempg) * 255f / inputg)));
                        tempb = (int) (inputb == 0 ? (0) : (255 - ((255 - tempb) * 255f / inputb)));
                        break;

                    }
                }

                int finalr = (int) (AtsUtil.clamp(tempr) * alpha + startr * (1 - alpha));
                int finalg = (int) (AtsUtil.clamp(tempg) * alpha + startg * (1 - alpha));
                int finalb = (int) (AtsUtil.clamp(tempb) * alpha + startb * (1 - alpha));

                texture_.setPixel(x, y, finalr, finalg, finalb, 255);
            }

    }

}
