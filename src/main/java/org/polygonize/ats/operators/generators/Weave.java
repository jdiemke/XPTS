package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "weave", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/*
 * http://athile.net/tools/patterns/
 * http://www.formz.com/manuals/renderzone/!SSL!/WebHelp/html/
 * 2_2_3_wrapped_procedural.html
 * http://graphics.ucsd.edu/courses/rendering/2006/agoldberg/index.html
 * http://graphics.ucsd.edu/courses/rendering/2003/cyrus/scatterbrain.html
 * http://forum.unity3d.com/threads/145345-Turbulence-Library-Massive-collection
 * -of-complex-and-fast-noise-on-the-GPU
 * http://drilian.com/2007/11/04/slow-progress-is-still-progress/
 * http://drilian.com/2007/10/27/jpeg-buoys-amidst-a-sea-of-text/
 * http://www.gamedev.net/blog/33/entry-1830272-programmer-art/
 * http://www.gamedev.net/blog/33/entry-2138456-seamless-noise/
 * http://www.neilblevins.com/cg_education/procedural_noise/procedural_noise.
 * html http://widgg-research.blogspot.de/2011_06_01_archive.html
 * http://widgg-research.blogspot.de/
 * http://algorithmic-worlds.net/info/info.php?page=pg-perlin
 * http://swiftcoder.wordpress.com/2008/11/12/erosion-take-one/
 * http://swiftcoder.wordpress.com/2008/11/11/3d-noise/
 * http://swiftcoder.wordpress.com/2008/11/08/noise-again/
 * http://swiftcoder.wordpress.com/2008/11/07/noise-library/
 * http://www.noisemachine.com/talk1/23.html
 * http://www.iquilezles.org/www/articles/morenoise/morenoise.htm
 * http://www.nullpointer.co.uk/content/terrain-generation/
 * http://www.iquilezles.org/www/articles/warp/warp.htm
 * http://www.andymator.co.uk/shaders/fbm/index.html
 * http://www.infinity-universe.com/Infinity/index.php?option=com_content&task=
 * view&id=99&Itemid=27 http://swiftcoder.wordpress.com/2008/11/08/noise-again/
 * http://www.spiegel.de/wissenschaft/mensch/numerator-was-muscheln-und-
 * praesidenten-gemeinsam-haben-a-648499.html
 */
public class Weave extends AtsDataSourceOperator {

    AtsPosition2D quantity = new AtsPosition2D(new AtsInteger(1, 1, 10), new AtsInteger(1, 1, 10));
    AtsPosition2D width = new AtsPosition2D(new AtsInteger(90, 1, 100), new AtsInteger(90, 1, 100));
    AtsInteger falloff = new AtsInteger(70, 0, 255);
    AtsColor color1 = new AtsColor(255, 156, 92, 255);
    AtsColor color2 = new AtsColor(255, 156, 255, 255);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("quantity", quantity);
        propertyContainer.attachParam("thread width", width);
        propertyContainer.attachParam("falloff", falloff);
        propertyContainer.attachParam("weaver color 1", color1);
        propertyContainer.attachParam("weaver color 2", color2);
    }

    public void process() {

        int quantityx = quantity.x.value;
        int quantityy = quantity.y.value;

        texture_.clear(0, 0, 0, 255);

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                float s = x / 256.0f * 2 * quantityx;
                float t = y / 256.0f * 2 * quantityy;

                int ss = (int) (s);
                int tt = (int) (t);

                int parity = ((ss + tt) % 2);

                float fs = s % 1;
                float ft = t % 1;
                fs = Math.abs(0.5f - fs);
                ft = Math.abs(0.5f - ft);

                float fall = falloff.value / 255.f * 2;

                float dt = 0.5f + (float) Math.pow(Math.cos(ft * Math.PI), fall) / 2.0f;
                float ds = 0.5f + (float) Math.pow(Math.cos(fs * Math.PI), fall) / 2.0f;

                int r1 = color1.red.value;
                int g1 = color1.green.value;
                int b1 = color1.blue.value;

                int r2 = color2.red.value;
                int g2 = color2.green.value;
                int b2 = color2.blue.value;
                float w = 0.5f * width.x.value / 100;
                float h = 0.5f * width.y.value / 100;

                if (parity == 0) {

                    if (fs < w) {
                        int colorr = (int) (dt * r1);
                        int colorg = (int) (dt * g1);
                        int colorb = (int) (dt * b1);
                        texture_.setPixel(x, y, colorr, colorg, colorb, 255);
                    } else if (ft < h) {
                        int colorr = (int) ((1 - ds) * r2);
                        int colorg = (int) ((1 - ds) * g2);
                        int colorb = (int) ((1 - ds) * b2);
                        texture_.setPixel(x, y, colorr, colorg, colorb, 255);
                    }

                } else {

                    if (ft < h) {

                        int colorr = (int) (ds * r2);
                        int colorg = (int) (ds * g2);
                        int colorb = (int) (ds * b2);
                        texture_.setPixel(x, y, colorr, colorg, colorb, 255);
                    } else if (fs < w) {
                        int colorr = (int) ((1 - dt) * r1);
                        int colorg = (int) ((1 - dt) * g1);
                        int colorb = (int) ((1 - dt) * b1);
                        texture_.setPixel(x, y, colorr, colorg, colorb, 255);
                    }

                }

            }
    }

}
