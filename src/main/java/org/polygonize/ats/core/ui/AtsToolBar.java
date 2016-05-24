package org.polygonize.ats.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.polygonize.ats.ApplicationController;

public class AtsToolBar extends JToolBar implements ActionListener {

    AtsToolBar() {
        super("Still draggable");
        // putClientProperty(SubstanceLookAndFeel.FLAT_PROPERTY,
        // false);
        // setFloatable(false);
        // setRollover(false);
        JButton saveDocument = new JButton();
        JButton saveAsDocument = new JButton();
        JButton openDocument = new JButton();
        JButton newDocument = new JButton();

        newDocument.setActionCommand("new");
        newDocument.setToolTipText("New");
        newDocument.addActionListener(this);
        // newDocument.setIcon(new ImageIcon("resources/document-new.png"));
        newDocument.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/document-new.png")));

        saveDocument.setActionCommand("save");
        saveDocument.setToolTipText("Save");
        saveDocument.addActionListener(this);
        saveDocument.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/document-save.png")));

        saveAsDocument.setActionCommand("saveas");
        saveAsDocument.setToolTipText("Save As...");
        saveAsDocument.addActionListener(this);
        saveAsDocument.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/document-save-as.png")));

        openDocument.setActionCommand("open");
        openDocument.setToolTipText("Open...");
        openDocument.addActionListener(this);
        openDocument.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/document-open.png")));

        add(newDocument);
        add(openDocument);
        add(saveDocument);
        add(saveAsDocument);
        addSeparator();

        // add(new JButton(new ImageIcon("resources/document-properties.png")));

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("exitMenuItem")) {
            ApplicationController.getInstance().close();
        }

        if (e.getActionCommand().equals("open")) {
            ApplicationController.getInstance().openProject();
        }

        if (e.getActionCommand().equals("saveas")) {
            ApplicationController.getInstance().saveAs();
        }

        if (e.getActionCommand().equals("save")) {
            ApplicationController.getInstance().save();
        }

        if (e.getActionCommand().equals("new")) {
            ApplicationController.getInstance().newProject();
        }

    }

}
