package org.polygonize.ats.core.ui;

import java.io.File;

import javax.swing.JFileChooser;

import org.polygonize.ats.ApplicationController;
import org.polygonize.ats.core.xml.AtsXMLDecoder;
import org.polygonize.ats.core.xml.AtsXMLEncoder;

public class AtsFileChooser {

    @SuppressWarnings("unchecked")
    public static void openDialog() {
        // UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("./textures"));
        fileChooser.setDialogTitle("Open XPTS Texture File As");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileChooser.setFileView(new ScenarioFileView());
        fileChooser.setFileFilter(new ScenarioFileFilter());
        // fileChooser.setAccessory(new ScenarioAcessory(fileChooser));

        int state = fileChooser.showOpenDialog(ApplicationController.getInstance().getMainWindow());

        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            boolean cancled = !ApplicationController.getInstance().showSaveDialog();

            if (cancled)
                return;

            AtsXMLDecoder.getInstance().load(file, AtsOperatorGraph.getInstance());
            ApplicationController.getInstance().setActiveFile(file);
            ApplicationController.getInstance().setDataChanged(false);
            ApplicationController.getInstance().updateCaption();

        }

    }

    public static boolean saveDialog(File file) {
        // UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileChooser = new JFileChooser();

        if (file != null) {
            fileChooser.setCurrentDirectory(file.getParentFile());
            fileChooser.setSelectedFile(file);
        } else {
            fileChooser.setCurrentDirectory(new File("./textures"));
            fileChooser.setSelectedFile(new File("./default.xml"));
        }

        fileChooser.setDialogTitle("Save XPTS Texture File");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileChooser.setFileView(new ScenarioFileView());
        fileChooser.setFileFilter(new ScenarioFileFilter());
        // fileChooser.setAccessory(new ScenarioAcessory(fileChooser));

        int state = fileChooser.showSaveDialog(ApplicationController.getInstance().getMainWindow());

        if (state == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();

            String name = (String) file.getAbsolutePath();

            if (!file.getName().toLowerCase().endsWith(".xml")) {
                name = name + ".xml";
                file = new File(name);
            }

            AtsXMLEncoder.getInstance().save(file, AtsOperatorGraph.getInstance());
            ApplicationController.getInstance().setActiveFile(file);
            ApplicationController.getInstance().setDataChanged(false);
            ApplicationController.getInstance().updateCaption();

            return true;
        } else
            return false;

    }
}