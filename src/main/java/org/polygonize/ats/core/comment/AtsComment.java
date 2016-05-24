package org.polygonize.ats.core.comment;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.polygonize.ats.core.ui.FocusRect;
import org.polygonize.ats.core.ui.TextRenderer;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsText;

public class AtsComment {

    BufferedImage image_, image2_, pixelFont, pixelFontShadow;
    int XPosition = 16;
    int YPosition = 16;
    int width = 16 * 12;
    int height = 16 * 4;
    int grabBarWidth_ = 16;
    int grabBarHeight_ = 16;

    AtsText text_ = new AtsText("This is a simple comment, known as annotation in XPTS. Try it yourself!!!");

    boolean selected_ = false;
    TextRenderer textRenderer = TextRenderer.getInstance();

    public String getText() {
        return text_.value_;
    }

    public AtsComment() {

        try {
            image_ = ImageIO.read(this.getClass().getResourceAsStream("/images/comments/commentGreen.png"));
            image2_ = ImageIO.read(this.getClass().getResourceAsStream("/images/comments/commentGreenSelected.png"));
            pixelFont = ImageIO.read(this.getClass().getResourceAsStream("/images/font/pixelfont.png"));
            pixelFontShadow = ImageIO.read(this.getClass().getResourceAsStream("/images/font/pixelfontshadow.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public AtsComment(int x, int y, int width, int height, String text) {
        this();
        XPosition = x;
        YPosition = y;
        this.width = width;
        this.height = height;
        text_.setValue(text);
    }

    public boolean isPointInDragArea(Point point) {
        return XPosition <= point.x && point.x <= XPosition + width - 1 && YPosition <= point.y
                && point.y <= YPosition + height - 1;
    }

    public void draw(Graphics2D g2) {

        drawTile(g2, 0, XPosition, YPosition);
        drawTile(g2, 2, XPosition + width - 16, YPosition);
        drawTile(g2, 6, XPosition, YPosition + height - 16);
        drawTile(g2, 8, XPosition + width - 16, YPosition + height - 16);
        for (int i = 0; i < width / 16 - 2; i++) {
            drawTile(g2, 1, XPosition + i * 16 + 16, YPosition);
            drawTile(g2, 7, XPosition + i * 16 + 16, YPosition + height - 16);
        }

        for (int i = 0; i < height / 16 - 2; i++) {
            drawTile(g2, 3, XPosition, YPosition + i * 16 + 16);
            drawTile(g2, 5, XPosition + width - 16, YPosition + i * 16 + 16);
        }

        for (int x = 0; x < width / 16 - 2; x++)
            for (int y = 0; y < height / 16 - 2; y++) {
                drawTile(g2, 4, XPosition + x * 16 + 16, YPosition + y * 16 + 16);
            }

        StringTokenizer tokenizer = new StringTokenizer(text_.value_, " \n", true);

        String line = "";

        int posy = YPosition + 16;

        int maxlines = (height - 32) / 12;

        int lines = 1;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (token.equals("\n"))
                continue;

            if (line.length() != 0 && textRenderer.getLength((line + token).trim()) > width - 32) {
                textRenderer.drawString(g2, line.trim(), XPosition + 16, posy);
                posy += 12;
                line = token.trim();
                if (lines > maxlines)
                    break;
                lines++;
            } else {
                line += token;
            }

        }

        if (!(lines > maxlines))
            textRenderer.drawString(g2, line.trim(), XPosition + 16, posy);
        // drawString(g2, text_.value_, XPosition+16, YPosition+16);

    }

    // void drawOp(Graphics2D g2) {
    // drawTile(g2, 0, positionX_, positionY_);
    // for(int i = 0; i < width_/16 -2; i++)
    // drawTile(g2, 1, positionX_+ i*16+16, positionY_);
    // drawTile(g2, 3, positionX_+ width_ -16, positionY_);
    // }
    void drawTile(Graphics2D g2, int index, int x, int y) {
        int posx = (index % 3) * 16;
        int posy = (index / 3) * 16;

        if (selected_)
            g2.drawImage(image2_, x, y, x + 16, y + 16, posx, posy, posx + 16, posy + 16, null);
        else
            g2.drawImage(image_, x, y, x + 16, y + 16, posx, posy, posx + 16, posy + 16, null);
        // int

        // g2.drawImage(image_, 0+x, 0+y, 16+x, 16+y, 0 + index*16, 0, 16+
        // index*16, 16, null);

    }

    public void setSelected(boolean b) {
        selected_ = b;

    }

    public int getX() {
        // TODO Auto-generated method stub
        return XPosition;
    }

    public int getY() {
        return YPosition;
    }

    public boolean isSelected() {
        // TODO Auto-generated method stub
        return selected_;
    }

    public void setBounds(FocusRect focusRect_) {
        XPosition = focusRect_.x_;
        YPosition = focusRect_.y_;

        width = focusRect_.width_;
        height = focusRect_.height_;

    }

    public int getWidth() {
        // TODO Auto-generated method stub
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void edit(AtsPropertyContainer container) {
        container.attachParam("comment", text_);

    }

    public boolean isPointInResizeArea(Point point) {
        return XPosition + width - grabBarWidth_ <= point.x && point.x <= XPosition + width - 1
                && YPosition + height - grabBarHeight_ <= point.y && point.y <= YPosition + height - 1;
    }

    public void setPosition(int x, int y) {
        XPosition = x;
        YPosition = y;

    }
}
