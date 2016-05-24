package org.polygonize.ats.core.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.polygonize.ats.core.operator.AtsDataSinkOperator;
import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProcessOperator;

/**
 * Class to load plugins
 */
public class AtsPlugInSystem {

    /**
     * Compares two operators by their designation (which is given as metadata)
     */
    private static class AtsOperatorComparator implements Comparator<Class<? extends AtsOperator>> {

        @Override
        public int compare(Class<? extends AtsOperator> o1, Class<? extends AtsOperator> o2) {

            String cls1, cls2;

            cls1 = o1.getCanonicalName();
            cls2 = o2.getCanonicalName();

            if (o1.getAnnotation(AtsOperatorMetadata.class) != null)
                cls1 = o1.getAnnotation(AtsOperatorMetadata.class).operatorDesignation();
            if (o2.getAnnotation(AtsOperatorMetadata.class) != null)
                cls2 = o2.getAnnotation(AtsOperatorMetadata.class).operatorDesignation();

            return cls1.compareToIgnoreCase(cls2);
        }

    }

    private static AtsPlugInSystem instance;
    private Map<String, Class<? extends AtsOperator>> plugins;

    /**
     * HashMap with categories
     */
    private SortedMap<String, SortedSet<Class<? extends AtsOperator>>> categorizedPlugins;

    private AtsPlugInSystem() {
        super();
        plugins = new HashMap<String, Class<? extends AtsOperator>>();
        categorizedPlugins = new TreeMap<String, SortedSet<Class<? extends AtsOperator>>>();
    }

    /**
     * @return Returns the instance of this singleton class
     */
    public static AtsPlugInSystem getInstance() {
        if (instance == null) {
            instance = new AtsPlugInSystem();
        }
        return instance;
    }

    /**
     * Add plugins from specified path to plugin list
     * 
     * @param path
     *            Relative path that points to a directory where plugins can be
     *            found
     * @return Same instance so that it can be used for initial configuration
     */
    public AtsPlugInSystem addPluginPath(String path) {
        loadPlugins(path);
        return this;
    }

    /**
     * @return Returns the loaded plugins from specified directories
     * @see AtsPlugInSystem#addPluginPath(String)
     */
    public Map<String, Class<? extends AtsOperator>> getPlugins() {
        return this.plugins;
    }

    /**
     * Returns a map with categories and plugins belonging to these categories
     * It is just another representation of {@link AtsPlugInSystem#getPlugins()}
     * 
     * @return Returns a map with categories and plugins belonging to these
     *         categories
     */
    public Map<String, SortedSet<Class<? extends AtsOperator>>> getCategorizedPlugins() {
        return categorizedPlugins;
    }

    /**
     * Manually adds a plugin to the internal plugin store
     * 
     * @param pluginClass
     *            Plugin to be added
     * @return true if the plugin was added, false if it wasn't added because
     *         it's invalid
     * 
     */

    public boolean addPlugin(Class<? extends AtsOperator> pluginClass) {

        if (!isValidPlugin(pluginClass)) {
            System.err.println(pluginClass.toString() + " is no valid plugin!");
            return false;
        }

        AtsOperatorMetadata metadata = pluginClass.getAnnotation(AtsOperatorMetadata.class);

        String key = "uncategorized";

        if (metadata != null) {
            key = metadata.category();
        }

        if (!this.categorizedPlugins.containsKey(key)) {
            this.categorizedPlugins.put(key, new TreeSet<Class<? extends AtsOperator>>(new AtsOperatorComparator()));
        }

        SortedSet<Class<? extends AtsOperator>> pluginSet = categorizedPlugins.get(key);
        pluginSet.add(pluginClass);

        plugins.put(pluginClass.getCanonicalName(), pluginClass);
        return true;
    }
    // public boolean addPlugin(Class<? extends AtsOperator> pluginClass) {
    //
    // if (!isValidPlugin(pluginClass)) {
    // System.err.println(pluginClass.toString() + " is no valid plugin!");
    // return false;
    // }
    //
    // AtsOperatorMetadata metadata =
    // pluginClass.getAnnotation(AtsOperatorMetadata.class);
    // if(metadata== null) return false;
    //
    //
    // if (!this.categorizedPlugins.containsKey(metadata.category())) {
    // this.categorizedPlugins.put(metadata.category(), new TreeSet<Class<?
    // extends AtsOperator>>(new AtsOperatorComparator()));
    // }
    //
    // SortedSet<Class<? extends AtsOperator>> pluginSet =
    // categorizedPlugins.get(metadata.category());
    // pluginSet.add(pluginClass);
    //
    // plugins.put(pluginClass.getCanonicalName(), pluginClass);
    // return true;
    // }

    /**
     * Manually adds a collection of plugins to the internal plugin store
     * 
     * @see AtsPlugInSystem#addPlugin(Class)
     * 
     * @param pluginClasses
     *            Collection of lasses to be added to the internal plugin store
     */
    public void addPlugins(Collection<? extends Class<AtsOperator>> pluginClasses) {
        for (Class<AtsOperator> pluginClass : pluginClasses) {
            this.addPlugin(pluginClass);
        }
    }

