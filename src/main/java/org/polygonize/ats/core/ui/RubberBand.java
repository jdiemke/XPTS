package org.polygonize.ats.core.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class RubberBand {
    public Point start = new Point();
    public Point end = new Point();

    public boolean visible_;

    RubberBand() {
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }

    BasicStroke focusRectStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1,
            new float[] { 4, 4 }, 0);
    Color focusRectColor = new Color(225, 150, 80, 255);

    public int x, y, width, height;

    public void computeRect() {
        int x1, x2, y1, y2;

        if (end.x < start.x) {
            x1 = end.x;
            x2 = start.x;
        } else {
            x1 = start.x;
            x2 = end.x;
        }

        if (end.y < start.y) {
            y1 = end.y;
            y2 = start.y;
        } else {
            y1 = start.y;
            y2 = end.y;
        }

        x = x1;
        y = y1;

        width = x2 - x1;
        height = y2 - y1;
    }

    void draw(Graphics2D g2) {
        if (visible_) {

            computeRect();

            g2.setStroke(focusRectStroke);
            g2.setColor(focusRectColor);
            g2.drawRect(x, y, width, height);
        }
    }

    public void setStart(int x, int y) {
        start.setLocation(x, y);
    }

    public void setEnd(int x, int y) {
        end.setLocation(x, y);
    }

}