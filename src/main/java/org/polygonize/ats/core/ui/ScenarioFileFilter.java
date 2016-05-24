package org.polygonize.ats.core.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ScenarioFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.isFile() && f.getName().endsWith(".xml");
    }

    @Override
    public String getDescription() {
        return "XPTS Texture File";
    }

}
