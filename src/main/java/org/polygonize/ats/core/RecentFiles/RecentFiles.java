package org.polygonize.ats.core.RecentFiles;

import java.io.File;
import java.util.Vector;

public class RecentFiles {

    private static RecentFiles instance_;

    Vector<File> recentFiles_;

    private RecentFiles() {
        recentFiles_ = new Vector<File>();
    }

    public static RecentFiles getInstance() {
        if (instance_ == null) {
            instance_ = new RecentFiles();
        }
        return instance_;
    }

    public void put(File file) {
        // LRU strategie
        recentFiles_.remove(file);
        recentFiles_.add(0, file);

        if (recentFiles_.size() > 10)
            recentFiles_.setSize(10);
    }

    public void clear() {
        recentFiles_.clear();
    }

    public Vector<File> getRecentFiles() {
        return recentFiles_;
    }

}
