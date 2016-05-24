package org.polygonize.ats;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Vector;

import org.polygonize.ats.core.RecentFiles.RecentFiles;

public class ApplicationProperties {

    private static ApplicationProperties instance_;
    Properties properties;
    boolean saveAsXML = true;

    public static ApplicationProperties getInstance() {
        if (instance_ == null) {
            instance_ = new ApplicationProperties();
        }
        return instance_;
    }

    public ApplicationProperties() {
        properties = new Properties();

        properties.put("window.location.x", "0");
        properties.put("window.location.y", "0");

        clearRecentFiles();
    }

    void clearRecentFiles() {
        for (int i = 0; i < 10; i++) {
            properties.setProperty("recent.file." + i, "");
        }
    }

    public void setWindowLocation(Point location) {
        properties.setProperty("window.location.x", Integer.toString(location.x));
        properties.setProperty("window.location.y", Integer.toString(location.y));
    }

    public void setRecentFiles() {
        Vector<File> files = RecentFiles.getInstance().getRecentFiles();

        // clear all, because only the n recent files available will be
        // overwritten
        // so everything from n+1 on has to be removed manually!
        clearRecentFiles();

        for (int i = 0; i < files.size(); i++) {
            properties.setProperty("recent.file." + i, files.get(i).getAbsolutePath());
        }
    }

    public void getRecentFiles() {
        RecentFiles.getInstance().clear();

        for (int i = 0; i < 10; i++) {
            String value = properties.getProperty("recent.file." + i);

            if (!value.equals("")) {
                RecentFiles.getInstance().getRecentFiles().add(new File(value));
            }
        }
    }

    public Point getWindowLocation() {
        return new Point(Integer.parseInt(properties.getProperty("window.location.x")),
                Integer.parseInt(properties.getProperty("window.location.y")));
    }

    public void load() {
        if (saveAsXML) {
            loadXML();
        } else {
            loadTXT();
        }
    }

    public void save() {
        if (saveAsXML) {
            saveXML();
        } else {
            saveTXT();
        }
    }

    public void loadTXT() {
        try {
            FileReader fileReader = new FileReader("properties.cfg");
            properties.load(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("properties.cfg not available");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTXT() {
        try {
            FileWriter fileWriter = new FileWriter("properties.cfg");
            properties.store(fileWriter, "eXtensible Procedural Texturing System Preferences");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadXML() {
        try {
            FileInputStream fileInputStream = new FileInputStream("configuration.xml");
            properties.loadFromXML(fileInputStream);
        } catch (FileNotFoundException e) {
            System.out.println("could not find configuration.xml");
            System.out.println("using default configuration");
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveXML() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("configuration.xml");
            properties.storeToXML(fileOutputStream, "eXtensible Procedural Texturing System Preferences");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
