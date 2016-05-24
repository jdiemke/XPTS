package org.polygonize.ats.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AtsSliderPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, FocusListener {
    int value = 0, oldvalue = 0;
    Point p;
    JLabel label;
    JPanel panel;
    AtsInteger integer_;
    PropertyChangeListener listener_;

    public AtsSliderPanel(String caption, AtsInteger integer) {
        setLayout(new GridLayout(1, 1));
        // setBackground(new Color(79, 74, 59));
        // JLabel cap = new JLabel(caption);
        // cap.setForeground(Color.LIGHT_GRAY);
        // add(cap);
        integer_ = integer;
        value = integer_.getValue();
        panel = new JPanel(new BorderLayout());

        label = new JLabel(String.valueOf(value));
        label.addMouseListener(this);
        label.setBackground(Color.DARK_GRAY);
        label.setForeground(Color.LIGHT_GRAY);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        label.addMouseMotionListener(this);
        panel.setFocusable(true);
        panel.addKeyListener(this);
        panel.addFocusListener(this);
        // panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(label);
        setPreferredSize(new Dimension(256, 17));
        // setMaximumSize(new Dimension(100,0));
        add(panel);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listener_ = listener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        p = e.getPoint();
        oldvalue = value;
        label.setForeground(new Color(0xF4A817));
        panel.requestFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        label.setForeground(Color.LIGHT_GRAY);

    }

    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        value = oldvalue + (e.getPoint().x - p.x);
        if (value < 0)
            value = 0;
        if (value > 255)
            value = 255;

        integer_.setValue(value);
        listener_.propertyChanged();

        label.setText(String.valueOf(value));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            value += 1;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            value -= 1;

        if (value > 255)
            value = 255;
        if (value < 0)
            value = 0;

        integer_.setValue(value);
        listener_.propertyChanged();

        label.setText(String.valueOf(value));

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void focusGained(FocusEvent e) {
        label.setBackground(Color.GRAY);
        label.setForeground(new Color(0xF4A817));
        // setBorder(BorderFactory.createLineBorder(Color.RED));

    }

    @Override
    public void focusLost(FocusEvent e) {
        label.setBackground(Color.DARK_GRAY);
        label.setForeground(Color.LIGHT_GRAY);

    }

}