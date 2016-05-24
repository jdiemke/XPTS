package org.polygonize.ats.core.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.polygonize.ats.core.ui.ImageFactory.ImageType;

public class TextRenderer {

    private static TextRenderer instance_;

    private static int[] fontWidth = new int[] { 4, 3, 7, 7, 7, 7, 7, 7, 4, 4, 7, 7, 7, 7, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 3, 7, 7, 7, 7, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 7, 7, 3, 7, 7, 5, 9, 7, 7, 7, 7, 6, 7, 5, 7, 7, 9, 7, 7, 7, 7, 7, 7, 7,
            7 };

    private BufferedImage pixelFont;
    private BufferedImage pixelFontShadow;

    public static TextRenderer getInstance() {

        if (instance_ == null) {
            instance_ = new TextRenderer();
        }

        return instance_;
    }

    private TextRenderer() {
        pixelFont = ImageFactory.getInstance().getImage(ImageType.PIXEL_FONT);
        pixelFontShadow = ImageFactory.getInstance().getImage(ImageType.PIXEL_FONT_SHADOW);
    }

    public int getLength(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            int index = (char) (text.charAt(i) - ' ');

            width += fontWidth[index];

        }

        return width;
    }

    public void drawString(Graphics2D g2, String text, int x, int y) {

        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += fontWidth[(char) (text.charAt(i) - ' ')];

        }

        int xpos = x;
        for (int i = 0; i < text.length(); i++) {

            drawChar(g2, (char) (text.charAt(i) - ' '), xpos, y);
            xpos += fontWidth[(text.charAt(i) - ' ')];
        }

    }

    public void drawStringOp(Graphics2D g2, String text, int x, int y, int width_) {

        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += fontWidth[(char) (text.charAt(i) - ' ')];

        }

        int xpos = (int) (x + (width_ / 2.0) - (width) / 2.0);
        for (int i = 0; i < text.length(); i++) {

            drawChar(g2, (char) (text.charAt(i) - ' '), xpos, y);
            xpos += fontWidth[(text.charAt(i) - ' ')];
        }

    }

    public void drawChar(Graphics2D g2, char character, int x, int y) {
        int xpos = character % 16;
        int ypos = character / 16;
        g2.drawImage(pixelFontShadow, 0 + x + 1, 0 + y + 1, x + 12 + 1, y + 8 + 1, xpos * 12, ypos * 8, xpos * 12 + 12,
                ypos * 8 + 8, null);
        g2.drawImage(pixelFont, 0 + x, 0 + y, x + 12, y + 8, xpos * 12, ypos * 8, xpos * 12 + 12, ypos * 8 + 8, null);

    }

}
