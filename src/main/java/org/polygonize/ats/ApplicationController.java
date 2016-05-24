package org.polygonize.ats;

import java.io.File;

import javax.swing.JOptionPane;

import org.polygonize.ats.core.ui.AtsFileChooser;
import org.polygonize.ats.core.ui.AtsMainWindow;
import org.polygonize.ats.core.ui.AtsOperatorGraph;
import org.polygonize.ats.core.ui.AtsOperatorGraphView;
import org.polygonize.ats.core.ui.AtsPropertyView;
import org.polygonize.ats.core.xml.AtsXMLEncoder;

public class ApplicationController {

    private static ApplicationController instance_;

    public AtsMainWindow mainWindow;
    public boolean dataChanged = false;
    public File activeFile = null;

    public static ApplicationController getInstance() {

        if (instance_ == null) {
            instance_ = new ApplicationController();
        }

        return instance_;
    }

    public void setActiveFile(File file) {
        activeFile = file;
    }

    public void updateCaption() {
        mainWindow.setTitle((dataChanged ? "*" : "") + (activeFile == null ? "default.xml" : activeFile.getName())
                + " - Extensible Procedural Texturing System");
    }

    public void setDataChanged(boolean changed) {
        dataChanged = changed;
    }

    public void setMainWindow(AtsMainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public AtsMainWindow getMainWindow() {
        return this.mainWindow;
    }

    public void newProject() {

        boolean cancled = !showSaveDialog();
        if (cancled)
            return;

        AtsOperatorGraphView.getInstance().clearComments();
        AtsPropertyView.getInstance().removePreviewOp();
        AtsPropertyView.getInstance().removePropertyPanel();
        AtsOperatorGraph.getInstance().clear();
        AtsOperatorGraphView.getInstance().render();

        ApplicationController.getInstance().setActiveFile(null);
        ApplicationController.getInstance().setDataChanged(false);
        ApplicationController.getInstance().updateCaption();
    }

    public boolean saveAs() {
        return AtsFileChooser.saveDialog(activeFile);
    }

    public void openProject() {
        AtsFileChooser.openDialog();
    }

    public void save() {

        if (activeFile == null)
            saveAs();
        else {
            AtsXMLEncoder.getInstance().save(activeFile, AtsOperatorGraph.getInstance());
            ApplicationController.getInstance().setDataChanged(false);
            ApplicationController.getInstance().updateCaption();
        }
    }

    public boolean showSaveDialog() {

        if (dataChanged) {

            if (activeFile == null) {

                String options[] = { "Close without Saving", "Cancel", "Save As" };

                int code = JOptionPane.showOptionDialog(mainWindow,
                        "If you don't save, changes will be permanently lost.", "Save changes before closing?",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);

                if (code == JOptionPane.CLOSED_OPTION)
                    return false;
                if (code == 1)
                    return false;
                if (code == 2) {
                    if (!saveAs())
                        return false;
                }

            } else {

                String options[] = { "Close without Saving", "Cancel", "Save" };

                int code = JOptionPane.showOptionDialog(mainWindow,
                        "If you don't save, changes will be permanently lost.", "Save changes before closing?",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);

                if (code == JOptionPane.CLOSED_OPTION)
                    return false;
                if (code == 1)
                    return false;
                if (code == 2)
                    save();

            }

        }

        return true;
    }

    public void close() {
        boolean cancled = !showSaveDialog();

        if (cancled)
            return;

        ApplicationProperties.getInstance().setWindowLocation(mainWindow.getLocation());
        ApplicationProperties.getInstance().setRecentFiles();
        ApplicationProperties.getInstance().save();

        mainWindow.setVisible(false);
        mainWindow.dispose();
    }

}
