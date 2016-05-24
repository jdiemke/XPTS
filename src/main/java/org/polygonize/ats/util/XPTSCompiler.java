package org.polygonize.ats.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class XPTSCompiler {

    private static XPTSCompiler instance_;
    public File javaSrcFile;

    public static XPTSCompiler getInstance() {
        if (instance_ == null) {
            instance_ = new XPTSCompiler();
        }
        return instance_;
    }

    public void createSourceFile() {
        javaSrcFile = new File("./plugins/Plugin.java");

        String code = "package org.polygonize.ats.operators;\n" + "\n"
                + "import org.polygonize.ats.core.operator.AtsDataSourceOperator;\n"
                + "import org.polygonize.ats.core.operator.AtsOperatorMetadata;\n"
                + "import org.polygonize.ats.util.AtsInteger;\n"
                + "import org.polygonize.ats.util.AtsPropertyContainer;\n" + "import org.polygonize.ats.util.AtsUtil;\n"
                + "\n" + "// new metadata\n" + "@AtsOperatorMetadata(\n"
                + "		operatorDesignation = \"first plugin\",\n"
                + "		category			= \"generator\",\n"
                + "		author			= \"Johannes Diemke\",\n"
                + "		version			= \"1.0\",\n"
                + "		description 		= \"This operator generates a XOR texture.\",\n"
                + "		date				= \"2008-08-08\",\n"
                + "		sourceOfSupply		= \"http://informatik.uni-oldenburg.de/~trigger\"\n" + ")\n"
                + "\n" + "public class Plugin extends AtsDataSourceOperator {\n" + "\n"
                + "	AtsInteger distort_ = new AtsInteger(0);\n" + "\n"
                + "	public void edit(AtsPropertyContainer container) {\n"
                + "		container.addInt(\"Distort:\", distort_);\n" + "	}\n" + "\n"
                + "	public void process() {\n" + "		AtsUtil.srand(distort_.getValue());\n" + "\n"
                + "		for(int y=0; y < 256;y++)\n" + "			for(int x=0; x< 256;x++){\n"
                + "				int color = AtsUtil.rand() % 256;\n"
                + "				texture_.setPixel(x,y,color, color, color,255);\n"
                + "			}\n" +

                "	}\n" + "}";
        Writer p;
        try {
            p = new FileWriter(javaSrcFile);
            p.write(code);

            p.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String compile(File file) {
        // http://www.tutego.de/blog/javainsel/2010/04/java-compiler-api-teil-1-grundlagen/
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> units;
        units = manager.getJavaFileObjectsFromFiles(Arrays.asList(file));
        Iterable<String> options = Arrays.asList(new String[] { "-d", "./plugins/bin", "-verbose" });
        // Iterable<String> options = Arrays.asList( new String[]{"-d",
        // "./plugins/bin"});
        StringWriter sw = new StringWriter();
        CompilationTask task = compiler.getTask(sw, manager, null, options, null, units);

        task.call();
        try {
            manager.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sw.getBuffer().toString();
    }

}
