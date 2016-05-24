package org.polygonize.ats.core.ui;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

public class ScenarioFileView extends FileView {

    @Override
    public Icon getIcon(File f) {

        if (f.isFile()) {
            if (f.getName().endsWith(".xml")) {
                return new ImageIcon("resources/text-x-boo.png");
            }
        }

        return null;
    }

    @Override
    public Boolean isTraversable(File f) {
        return null;
    }

    @Override
    public String getName(File f) {
        if (f.isFile())
            return f.getName();// .substring(0, f.getName().length()-4);
        else
            return null;
    }

}