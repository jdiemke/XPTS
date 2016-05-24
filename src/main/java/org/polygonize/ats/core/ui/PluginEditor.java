package org.polygonize.ats.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.polygonize.ats.ApplicationController;
import org.polygonize.ats.core.plugin.AtsPlugInSystem;
import org.polygonize.ats.util.XPTSCompiler;

public class PluginEditor extends JPanel implements ActionListener {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private RSyntaxTextArea textArea;
    private File sourceFile = null;
    JEditorPane console;

    private static final String text = "package org.polygonize.ats.operators;\n" + "\n"
            + "import org.polygonize.ats.core.operator.AtsDataSourceOperator;\n"
            + "import org.polygonize.ats.core.operator.AtsOperatorMetadata;\n"
            + "import org.polygonize.ats.util.AtsInteger;\n" + "import org.polygonize.ats.util.AtsPropertyContainer;\n"
            + "import org.polygonize.ats.util.AtsUtil;\n" + "\n" + "// new metadata\n" + "@AtsOperatorMetadata(\n"
            + "		operatorDesignation = \"white noise\",\n"
            + "		category			= \"generator\",\n"
            + "		author			= \"Johannes Diemke\",\n"
            + "		version			= \"1.0\",\n"
            + "		description 		= \"This operator generates a XOR texture.\",\n"
            + "		date				= \"2008-08-08\",\n"
            + "		sourceOfSupply		= \"http://informatik.uni-oldenburg.de/~trigger\"\n" + ")\n" + "\n"
            + "public class AtsWhiteNoiseOperator extends AtsDataSourceOperator {\n" + "\n"
            + "	AtsInteger distort_ = new AtsInteger(0);\n" + "\n"
            + "	public void edit(AtsPropertyContainer container) {\n"
            + "		container.addInt(\"Distort:\", distort_);\n" + "	}\n" + "\n"
            + "	public void process() {\n" + "		AtsUtil.srand(distort_.getValue());\n" + "\n"
            + "		for(int y=0; y < 256;y++)\n" + "			for(int x=0; x< 256;x++){\n"
            + "				int color = AtsUtil.rand() % 256;\n"
            + "				texture_.setPixel(x,y,color, color, color,255);\n" + "			}\n" +

            "	}\n" + "}";

    public PluginEditor() {

        setLayout(new BorderLayout());

        textArea = new RSyntaxTextArea(20, 80);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        textArea.setBackground(new JLabel().getBackground());
        textArea.setAntiAliasingEnabled(true);
        textArea.setAnimateBracketMatching(false);

        textArea.setCaretColor(textArea.getBackground().brighter().brighter().brighter());
        textArea.setCurrentLineHighlightColor(textArea.getBackground().brighter());
        textArea.setMarginLineColor(textArea.getBackground().darker());
        textArea.setMarginLineEnabled(true);
        textArea.setBracketMatchingEnabled(false);
        textArea.setSelectedTextColor(Color.ORANGE.brighter().brighter());
        textArea.setSelectionColor(Color.ORANGE.darker());
        textArea.setForeground(textArea.getBackground().brighter().brighter().brighter());
        RTextScrollPane sp = new RTextScrollPane(textArea, true, textArea.getBackground().brighter().brighter());
        // sp.setBackground(new Color(255,255,255));
        sp.setForeground(new Color(255, 255, 255));
        sp.getGutter().setBorderColor(textArea.getBackground().darker());
        // sp.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new
        // Double(0.0));
        /*
         * 
         */
        add(sp, BorderLayout.CENTER);

        JToolBar bar = new JToolBar();

        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("saveButton");
        saveButton.addActionListener(this);
        bar.add(saveButton);

        JButton newButton = new JButton("New");
        newButton.setActionCommand("newButton");
        newButton.addActionListener(this);
        bar.add(newButton);

        JButton openButton = new JButton("Open");
        openButton.setActionCommand("openButton");
        openButton.addActionListener(this);
        bar.add(openButton);

        JButton compileButton = new JButton("Compile");
        compileButton.setActionCommand("compileButton");
        compileButton.addActionListener(this);
        bar.add(compileButton);

        // cp.add(new JLabel("tzes"),BorderLayout.PAGE_START);
        add(bar, BorderLayout.PAGE_START);

        console = new JEditorPane();

        JScrollPane scrollpane = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        console.setContentType("text/html");
        console.setEditable(true);
        console.setBackground(new JLabel().getBackground());

        // scrollpane.setMinimumSize(new Dimension(0,0));
        scrollpane.setPreferredSize(new Dimension(0, 100));

        console.setContentType("text/plain");
        console.setFont(new Font("Monospaced", Font.PLAIN, 12));
        console.setEditable(false);
        // console.setBackground(Color.WHITE);

        add(scrollpane, BorderLayout.PAGE_END);
        console.setForeground(Color.getHSBColor(0 / 369.f, 0.4f, 0.99f));

        changeStyleProgrammatically();
        // setMinimumSize(getPreferredSize());

    }

