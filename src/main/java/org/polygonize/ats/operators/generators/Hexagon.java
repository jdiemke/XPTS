package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.Vector3D;

@AtsOperatorMetadata(operatorDesignation = "hexagon", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/*
 * http://www.playchilla.com/how-to-check-if-a-point-is-inside-a-hexagon
 * http://www.formz.com/manuals/renderzone/!SSL!/WebHelp/html/
 * 2_2_3_wrapped_procedural.html
 * http://www.guerillarender.com/forum/viewtopic.php?id=293
 * http://www.neilblevins.com/cg_education/procedural_noise/procedural_noise.
 * html http://drilian.com/category/development/graphics/procedural-textures/
 * http://bemmu.blogspot.de/2011/02/foobar.html
 * http://graphics.ucsd.edu/courses/rendering/2006/agoldberg/index.html
 * http://www.iquilezles.org/www/articles/cellularffx/cellularffx.htm
 * http://www-cs-students.stanford.edu/~amitp/game-programming/polygon-map-
 * generation/
 * http://theinstructionlimit.com/author/renaudbedardrenaudbedard/page/3
 * http://carljohanrosen.com/processing/index.php?page=start&topic=1.0
 * http://webstaff.itn.liu.se/~stegu/webglshadertutorial/shadertutorial.html
 * http://webstaff.itn.liu.se/~stegu/
 * http://webstaff.itn.liu.se/~stegu/gpunoise/
 * http://webstaff.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
 * http://webstaff.itn.liu.se/~stegu/TNM084-2012/proceduraltextures.pdf
 * http://webstaff.itn.liu.se/~stegu/gpunoise/GPU-proceduralshading.pdf
 * http://webstaff.itn.liu.se/~stegu/TNM084-2012/10.1.1.95.412.pdf
 * http://webstaff.itn.liu.se/~stegu/TNM084-2012/p287-perlin.pdf
 * http://webstaff.itn.liu.se/~stegu/TNM084-2012/noise-eg2010.pdf
 * http://webstaff.itn.liu.se/~stegu/TNM084-2009/SL-tartan/
 * http://webstaff.itn.liu.se/~stegu/TNM084-2009/SL-tartan/
 * http://www.artofillusion.org/docs/texedtut/chapter9
 * http://webstaff.itn.liu.se/~stegu/TNM084-2012/SL-mosaic/mosaic.sl
 * http://devmag.org.za/2009/05/03/poisson-disk-sampling/
 * http://devmag.org.za/2011/06/25/implementing-and-debugging-the-perlin-noise-
 * algorithm/ http://devmag.org.za/2009/04/25/perlin-noise/
 */
public class Hexagon extends AtsDataSourceOperator {

    AtsPosition2D dimension = new AtsPosition2D(9, 6);
    AtsInteger mortar = new AtsInteger(0);
    AtsInteger slope = new AtsInteger(2);
    AtsInteger random = new AtsInteger(89);
    AtsColor color1 = new AtsColor(255, 0, 0, 255);
    AtsColor color2 = new AtsColor(255, 255, 255, 255);
    AtsColor color3 = new AtsColor(80, 80, 80, 255);

    public void edit(PropertyContainer container) {
        container.attachParam("count", dimension);
        container.attachParam("mortar width", mortar);
        container.attachParam("slope length", slope);
        container.attachParam("random seed", random);
        container.attachParam("brick color 1", color1);
        container.attachParam("brick color 2", color2);
        container.attachParam("mortar color", color3);

    }

    public boolean isInside(float x, float y, float width, float height) {
        float centerx = width / 2.0f;
        float centery = height / 2.0f;
        float vert = height / 4.0f;
        float hori = width / 2.0f;

        float q2x = Math.abs(x - centerx); // transform the test point locally
                                           // and to quadrant 2
        float q2y = Math.abs(y - centery); // transform the test point locally
                                           // and to quadrant 2
        if (q2x > hori || q2y > vert * 2)
            return false; // bounding test (since q2 is in quadrant 2 only 2
                          // tests are needed)
        return 2 * vert * hori - vert * q2x - hori * q2y >= 0; // finally the
                                                               // dot product
                                                               // can be reduced
                                                               // to this due to
                                                               // the hexagon
                                                               // symmetry
    }

    public float isInside2(float x, float y, float width, float height) {
        float centerx = width / 2.0f;
        float centery = height / 2.0f;
        float vert = height / 4.0f;
        float hori = width / 2.0f;

        float q2x = Math.abs(x - centerx); // transform the test point locally
                                           // and to quadrant 2
        float q2y = Math.abs(y - centery); // transform the test point locally
                                           // and to quadrant 2

        Vector3D p2 = new Vector3D(hori, vert, 0);
        Vector3D q2 = new Vector3D(q2x, q2y, 0);
        Vector3D n = new Vector3D();
        n.normalize(new Vector3D(-vert, -hori, 0));

        Vector3D m = q2.sub(p2);
        float dot = -(float) m.dot(n);

        // if (q2x > hori || q2y > vert*2) return false; // bounding test (since
        // q2 is in quadrant 2 only 2 tests are needed)
        return (float) Math.max(q2x - hori, dot);
        // return 2 * vert * hori - vert * q2x - hori * q2y >= 0; // finally the
        // dot product can be reduced to this due to the hexagon symmetry
    }

    public void process() {

        float width = 256 / (float) (dimension.x.value);
        float height = 256 / (6.0f * dimension.y.value);

        int count = dimension.x.value;
        int count2 = dimension.y.value * 2;
        int[] colors = new int[count * count2];

        AtsUtil.srand(random.value);
        for (int i = 0; i < count * count2; i++)
            colors[i] = AtsUtil.rand() % 256;

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                int color = 0;

                float row = ((y) / height);

                float dispx = 0, dispy = 0;

                float s = (x) % width;
                float t = (y) % (height * 6);
                float s2 = (x + width / 2.0f) % width;
                float t2 = (y + height * 3.0f) % (height * 6);

                int random = (int) (x / width) % count + (((int) (y / (height * 6)) * 2) % count2) * (count + 1);
                int random2 = (int) ((x + width / 2.0f) / width) % count
                        + (((int) ((y + height * 3.0f) / (height * 6)) * 2) % count2) * (count);

                float dist1 = isInside2(s, t, width, (height * 4));
                float dist2 = 10000;// isInside2(s2,t2,width, (height*4));

                float dist = Math.min(dist1, dist2);
                if (dist2 < dist1)
                    random = random2;

                float alpha = (-(dist + mortar.value / 2.f)) / slope.value;

                if (slope.value == 0)
                    alpha = (-(dist + mortar.value / 2.0f)) + 1.0f;

                if (alpha < 0)
                    alpha = 0;
                if (alpha > 1)
                    alpha = 1;
                color = (int) (AtsUtil.linearInterpolate(0, 255, alpha));

                // color = -(int)dist;

                // int r = (int)(alpha*((color2.red.value_ - color1.red.value_)
                // * colors[random%(count*count2)]/255.f + color1.red.value_));
                // int g = (int)(alpha*((color2.green.value_ -
                // color1.green.value_) * colors[random%(count*count2)]/255.f +
                // color1.green.value_));
                // int b = (int)(alpha*((color2.blue.value_ -
                // color1.blue.value_) * colors[random%(count*count2)]/255.f +
                // color1.blue.value_));
                int r = (int) (alpha
                        * ((color2.red.value - color1.red.value) * colors[random % (count * count2)] / 255.f
                                + color1.red.value)
                        + (1 - alpha) * color3.red.value);
                int g = (int) (alpha
                        * ((color2.green.value - color1.green.value) * colors[random % (count * count2)] / 255.f
                                + color1.green.value)
                        + (1 - alpha) * color3.green.value);
                int b = (int) (alpha
                        * ((color2.blue.value - color1.blue.value) * colors[random % (count * count2)] / 255.f
                                + color1.blue.value)
                        + (1 - alpha) * color3.blue.value);

                texture_.setPixel(x, y, r, g, b, 255);

            }

    }

}