package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "spots", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

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
 */
public class Spots extends AtsDataSourceOperator {

    AtsPosition2D quantity = new AtsPosition2D(4, 4);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("quantity", quantity);
    }

    public void process() {

        int quantityx = quantity.x.value;
        int quantityy = quantity.y.value;
        float mortarx = 0f / 256 * 6;
        float mortary = 5.12f / 256 * 6;

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                float s = (0.5f - ((x * 6 / 256.0f) % 1));
                float t = (0.5f - ((y * 6 / 256.0f) % 1));

                float r = 0.5f;

                float dist = (float) Math.sqrt(s * s + t * t) - r;

                float alpha = (-(dist + mortarx)) / mortary;
                if (alpha < 0)
                    alpha = 0;
                if (alpha > 1)
                    alpha = 1;
                int color = (int) (AtsUtil.linearInterpolate(0, 255, alpha));

                texture_.setPixel(x, y, color, color, color, 255);
            }
    }

}
