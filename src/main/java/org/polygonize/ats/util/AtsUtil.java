package org.polygonize.ats.util;

import java.util.Random;

public class AtsUtil {

    private static long holdrand_;

    final static int[] table = { 0, 16, 22, 27, 32, 35, 39, 42, 45, 48, 50, 53, 55, 57, 59, 61, 64, 65, 67, 69, 71, 73,
            75, 76, 78, 80, 81, 83, 84, 86, 87, 89, 90, 91, 93, 94, 96, 97, 98, 99, 101, 102, 103, 104, 106, 107, 108,
            109, 110, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 128, 128, 129, 130,
            131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 144, 145, 146, 147, 148, 149, 150,
            150, 151, 152, 153, 154, 155, 155, 156, 157, 158, 159, 160, 160, 161, 162, 163, 163, 164, 165, 166, 167,
            167, 168, 169, 170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178, 179, 180, 181, 181, 182,
            183, 183, 184, 185, 185, 186, 187, 187, 188, 189, 189, 190, 191, 192, 192, 193, 193, 194, 195, 195, 196,
            197, 197, 198, 199, 199, 200, 201, 201, 202, 203, 203, 204, 204, 205, 206, 206, 207, 208, 208, 209, 209,
            210, 211, 211, 212, 212, 213, 214, 214, 215, 215, 216, 217, 217, 218, 218, 219, 219, 220, 221, 221, 222,
            222, 223, 224, 224, 225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231, 231, 232, 232, 233, 234,
            234, 235, 235, 236, 236, 237, 237, 238, 238, 239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 245,
            245, 246, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253, 253, 254, 254, 255 };

    static Random randomizer = new Random();

    // seed the random number generator
    public static void srand(int seed) {
        holdrand_ = (long) seed;
        // randomizer.setSeed(seed);
    }

    /**
     * pseudo-random number based on linear congruential method
     * 
     * @return a pseudo-random number 0 through 32767.
     * @see http://en.wikipedia.org/wiki/Linear_congruential_generator
     */
    public static int rand() {
        holdrand_ = holdrand_ * 214013L + 2531011L;
        return (int) ((holdrand_ >> 16) & 0x7fff);
        // return (int)(randomizer.nextInt());

    }

