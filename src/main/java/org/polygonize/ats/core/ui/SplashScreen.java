package org.polygonize.ats.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class SplashScreen extends JDialog implements MouseListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    BufferedImage img;
    Timer timer = new Timer();

    public SplashScreen(Component main) {
        // setModalityType(ModalityType.APPLICATION_MODAL);
        setAlwaysOnTop(true);
        setUndecorated(true);
        // getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

        setResizable(false);
        setSize(525, 158);
        setLocationRelativeTo(main);

        try {
            img = ImageIO.read(this.getClass().getResourceAsStream("/images/splashscreen/splashscreen.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Could not load Splashscreen");
        }
        JPanel image = new JPanel() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.drawImage(img, 0, 0, 525, 158, 0, 0, 525, 158, null);
            }
        };
        image.setBorder(BorderFactory.createLineBorder(new Color(42, 41, 39)));
        add(image);
        addMouseListener(this);
        setVisible(true);

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                setVisible(false);
                dispose();
            }
        }, 5000);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        setVisible(false);
        dispose();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
