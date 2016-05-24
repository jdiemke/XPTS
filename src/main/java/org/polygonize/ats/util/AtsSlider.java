package org.polygonize.ats.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class AtsSlider extends JLabel implements FocusListener, MouseListener, MouseMotionListener, KeyListener {

    private AtsInteger integer;

    private int currentValue;
    private int oldValue;

    private Point oldMousePosition;

    PropertyChangeListener listener_;

    public AtsSlider(int min, int max, AtsInteger integer) {
        setHorizontalAlignment(JLabel.CENTER);
        setFocusable(true);

        addFocusListener(this);
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);

        this.integer = integer;
        setText(String.valueOf(integer.getValue()));
        currentValue = integer.getValue();

        putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1.0));

        // FIXME: do this better
        setPreferredSize(new Dimension(10, (int) new JComboBox().getPreferredSize().getHeight()));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listener_ = listener;
    }

    private SubstanceColorScheme getColorSchemeFill(boolean hasFocus) {
        if (hasFocus) {
            return SubstanceLookAndFeel.getCurrentSkin().getColorScheme(DecorationAreaType.NONE,
                    ColorSchemeAssociationKind.FILL, ComponentState.SELECTED);
        } else {
            return SubstanceLookAndFeel.getCurrentSkin().getColorScheme(DecorationAreaType.NONE,
                    ColorSchemeAssociationKind.FILL, ComponentState.DEFAULT);
        }
    }

    private SubstanceColorScheme getColorSchemeBorder(boolean hasFocus) {
        if (hasFocus) {
            return SubstanceLookAndFeel.getCurrentSkin().getColorScheme(DecorationAreaType.NONE,
                    ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED);
        } else {
            return SubstanceLookAndFeel.getCurrentSkin().getColorScheme(DecorationAreaType.NONE,
                    ColorSchemeAssociationKind.BORDER, ComponentState.DEFAULT);
        }
    }

    @Override
    public void paint(Graphics g) {
        boolean focus = hasFocus();

        SubstanceColorScheme fillColorScheme = getColorSchemeFill(focus);
        SubstanceColorScheme borderColorScheme = getColorSchemeBorder(focus);

        RoundRectangle2D outerRectangle = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
        RoundRectangle2D innerRectangle = new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);

        RoundRectangle2D outerRectangleLeft = new RoundRectangle2D.Float(0, 0, 16 - 1, getHeight() - 1, 4, 4);
        RoundRectangle2D innerRectangleLeft = new RoundRectangle2D.Float(1, 1, 16 - 3, getHeight() - 3, 4, 4);

        RoundRectangle2D outerRectangleRight = new RoundRectangle2D.Float(getWidth() - 1 - 15, 0, getWidth() - 1,
                getHeight() - 1, 6, 6);
        RoundRectangle2D innerRectangleRight = new RoundRectangle2D.Float(getWidth() - 3 - 15, 1, getWidth() - 3,
                getHeight() - 3, 6, 6);

        SubstanceLookAndFeel.getCurrentSkin().getFillPainter().paintContourBackground(g, this, this.getWidth(),
                this.getHeight(), outerRectangle, focus, fillColorScheme, true);

        g.setColor(Color.getHSBColor(83 / 369.f, 0.4f, 0.69f));

        g.fillRoundRect(1, 0,
                (int) ((getWidth() - 2) * ((float) (integer.value - integer.min) / (integer.max - integer.min))),
                getHeight() - 1, 6, 6);
        // g.setColor( Color.getHSBColor(83/369.f, 0.4f, 0.49f ));
        // g.drawRoundRect(1, 1, (int)((getWidth()-1)*((float)(integer.value_ -
        // integer.min)/(integer.max-integer.min))), getHeight()-3,6,6);

        setForeground(fillColorScheme.getForegroundColor());
        super.paint(g);

        SubstanceLookAndFeel.getCurrentSkin().getBorderPainter().paintBorder(g, this, this.getWidth(), getHeight(),
                outerRectangle, innerRectangle, borderColorScheme);
        // inner controls
        // SubstanceLookAndFeel.getCurrentSkin().getBorderPainter().paintBorder(g,
        // this,this.getWidth(), getHeight(),outerRectangleLeft,
        // innerRectangleLeft,borderColorScheme);
        // SubstanceLookAndFeel.getCurrentSkin().getBorderPainter().paintBorder(g,
        // this,this.getWidth(), getHeight(),outerRectangleRight,
        // innerRectangleRight,borderColorScheme);

    }

    @Override
    public void focusGained(FocusEvent e) {
        repaint();
        System.out.println("slider got focus");
    }

    @Override
    public void focusLost(FocusEvent e) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {
            currentValue = integer.defaultValue;
            integer.setValue(currentValue);
            setText(String.valueOf(currentValue));
            paintImmediately(new Rectangle(getSize()));
            listener_.propertyChanged();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocus(true);
        oldValue = currentValue;
        oldMousePosition = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentValue = oldValue + (int) ((float) (e.getPoint().x - oldMousePosition.x) / (getWidth() - 1)
                * (integer.max - integer.min));

        if (currentValue > integer.max)
            currentValue = integer.max;
        if (currentValue < integer.min)
            currentValue = integer.min;

        integer.setValue(currentValue);
        setText(String.valueOf(currentValue));
        repaint();
        if (listener_ != null)
            listener_.propertyChanged();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            currentValue += 1;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            currentValue -= 1;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            currentValue += 5;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            currentValue -= 5;

        if (currentValue > integer.max)
            currentValue = integer.max;
        if (currentValue < integer.min)
            currentValue = integer.min;

        integer.setValue(currentValue);
        setText(String.valueOf(currentValue));
        paintImmediately(new Rectangle(getSize()));
        listener_.propertyChanged();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}