    // http://martin.ankerl.com/2009/01/05/approximation-of-sqrtx-in-java/
    public static float fastSqrt(float x) {
        float y = Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));

        // repeat the following line for more precision
        y = (y + x / y) * 0.5f;
        return y;
    }

    public static int clamp(int value) {
        if (value > 255)
            value = 255;
        else if (value < 0)
            value = 0;

        return value;
    }

    public static int repeat(int value) {
        return value & 0x000000ff;
    }

    /**
     * interpolates linear between two values
     * 
     * @param y1
     *            is the first value
     * @param y2
     *            is the second value
     * @param mu
     *            is a value between 0 and 1
     * @return interpolated value
     * 
     * @see http://local.wasp.uwa.edu.au/~pbourke/other/interpolation/
     */
    public static float linearInterpolate(float y1, float y2, float mu) {
        return (y1 * (1 - mu) + y2 * mu);
    }

    public static float cosineInterpolate(float y1, float y2, float mu) {
        float mu2;

        mu2 = (float) (1 - Math.cos(mu * Math.PI)) / 2;
        return (y1 * (1 - mu2) + y2 * mu2);
    }

    public static float cosineInterpolate2(float start, float end, float x) {
        float mu2;

        if (x < start)
            return 0.0f;
        if (x > end)
            return 1.0f;

        float mu = (x - start) / (end - start);

        return (float) (1 - Math.cos(mu * Math.PI)) / 2;

    }

    public static float cubicInterpolate(float y0, float y1, float y2, float y3, float mu) {
        float a0, a1, a2, a3, mu2;

        mu2 = mu * mu;
        a0 = y3 - y2 - y0 + y1;
        a1 = y0 - y1 - a0;
        a2 = y2 - y0;
        a3 = y1;

        return (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
    }

    // Tension: 1 is high, 0 normal, -1 is low
    // Bias: 0 is even,
    // positive is towards first segment,
    // negative towards the other
    public static float hermiteInterpolate(float y0, float y1, float y2, float y3, float mu, float tension,
            float bias) {
        float m0, m1, mu2, mu3;
        float a0, a1, a2, a3;

        mu2 = mu * mu;
        mu3 = mu2 * mu;

        m0 = (y1 - y0) * (1 + bias) * (1 - tension) / 2;
        m0 += (y2 - y1) * (1 - bias) * (1 - tension) / 2;
        m1 = (y2 - y1) * (1 + bias) * (1 - tension) / 2;
        m1 += (y3 - y2) * (1 - bias) * (1 - tension) / 2;

        a0 = 2 * mu3 - 3 * mu2 + 1;
        a1 = mu3 - 2 * mu2 + mu;
        a2 = mu3 - mu2;
        a3 = -2 * mu3 + 3 * mu2;

        return (a0 * y1 + a1 * m0 + a2 * m1 + a3 * y2);
    }

    public static HSV RGB2HSV(RGB rgb) {
        HSV hsv = new HSV();

        float max = Math.max(Math.max(rgb.red, rgb.green), rgb.blue);
        float min = Math.min(Math.min(rgb.red, rgb.green), rgb.blue);

        hsv.value = max;
        hsv.saturation = max != 0.0 ? (max - min) / max : 0.0f;

        if (hsv.saturation == 0.0f)
            hsv.hue = 0.0f;
        else {
            float delta = max - min;

            if (rgb.red == max)
                hsv.hue = (rgb.green - rgb.blue) / delta;
            if (rgb.green == max)
                hsv.hue = 2.0f + (rgb.blue - rgb.red) / delta;
            if (rgb.blue == max)
                hsv.hue = 4.0f + (rgb.red - rgb.green) / delta;

            hsv.hue *= 60.0f;

            if (hsv.hue < 0.0f)
                hsv.hue += 360.0f;
        }

        return hsv;
    }

    public static RGB HSV2RGB(HSV hsv) {
        RGB rgb = new RGB();

        if (hsv.saturation == 0.0f) {
            rgb.red = hsv.value;
            rgb.green = hsv.value;
            rgb.blue = hsv.value;
        } else {
            float hue = hsv.hue;

            if (hue == 360.0f)
                hue = 0.0f;

            hue /= 60.0f;
            int i = (int) Math.floor(hue);
            float f = hue - i;

            float p = hsv.value * (1.0f - hsv.saturation);
            float q = hsv.value * (1.0f - (hsv.saturation * f));
            float t = hsv.value * (1.0f - (hsv.saturation * (1.0f - f)));

            switch (i) {
            case 0:
                rgb.red = hsv.value;
                rgb.green = t;
                rgb.blue = p;
                break;
            case 1:
                rgb.red = q;
                rgb.green = hsv.value;
                rgb.blue = p;
                break;
            case 2:
                rgb.red = p;
                rgb.green = hsv.value;
                rgb.blue = t;
                break;
            case 3:
                rgb.red = p;
                rgb.green = q;
                rgb.blue = hsv.value;
                break;
            case 4:
                rgb.red = t;
                rgb.green = p;
                rgb.blue = hsv.value;
                break;
            case 5:
                rgb.red = hsv.value;
                rgb.green = p;
                rgb.blue = q;
                break;
            }
        }

        return rgb;
    }

    public static int extractR(int color) {
        return ((color >> 24) & 0x000000ff);
    }

    public static int extractG(int color) {
        return ((color >> 16) & 0x000000ff);
    }

    public static int extractB(int color) {
        return ((color >> 8) & 0x000000ff);
    }

    public static int extractA(int color) {
        return ((color >> 0) & 0x000000ff);
    }

}
