package org.polygonize.ats.operators.generators;

import java.awt.Point;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsBoolean;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "cell", category = "generators", author = "Johannes Diemke", version = "1.0", description = "Generates cellular worley noise.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
/*
 * http://www.iquilezles.org/www/articles/voronoilines/voronoilines.htm
 * http://bit-player.org/2011/a-slight-discrepancy
 * http://www-cs-students.stanford.edu/~amitp/game-programming/polygon-map-
 * generation/
 * 
 * important: quasi randomness
 * http://welltemperedspreadsheet.wordpress.com/2012/01/09/quasi-random-
 * christmas-trees/
 * http://www.mathworks.de/de/help/stats/generating-quasi-random-numbers.html
 * http://www.johndcook.com/blog/2009/03/16/quasi-random-sequences-in-art-and-
 * integration/
 * http://www.centerspace.net/blog/category/nmath/nmath-tutorial/page/2/
 * http://grahamshawcross.com/2012/10/12/find-your-own-space/
 * http://en.wikipedia.org/wiki/Lloyd%27s_algorithm
 * http://simblob.blogspot.de/2010/09/polygon-map-generation-part-1.html
 * http://devmag.org.za/2009/05/03/poisson-disk-sampling/ !!!!!!!!!!!!
 * http://www.joesfer.com/
 * this.numberhttp://people.cs.ubc.ca/~rbridson/docs/bridson-siggraph07-
 * poissondisk.pdf
 * http://people.cs.ubc.ca/~rbridson/docs/bridson-siggraph07-poissondisk.pdf
 * http://www.generatorx.no/20050707/make-some-noise/
 * http://wiki.ohm-hochschule.de/roettger/index.php/Multimodale-Visualisierung/
 * 3DPerlinNoiseTexturen
 * http://wiki.ohm-hochschule.de/roettger/index.php/Multimodale-Visualisierung/
 * 3DPerlinNoiseTexturen http://www.filterforge.com/wiki/index.php/Noise_Lab
 * http://www.shaders.co.uk/ifw2_textures/whatsin10.htm
 * http://www.cg.tuwien.ac.at/research/publications/2008/weidlich_2008_magwpt/
 * http://www.war-worlds.com/blog/2012/06/planet-rendering---part-4-perlin-noise
 * http://digitalerr0r.wordpress.com/2011/05/18/xna-shader-programming-tutorial-
 * 26-bump-mapping-perlin-noise/
 * 
 * TODO: - different distributions
 */
public class Cell extends AtsDataSourceOperator {

    private AtsInteger seed_ = new AtsInteger(2);
    private AtsInteger count_ = new AtsInteger(2);
    private AtsInteger relax = new AtsInteger(2);
    private AtsInteger min = new AtsInteger(2);
    private AtsBoolean mosaic = new AtsBoolean(false);
    private AtsEnumeration type_ = new AtsEnumeration(
            new String[] { "F1", "1 - F1", "F2", "F3", "F2 - F1", "F1 + F2", "F1 * F2", "2 * F3 - F2 - F1" });
    private AtsEnumeration metric_ = new AtsEnumeration(
            new String[] { "euclidean", "manhatten", "quasi euclidean", "chebbyshev" });
    private AtsEnumeration distribution = new AtsEnumeration(new String[] { "pseudo random", "quasi random" });

    public void edit(PropertyContainer container) {
        container.attachParam("random seed", seed_);
        container.attachParam("quantity", count_);
        container.attachParam("distribution type", distribution);
        container.attachParam("minimum distance", min);
        container.attachParam("linear combination", type_);
        container.attachParam("metric", metric_);
        container.attachParam("mosaic", mosaic);
    }

    int number = 0;

    public void process() {
        number = count_.value;
        Cellular(seed_.value, count_.value, type_.selected, metric_.selected);
    }

    float[] distBuffer = new float[256 * 256];
    int[] colorBuffer = new int[256 * 256];
    Point[] points = new Point[255];
    int[] colors = new int[255];

    void uniformRandom(int number) {
        for (int loop1 = 0; loop1 < number; loop1++) {
            points[loop1] = new Point();
            points[loop1].x = AtsUtil.rand() % 256;
            points[loop1].y = AtsUtil.rand() % 256;
            colors[loop1] = AtsUtil.rand() % 256;
        }
    }

    // very bad implementation of poission sampling
    void possionRandom(int number) {

        boolean tooClose = false;
        float r = min.getValue();
        int x, y;

        points[0] = new Point(AtsUtil.rand() % 256, AtsUtil.rand() % 256);

        int failcount = 0;

        breakme: for (int i = 1; i < number; i++) {
            failcount = 0;
            do {
                x = AtsUtil.rand() % 256;
                y = AtsUtil.rand() % 256;
                tooClose = false;
                for (int j = 0; j < i; j++) {
                    int dx = Math.abs(x - points[j].x);

                    int dy = Math.abs(y - points[j].y);

                    if (dx > 256 / 2)
                        dx = 256 - dx;

                    if (dy > 256 / 2)
                        dy = 256 - dy;
                    float xl = dx;// x - points[j].x;
                    float yl = dy;// y - points[j].y;
                    if (xl * xl + yl * yl < r * r) {
                        tooClose = true;
                        failcount++;
                        break;
                    }
                }

                if (failcount > 16)
                    break breakme;
            } while (tooClose == true);

            points[i] = new Point(x, y);
            this.number = i + 1;
        }
    }

