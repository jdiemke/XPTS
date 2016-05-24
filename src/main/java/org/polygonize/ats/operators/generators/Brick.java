package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "brick", category = "generators", author = "Johannes Diemke", version = "1.0", description = "Generates cellular worley noise.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
/*
 * TODO: - color balance param - different presets for distance functions
 * 
 */
public class Brick extends AtsDataSourceOperator {

    AtsPosition2D dimension = new AtsPosition2D(new AtsInteger(5, 1, 100), new AtsInteger(14, 1, 100));
    // AtsPosition2D mortar = new AtsPosition2D(1, 2);
    AtsInteger mortar = new AtsInteger(1, 0, 255);
    AtsInteger slope = new AtsInteger(1);
    AtsInteger roundness = new AtsInteger(0);
    AtsInteger offset = new AtsInteger(0, 0, 20);

    AtsColor color1 = new AtsColor(255, 0, 0, 255);
    AtsColor color2 = new AtsColor(255, 255, 255, 255);
    AtsColor color3 = new AtsColor(80, 80, 80, 255);
    AtsInteger random = new AtsInteger(89);

    public void edit(PropertyContainer container) {
        container.attachParam("count", dimension);
        container.attachParam("mortar width", mortar);
        container.attachParam("slope length", slope);
        container.attachParam("roundness", roundness);
        container.attachParam("random seed", random);
        container.attachParam("offset", offset);
        container.attachParam("brick color 1", color1);
        container.attachParam("brick color 2", color2);
        container.attachParam("mortar color", color3);
    }

    /**
     * @see http://www.csee.umbc.edu/~olano/s2001c24/ch01.html
     *      http://www.csee.umbc.edu/~olano/s2001c24/
     *      http://digitalerr0r.wordpress.com/2011/05/15/xna-shader-programming-
     *      tutorial-25-perlin-noise-using-the-gpu/
     *      http://neotextureedit.sourceforge.net/
     *      http://www.andygibson.net/blog/news/jtexgen-procedural-texture-
     *      library-released/
     *      http://rusesoftware.com/index.php/11-tutorials/1-procedural-
     *      generation-textures-on-the-cpu
     *      http://libnoise.sourceforge.net/examples/worms/index.html
     *      http://http.developer.nvidia.com/GPUGems/gpugems_ch05.html
     *      http://drilian.com/2010/01/05/update-for-the-past-n-months/
     *      http://media2mult.uos.de/pmwiki/fields/cg-II-09/index.php?n=
     *      ProceduralGraphicsI.Beispieltextur http://athile.net/tools/patterns/
     *      http://www.altdevblogaday.com/2011/09/01/the-content-conundrum/
     *      vorheriges fuer motivation plus meridaartikel von pouet net ueber iq
     *      http://www.xorcyst.com/portfolio/
     *      http://stephencarmody.wikispaces.com/Simplex+Noise
     * 
     */
    public void process() {

        int dimx = dimension.x.value;
        int dimy = dimension.y.value;

        float brickWidth = 256.f / dimx;
        float brickHeight = 256.f / dimy;
        float mortarx = mortar.getValue() / 2.0f;
        float mortary = slope.getValue();// mortar.y.getValue();

        texture_.clear(255, 255, 255, 255);

        int rows[] = new int[dimy];
        for (int i = 0; i < dimy; i++)
            rows[i] = AtsUtil.rand() % (int) (brickWidth / 2) + (int) brickWidth / 2;

        AtsUtil.srand(random.value);
        int bricks[] = new int[dimy * dimx];
        for (int i = 0; i < dimy * dimx; i++)
            bricks[i] = AtsUtil.rand() % 256;

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                float row = ((y) / brickHeight);

                float disp = 0;

                AtsUtil.srand((int) row * 10);
                float offset = 0;
                if (row % 2 >= 1) {
                    disp = brickWidth / 2;
                    offset = this.offset.getValue() * (int) row;
                } else
                    offset = -this.offset.getValue() * (int) row;

                float s = ((x + disp + offset) % brickWidth + brickWidth) % brickWidth;
                float t = y % brickHeight;

                if (s < mortarx || t < mortary) {
                    texture_.setPixel(x, y, 0, 0, 0, 255);
                } else {
                    int color = 255;
                    texture_.setPixel(x, y, color, color, color, 255);
                }

                float dist = Math.max(s - brickWidth, 0 - s);
                dist = Math.max(dist, Math.max(t - brickHeight, 0 - t));
                // new try
                float radius = Math.min((brickHeight / 2 - mortarx / 2), (brickWidth / 2 - mortarx / 2))
                        * roundness.value / 255.f;
                dist = Math.abs(s - brickWidth / 2) - brickWidth / 2 + mortarx / 2;
                dist = Math.max(dist, Math.abs(t - brickHeight / 2) - brickHeight / 2 + mortarx / 2);
                float box = -(Math.abs(s - brickWidth / 2) - brickWidth / 2 + radius + mortarx / 2);
                box = Math.max(box, -(Math.abs(t - brickHeight / 2) - brickHeight / 2 + radius + mortarx / 2));
                dist = Math.max(dist, -box);
                float spherex = -(Math.abs(s - brickWidth / 2) - brickWidth / 2 + radius + mortarx / 2);
                float spherey = -(Math.abs(t - brickHeight / 2) - brickHeight / 2 + radius + mortarx / 2);
                float sphere = (float) Math.sqrt(spherex * spherex + spherey * spherey) - radius;
                dist = Math.min(dist, sphere);

                // http://www.mathworks.de/de/help/pde/ug/represent-geometries-with-rounded-corners-using-csg-modeling.html

                // float alpha = (-(dist+mortarx))/mortary;
                // //alpha = (-(dist+mortarx));
                //
                // if(mortary==0) alpha = (-(dist+mortarx))+1.0f;

                float alpha = -(dist);
                alpha = (alpha);

                if (alpha < 0)
                    alpha = 0;
                if (alpha > 1)
                    alpha = 1;

                int color = (int) (AtsUtil.linearInterpolate(0, 255, alpha));

                int random = (int) ((((x + disp + offset) / brickWidth) % dimx + dimx) % dimx
                        + ((int) (y / brickHeight) * dimx));
                // random = (int)(((x) /brickWidth)%dimx +
                // ((int)(y/brickHeight)*dimx));

                int r = (int) (alpha * ((color2.red.value - color1.red.value) * bricks[random % (dimy * dimx)] / 255.f
                        + color1.red.value) + (1 - alpha) * color3.red.value);
                int g = (int) (alpha
                        * ((color2.green.value - color1.green.value) * bricks[random % (dimy * dimx)] / 255.f
                                + color1.green.value)
                        + (1 - alpha) * color3.green.value);
                int b = (int) (alpha * ((color2.blue.value - color1.blue.value) * bricks[random % (dimy * dimx)] / 255.f
                        + color1.blue.value) + (1 - alpha) * color3.blue.value);

                texture_.setPixel(x, y, r, g, b, 255);
                // test
            }

        // int width = 256/5, height = 256/9, mortar = 4, s, t;
        //
        // for(int x=0; x < 256; x++)
        // for(int y=0;y< 256; y++) {
        // int row = y/height;
        // s=x; t=y;
        // // offset even rows by half a row
        // if (row % 2 == 0) s += width/2;
        // // wrap texture coordinates to get "brick coordinates"
        // s = s % width;
        // t = t % height;
        // // pick a color for this pixel, brick or mortar
        // texture_.setPixel(x, y, 215,20, 20,255);
        // if (s < mortar || t < mortar)
        // texture_.setPixel(x, y, 90, 90, 90,255);
        // }

    }

}
