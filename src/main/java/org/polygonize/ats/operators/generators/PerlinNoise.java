package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsBoolean;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.math.Vector2d;

//@formatter:off
@AtsOperatorMetadata(
    operatorDesignation = "perlin noise",
    category            = "generators",
    author              = "Johannes Diemke",
    version             = "1.0",
    description         = "This operator generates a XOR texture.",
    date                = "2008-08-08",
    sourceOfSupply      = "http://informatik.uni-oldenburg.de/~trigger"
)
//@formatter:on
public class PerlinNoise extends AtsDataSourceOperator {
    private AtsInteger seed = new AtsInteger(2);
    private AtsInteger frequency = new AtsInteger(1, 1, 255);
    private AtsInteger octaves = new AtsInteger(1, 1, 8);
    private AtsBoolean absolute = new AtsBoolean(false);
    private AtsEnumeration marble = new AtsEnumeration(
            new String[] { "disabled", "sin(noise(p))", "abs(sin(noise(p)))" });
    private AtsInteger periods = new AtsInteger(1, 1, 10);
    private AtsInteger distortion = new AtsInteger(1);
    AtsColor color = new AtsColor(255, 255, 255, 255);

    static Vector2d[][] grads;

    static {

    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color", color);
        propertyContainer.attachParam("frequency", frequency);
        propertyContainer.attachParam("fbm octaves", octaves);
        propertyContainer.attachParam("random seed", seed);
        propertyContainer.attachParam("absolute noise", absolute);
        propertyContainer.attachParam("marble mode", marble);
        propertyContainer.attachParam("periods", periods);
        propertyContainer.attachParam("displacement", distortion);

    }

    public void process() {

        int freq = frequency.value;
        int o = Math.max(1, octaves.value);
        int scale = 2;

        float dist = distortion.value / 255.f * 0.1f;

        int rand = seed.value;

        grads = new Vector2d[256][256];

        AtsUtil.srand(rand);
        for (int y = 0; y < grads.length; y++) {
            for (int x = 0; x < grads[y].length; x++) {

                Vector2d grad;

                do {
                    grad = new Vector2d(AtsUtil.rand() % 256 - 127, AtsUtil.rand() % 256 - 127).normalize();
                } while (grad.length() == 0);

                grads[y][x] = grad;
            }

        }

        for (int x = 0; x < 256; x++)
            for (int y = 0; y < 256; y++) {

                double fbm = 0;
                scale = 2;
                float maxvalue = 0;
                for (int i = 0; i < o; i++) {
                    if (absolute.value_)
                        fbm += Math.abs(
                                (noise(x / 256.0 * freq * scale, y / 256.0 * freq * scale, freq * scale, freq * scale)
                                        + 1) / 2 - 0.5)
                                * 2 / scale;
                    else
                        fbm += (noise(x / 256.0 * freq * scale, y / 256.0 * freq * scale, freq * scale, freq * scale)
                                + 1) / 2 / scale;

                    maxvalue += 1.0f / scale;

                    scale = Math.min(255, scale * 2);

                }

                fbm = fbm * (1.0 / maxvalue);

                double value;

                if (marble.selected == 1) {
                    value = (1 + Math.sin(x / 256.0 * Math.PI * 2 * 1 + 1 * dist * fbm * 255)) / 2;
                } else if (marble.selected == 1) {
                    value = Math.abs(Math.sin(x / 256.0 * Math.PI * 1 + 1 * dist * fbm * 255));
                } else {
                    value = fbm;
                }

                texture_.setPixel(x, y, (int) (value * 255), (int) (value * 255), (int) (value * 255), 255);
            }

    }

    public double noise(double x, double y, int wrapx, int wrapy) {

        int floorx = (int) Math.floor(x);
        int floory = (int) Math.floor(y);

        x = x - floorx;
        y = y - floory;

        wrapy = Math.min(256, wrapy);
        wrapx = Math.min(256, wrapx);

        Vector2d g00 = grads[(floory) % wrapy][(floorx) % wrapx];
        Vector2d g10 = grads[(floory) % wrapy][(floorx + 1) % wrapx];
        Vector2d g01 = grads[(floory + 1) % wrapy][(floorx) % wrapx];
        Vector2d g11 = grads[(floory + 1) % wrapy][(floorx + 1) % wrapx];

        double n00 = g00.dot(new Vector2d(x, y));
        double n10 = g10.dot(new Vector2d(x - 1, y));
        double n01 = g01.dot(new Vector2d(x, y - 1));
        double n11 = g11.dot(new Vector2d(x - 1, y - 1));

        double u = fade(x);
        double v = fade(y);

        double n00n10 = mix(n00, n10, u);
        double n01n11 = mix(n01, n11, u);

        double result = mix(n00n10, n01n11, v);

        return result;
    }

    private static double mix(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

}