    void Cellular(int random, int number, int version, int metric) {

        float mindist = Float.MAX_VALUE;
        float maxdist = 0;

        AtsUtil.srand(random); // if you use the same value it will be every

        uniformRandom(number);

        if (distribution.selected == 1)
            possionRandom(number);

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {
                distBuffer[(y << 8) + x] = DistToNearestPoint(x, y, points, number, version, metric);
                if (distBuffer[(y << 8) + x] < mindist)
                    mindist = distBuffer[(y << 8) + x];
                if (distBuffer[(y << 8) + x] > maxdist)
                    maxdist = distBuffer[(y << 8) + x];
            }
        /*
         * for (int x=0;x<256;x++) for (int y=0;y<256;y++) { //
         * (1-((distBuffer... color1 =
         * (int)(red*(((distBuffer[(y<<8)+x]-mindist)/(maxdist-mindist))));
         * color2 =
         * (int)(green*(((distBuffer[(y<<8)+x]-mindist)/(maxdist-mindist))));
         * color3 =
         * (int)(blue*(((distBuffer[(y<<8)+x]-mindist)/(maxdist-mindist))));
         * texture_.setPixel(x, y, color1, color2, color3, 255); }
         */

        int red, green, blue;
        for (int i = 0; i < 256 * 256; i++) { // (1-((distBuffer...
            red = green = blue = (int) ((distBuffer[i] - mindist) / (maxdist - mindist) * 255);

            texture_.texels_[i] = ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16) | ((blue & 0x000000ff) << 8)
                    | ((255 & 0x000000ff));

        }

        if (mosaic.value_)
            for (int i = 0; i < 256 * 256; i++) { // (1-((distBuffer...
                red = green = blue = (int) (colorBuffer[i]);

                texture_.texels_[i] = ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16)
                        | ((blue & 0x000000ff) << 8) | ((255 & 0x000000ff));

            }

    }

    float[] F = new float[3];

    float DistToNearestPoint(int x, int y, Point[] points, int Number, int version, int metric) {
        float dist;

        F[2] = F[1] = F[0] = Float.MAX_VALUE;
        Number = this.number;

        // for (int count=0;count<255;count++) {
        // F[count] = Float.MAX_VALUE;
        // }

        for (int count = 0; count < Number; count++) {
            dist = wrapDist(x, y, points[count].x, points[count].y, metric);

            if (dist < F[0]) {
                F[2] = F[1];
                F[1] = F[0];
                F[0] = dist;
                colorBuffer[(y << 8) + x] = colors[count];

            } else if (dist < F[1]) {
                F[2] = F[1];
                F[1] = dist;
            } else if (dist < F[2]) {
                F[2] = dist;
            }

        }

        // Arrays.sort(F);

        switch (version) {
        case 0:
            return (F[0]); // F1
        case 1:
            return (-F[0]); // 1-F1
        case 2:
            return (F[1]); // F2
        case 3:
            return (F[2]); // F3
        case 4:
            return (F[1] - F[0]); // F2 - F1
        case 5:
            return (F[0] + F[1]); // F1 + F2
        case 6:
            return (F[0] * F[1]); // F1 * F2
        case 7:
            return (2 * F[2] - F[1] - F[0]); // 2 * F3 - F2 -F1
        default:
            return 0;
        }

    }

    // http://en.wikipedia.org/wiki/Chebyshev_distance
    float wrapDist(int x, int y, int px, int py, int metric) {
        int dx = Math.abs(x - px);

        int dy = Math.abs(y - py);

        if (dx > 256 / 2)
            dx = 256 - dx;

        if (dy > 256 / 2)
            dy = 256 - dy;
        switch (metric) {

        case 0:
            return (float) Math.sqrt(dx * dx + dy * dy); // euclidean distance
                                                         // 'euclidean'
        case 1:
            return (dx + dy); // manhattan distance 'cityblock'
        case 2:
            if (dx > dy) // 'quasi-euclidean'
                return dx + ((float) Math.sqrt(2) - 1) * dy;
            else
                return ((float) Math.sqrt(2) - 1) * dx + dy;
            // case 3: return Math.atan(dx + dy);
            // case 4: return Math.atan(dx) * Math.atan(dy);
        case 3:
            return Math.max(dx, dy); // chebbyshev distance 'chessboard'
        default:
            return 0.0f;
        }
    }

}
