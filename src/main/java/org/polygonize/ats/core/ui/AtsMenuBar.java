package org.polygonize.ats.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.polygonize.ats.ApplicationController;
import org.polygonize.ats.core.RecentFiles.RecentFiles;
import org.polygonize.ats.core.xml.AtsXMLDecoder;

public class AtsMenuBar extends JMenuBar implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    JMenu openRecentMenuItem = null;
    private static AtsMenuBar instance_;
    JFrame blah;

    public static AtsMenuBar getInstance() {
        if (instance_ == null) {
            instance_ = new AtsMenuBar();
        }
        return instance_;
    }

    public void update() {
        openRecentMenuItem.removeAll();

        Vector<File> files = RecentFiles.getInstance().getRecentFiles();

        if (files.size() > 0)
            openRecentMenuItem.setEnabled(true);
        else {
            openRecentMenuItem.setEnabled(false);
        }

        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<File> files = RecentFiles.getInstance().getRecentFiles();
                for (File file : files) {
                    if (file.getAbsolutePath().equals(e.getActionCommand())) {
                        boolean cancled = !ApplicationController.getInstance().showSaveDialog();

                        if (cancled)
                            return;

                        AtsXMLDecoder.getInstance().load(file, AtsOperatorGraph.getInstance());
                        ApplicationController.getInstance().setActiveFile(file);
                        ApplicationController.getInstance().setDataChanged(false);
                        ApplicationController.getInstance().updateCaption();
                        return;
                    }
                }

            }
        };

        for (int i = 0; i < files.size(); i++) {
            JMenuItem item = new JMenuItem((i + 1) + " " + files.get(i).getName());

            item.setMnemonic(KeyEvent.VK_0 + i);
            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, ActionEvent.CTRL_MASK));
            item.setActionCommand(files.get(i).getAbsolutePath());
            item.addActionListener(listener);
            openRecentMenuItem.add(item);

        }

        openRecentMenuItem.addSeparator();
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.setMnemonic(KeyEvent.VK_C);
        clearItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RecentFiles.getInstance().clear();
                update();
            }
        });
        openRecentMenuItem.add(clearItem);

        blah.setJMenuBar(this);
    }

    void setParent(JFrame frame) {
        blah = frame;
    }

    public AtsMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setHorizontalAlignment(SwingConstants.LEFT);

        JMenu pluginMenu = new JMenu("Plugin");
        JMenu aboutMenu = new JMenu("Help");
        for (int i = 0; i < 10; i++) {
            aboutMenu.add(new JMenuItem("test"));
        }

        //
        //
        // item2.setMnemonic('L');
        // item2.setAccelerator(KeyStroke.getKeyStroke('L' ,Event.CTRL_MASK ));

        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open...");
        openRecentMenuItem = new JMenu("Open Recent");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        JMenuItem fileInfoMenuItem = new JMenuItem("File Info");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        openMenuItem.setActionCommand("openMenuItem");
        newMenuItem.setActionCommand("newMenuItem");
        saveMenuItem.setActionCommand("saveMenuItem");
        exitMenuItem.setActionCommand("exitMenuItem");
        saveAsMenuItem.setActionCommand("saveAsMenuItem");

        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        saveAsMenuItem.addActionListener(this);

        Vector<File> files = RecentFiles.getInstance().getRecentFiles();
        for (int i = 0; i < files.size(); i++) {
            openRecentMenuItem.add(new JMenuItem(files.get(i).getName()));
        }

        if (files.size() == 0)
            openRecentMenuItem.setEnabled(false);

        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        openRecentMenuItem.setMnemonic(KeyEvent.VK_R);

        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        fileInfoMenuItem.setMnemonic(KeyEvent.VK_F);
        fileInfoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));

        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);

        fileMenu.add(openRecentMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(fileInfoMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(exitMenuItem);

        add(fileMenu);
        add(pluginMenu);
        add(aboutMenu);
    }

    boolean blub = false;

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("exitMenuItem")) {
            ApplicationController.getInstance().close();
        }

        if (e.getActionCommand().equals("openMenuItem")) {
            ApplicationController.getInstance().openProject();
        }

        if (e.getActionCommand().equals("saveAsMenuItem")) {
            ApplicationController.getInstance().saveAs();
        }

        if (e.getActionCommand().equals("saveMenuItem")) {
            ApplicationController.getInstance().save();
        }

        if (e.getActionCommand().equals("newMenuItem")) {
            ApplicationController.getInstance().newProject();
        }

    }

}
