package org.polygonize.ats.core.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.polygonize.ats.core.operator.AtsOperator;

public class GroupSelection {

    Vector<AtsOperator> selection = new Vector<AtsOperator>();
    boolean visible = false;
    public Point offset = new Point(0, 0);

    public Vector<AtsOperator> getSelection() {
        return selection;
    }

    public GroupSelection() {
        // TODO Auto-generated constructor stub
    }

    public void set(Vector<AtsOperator> ops) {
        selection = ops;
    }

    public void setVisible(boolean vis) {
        this.visible = vis;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public boolean intersects(AtsOperator op) {
        Vector<AtsOperator> temp = new Vector<AtsOperator>();
        for (AtsOperator ops : selection) {
            try {
                AtsOperator tempop = (AtsOperator) ops.clone();
                tempop.setPosition(tempop.getX() + offset.x, tempop.getY() + offset.y);
                temp.add(tempop);
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for (AtsOperator selected : temp) {
            if (selected.intersects(op))
                return true;
        }
        return false;
    }

    BasicStroke focusRectStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1,
            new float[] { 4, 4 }, 0);
    Color focusRectColor = new Color(150, 225, 80, 255);

    public void draw(Graphics2D g2) {

        Area area = new Area();
        if (visible)
            for (AtsOperator op : selection) {

                g2.setStroke(focusRectStroke);
                g2.setColor(focusRectColor);

                // g2.drawRect(op.getX()+offset.x,op.getY()+offset.y,
                // op.getWidth()-1, op.getHeight()-1);

                area.add(new Area(new Rectangle2D.Double(op.getX() + offset.x, op.getY() + offset.y, op.getWidth(),
                        op.getHeight())));

            }
        // g2.draw(getDaisyShape());
        g2.draw(area);

        Composite original = g2.getComposite();
        AlphaComposite ac1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);

        g2.setComposite(ac1);
        g2.fill(area);
        g2.setComposite(original);
    }

    public void computeNewPos() {
        for (AtsOperator op : selection) {

            op.setPosition(op.getX() + offset.x, op.getY() + offset.y);

        }

    }

}
