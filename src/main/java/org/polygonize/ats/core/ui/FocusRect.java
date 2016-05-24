package org.polygonize.ats.core.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import org.polygonize.ats.core.comment.AtsComment;
import org.polygonize.ats.core.operator.AtsOperator;

public class FocusRect {
    public int x_;
    public int y_;
    public int width_;
    public int height_;

    public boolean visible_;

    void setWidth(int width) {
        this.width_ = width;
    }

    void setHeight(int width) {
        this.height_ = width;
    }

    FocusRect() {
    }

    FocusRect(AtsOperator operator) {
        x_ = operator.getX();
        y_ = operator.getY();
        width_ = operator.getWidth();
        height_ = operator.getHeight();
    }

    void set(AtsOperator operator) {
        x_ = operator.getX();
        y_ = operator.getY();
        width_ = operator.getWidth();
        height_ = operator.getHeight();
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }

    BasicStroke focusRectStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1,
            new float[] { 4, 4 }, 0);
    Color focusRectColor = new Color(150, 225, 80, 255);

    void draw(Graphics2D g2) {
        if (visible_) {

            g2.setStroke(focusRectStroke);
            g2.setColor(focusRectColor);

            g2.drawRect(x_, y_, width_, height_);

            Composite original = g2.getComposite();
            AlphaComposite ac1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);

            g2.setComposite(ac1);
            g2.fillRect(x_, y_, width_, height_);
            g2.setComposite(original);
        }
    }

    public void set(AtsComment comment) {
        x_ = comment.getX();
        y_ = comment.getY();
        width_ = comment.getWidth();
        height_ = comment.getHeight();
    }
}