    /**
     * This method does the main work in classloading. A recursive search in
     * given directory for classfiles is performed and valid plugins are added
     * to the class internal plugins map
     * 
     * @param pluginPath
     *            Path where the plugins are located respectively where the
     *            search for plugins shall be performed
     */
    @SuppressWarnings("unchecked")
    private void loadPlugins(String pluginPath) {

        File pluginsRootDirectory = new File(pluginPath);
        if (!(pluginsRootDirectory.exists()
                && (pluginsRootDirectory.isDirectory() || pluginsRootDirectory.getAbsolutePath().endsWith(".jar")))) {
            // TODO: add following line from joern again
            // throw new RuntimeException(pluginsRootDirectory + " is an invalid
            // plugin classpath (no directory)");
            System.out.println(pluginsRootDirectory + " is an invalid plugin classpath (no directory)");
            return;
        }

        // Alle Klassen aus dem Pluginordner suchen
        List<File> classfiles = getClassfilesRecursive(pluginsRootDirectory);

        // URLClassloader mit pluginpfad initialisieren
        URLClassLoader loader = null;
        try {
            loader = new URLClassLoader(new URL[] { pluginsRootDirectory.toURI().toURL() });
        } catch (MalformedURLException e) {
            throw new RuntimeException("Plugin classpath " + pluginsRootDirectory + " is not useable!");
        }

        // Alle gefundenen Klassen laden und �berpr�fen ob es sich um
        // g�ltige AtsOperatoren handelt
        for (File file : classfiles) {
            String fullyQualifiedClassname = filenameToClassname(file, pluginsRootDirectory);
            Class<? extends AtsOperator> loadedClass;
            try {
                loadedClass = (Class<? extends AtsOperator>) loader.loadClass(fullyQualifiedClassname);
                this.addPlugin(loadedClass);
            } catch (ClassNotFoundException e) {
                System.err.println(fullyQualifiedClassname + " not found!\n");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that defines whether a plugin class is a valid class or not
     * 
     * @param classToTest
     *            Class to be tested for validity
     * @return True if the class is a valid plugin class, false otherwise
     */
    @SuppressWarnings("unchecked")
    private static boolean isValidPlugin(Class classToTest) {
        return classToTest != null && classToTest.getCanonicalName() != null // no
                                                                             // anonymous
                                                                             // classes
                && ((classToTest.getSuperclass() == AtsProcessOperator.class)
                        || (classToTest.getSuperclass() == AtsDataSourceOperator.class)
                        || (classToTest.getSuperclass() == AtsDataSinkOperator.class));
    }

    /**
     * Lists all classfiles in a directory and its subdirectories or in a jar
     * file recursive,
     * 
     * @param startingDirectory
     *            Directory or jar file where listing starts
     * @return List of all classfiles in startingDirectory and its
     *         subdirectories as File-Object
     */
    private static List<File> getClassfilesRecursive(File startingDirectory) {

        List<File> result = new ArrayList<File>();

        // jar files behandeln
        if (startingDirectory.exists() && startingDirectory.getAbsolutePath().endsWith(".jar")) {

            JarFile jar;
            try {
                jar = new JarFile(startingDirectory);
                Enumeration<JarEntry> en = jar.entries();
                while (en.hasMoreElements()) {
                    JarEntry jarEntry = en.nextElement();
                    if (jarEntry.getName().endsWith(".class") && !jarEntry.isDirectory()) {
                        result.add(new File(jarEntry.getName()));
                    }
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File[] classesAndDirs = startingDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // accept directories & classfiles
                return pathname.isDirectory() || pathname.isFile() && pathname.toString().endsWith(".class");
            }
        });

        for (File file : classesAndDirs) {
            if (file.isDirectory()) {
                List<File> sub = getClassfilesRecursive(file);
                result.addAll(sub); // add classfiles from subdir
            } else if (file.isFile()) {
                result.add(file); // add classfile
            }
        }
        return result;
    }

    /**
     * Converts a filename to a fully qualified java class name (with preceeding
     * package using the java dot notation).
     * 
     * If filename doesn't end with ".class" it's supposed to be a packagename.
     * If classpathRoot is set and a directory filenames preceeding path that
     * equals classpathRoot will be cut off, otherwise it's supposed that
     * filename is already a relative path, that shall be converted
     * 
     * @param classfile
     *            Path to a classfile or package (directory)
     * @param classpathRoot
     *            Optional path that is a suffix of filename and shall be
     *            removed for conversion
     * @return Dotted notated class- or packagename
     */
    private static String filenameToClassname(File classfile, File... classpathRoot) {

        String classfilename = null;
        if (classpathRoot.length == 1 && classpathRoot[0].getAbsolutePath().endsWith(".jar")) {
            classfilename = classfile.getPath();
            classfilename = classfilename.substring(0, classfilename.lastIndexOf(".class"));
            return replaceAll(classfilename, "/", "."); // TODO "File.separator"
                                                        // oder immer "/"?
        }

        try {
            classfilename = classfile.getCanonicalPath();

            boolean isPackagename = !classfilename.endsWith(".class");

            if (classpathRoot.length == 1 && classpathRoot[0].isDirectory()) {
                String suffixToRemove = classpathRoot[0].getCanonicalPath() + File.separator;

                classfilename = replaceAll(classfilename, suffixToRemove, "");
            }

            if (!isPackagename) {
                classfilename = classfilename.substring(0, classfilename.lastIndexOf(".class")); // remove
                                                                                                 // ".class"
            }

            classfilename = replaceAll(classfilename, File.separator, ".");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return classfilename;
    }

    private static String replaceAll(String original, String replace, String replacement) {

        String originalString = new String(original);

        int index = 0;
        while ((index = originalString.indexOf(replace)) != -1) {
            String before = originalString.substring(0, index);
            String after = originalString.substring(index + replace.length(), originalString.length());
            originalString = before + replacement + after;
        }

        return originalString;
    }
}