    private void changeStyleProgrammatically() {

        // Set the font for all token types.
        setFont(textArea, new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Change a few things here and there.
        SyntaxScheme scheme = textArea.getSyntaxScheme();

        scheme.getStyle(Token.RESERVED_WORD).foreground = Color.getHSBColor(246 / 369.f, 0.4f, 1.f);
        scheme.getStyle(Token.SEPARATOR).foreground = textArea.getBackground().brighter().brighter().brighter();
        scheme.getStyle(Token.COMMENT_MULTILINE).foreground = Color.getHSBColor(112 / 369.f, 0.4f, 0.99f);
        scheme.getStyle(Token.DATA_TYPE).foreground = Color.getHSBColor(252 / 369.f, 0.2f, 0.8f);
        scheme.getStyle(Token.OPERATOR).foreground = textArea.getBackground().brighter().brighter().brighter();
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.getHSBColor(33 / 369.f, 0.4f, 0.99f);
        ;
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.getHSBColor(0 / 369.f, 0.4f, 0.99f);
        ;
        scheme.getStyle(Token.COMMENT_EOL).foreground = Color.getHSBColor(112 / 369.f, 0.4f, 0.99f);
        scheme.getStyle(Token.ANNOTATION).foreground = Color.getHSBColor(286 / 369.f, 0.3f, 0.99f);

        textArea.revalidate();

    }

    /**
     * Set the font for all token types.
     * 
     * @param textArea
     *            The text area to modify.
     * @param font
     *            The font to use.
     */
    public static void setFont(RSyntaxTextArea textArea, Font font) {
        if (font != null) {
            SyntaxScheme ss = textArea.getSyntaxScheme();
            ss = (SyntaxScheme) ss.clone();
            for (int i = 0; i < ss.getStyleCount(); i++) {
                if (ss.getStyle(i) != null) {
                    ss.getStyle(i).font = font;
                }
            }
            textArea.setSyntaxScheme(ss);
            textArea.setFont(font);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("newButton")) {
            textArea.setText(text);
        }

        if (e.getActionCommand().equals("saveButton")) {
            saveDialog();
        }

        if (e.getActionCommand().equals("openButton")) {
            openDialog();
        }

        if (e.getActionCommand().equals("compileButton")) {
            if (sourceFile != null) {
                String str = XPTSCompiler.getInstance().compile(sourceFile);

                console.setText(str);

                AtsPlugInSystem loader = AtsPlugInSystem.getInstance();
                loader.addPluginPath("./plugins/bin");
            }
        }
    }

    public void openDialog() {
        // UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("./plugins"));
        fileChooser.setDialogTitle("Load Plugin");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // fileChooser.setFileView(new ScenarioFileView());
        // fileChooser.setFileFilter(new ScenarioFileFilter());
        // fileChooser.setAccessory(new ScenarioAcessory(fileChooser));

        int state = fileChooser.showOpenDialog(ApplicationController.getInstance().getMainWindow());

        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            textArea.setText(getContents(file));

            sourceFile = file;

        }

    }

    static public String getContents(File aFile) {
        // ...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        try {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null; // not declared within while loop
                /*
                 * readLine is a bit quirky : it returns the content of a line
                 * MINUS the newline. it returns null only for the END of the
                 * stream. it returns an empty String if two newlines appear in
                 * a row.
                 */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

    public void saveDialog() {
        // UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("./plugins"));
        fileChooser.setDialogTitle("Save Plugin");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // fileChooser.setFileView(new ScenarioFileView());
        // fileChooser.setFileFilter(new ScenarioFileFilter());

        int state = fileChooser.showSaveDialog(ApplicationController.getInstance().getMainWindow());

        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            String name = (String) file.getAbsolutePath();

            if (!file.getName().toLowerCase().endsWith(".java")) {
                name = name + ".java";
                file = new File(name);
            }

            Writer p;
            try {
                p = new FileWriter(file);
                p.write(textArea.getText());

                p.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sourceFile = file;

        }

    }

}
