package org.polygonize.ats.core.xml;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.polygonize.ats.core.RecentFiles.RecentFiles;
import org.polygonize.ats.core.comment.AtsComment;
import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.plugin.AtsPlugInSystem;
import org.polygonize.ats.core.ui.AtsCommentHolder;
import org.polygonize.ats.core.ui.AtsMenuBar;
import org.polygonize.ats.core.ui.AtsOperatorGraph;
import org.polygonize.ats.core.ui.AtsOperatorGraphView;
import org.polygonize.ats.core.ui.AtsPropertyView;
import org.polygonize.ats.util.PropertyContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AtsXMLDecoder {

    private static AtsXMLDecoder instance_;

    private AtsXMLDecoder() {
    }

    public static AtsXMLDecoder getInstance() {
        if (instance_ == null) {
            instance_ = new AtsXMLDecoder();
        }
        return instance_;
    }

    public void load(File file, AtsOperatorGraph operatorGraph) {

        // clear the graph!!!
        AtsOperatorGraph.getInstance().clear();
        AtsOperatorGraphView.getInstance().clearComments();

        Document document = parseXmlFile(file, false);

        // DOCTYPE CAUSE document to be null
        if (document == null) {
            System.err.println("null node!");
            System.err.println(file);
            return;
        }

        Element root = document.getDocumentElement();

        if (!root.getNodeName().equals("xptsml")) {
            System.err.println(file + " is not a XPTS document!");
            return;
        }

        if (!root.getAttribute("version").equals("0.1.3")) {
            System.err.println(file + " has a wrong XPTS document version!");
            return;
        }

        parseOperators(root, operatorGraph);
        parseComments(root);

        RecentFiles.getInstance().put(file);
        AtsMenuBar.getInstance().update();

        operatorGraph.connectOperators();
        AtsPropertyView.getInstance().removePreviewOp();
        AtsPropertyView.getInstance().removePropertyPanel();

        AtsOperatorGraphView.getInstance().render();

        AtsOperator operator = operatorGraph.getActive();
        if (operator != null) {
            // atsOperatorGraph_.connectOperators();
            // AtsOperatorGraph.getInstance().setDirty();
            operator.evaluate();
            AtsPropertyView.getInstance().setOperator(operator);
        }

        if (operatorGraph.getSelected().size() == 1)
            operator = operatorGraph.getSelected().get(0);
        if (operator != null) {
            AtsPropertyView.getInstance().setPropertyPanel(operator);
        }
    }

    public void parseOperators(Element element, AtsOperatorGraph operatorGraph) {
        NodeList list = element.getElementsByTagName("operator");

        for (int i = 0; i < list.getLength(); i++) {
            createOperator((Element) list.item(i));
        }
    }

    public void parseComments(Element element) {
        NodeList list = element.getElementsByTagName("comment");

        for (int i = 0; i < list.getLength(); i++) {
            createComment((Element) list.item(i));
        }
    }

    void createComment(Element element) {
        String width = element.getAttribute("width");
        String height = element.getAttribute("height");
        String x = element.getAttribute("x");
        String y = element.getAttribute("y");

        NodeList list = element.getElementsByTagName("param");

        String text = "";

        for (int i = 0; i < list.getLength(); i++) {
            if (((Element) list.item(i)).getAttribute("name").equals("comment"))
                text = ((Element) list.item(i)).getAttribute("value");

        }

        AtsComment comment = new AtsComment(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width),
                Integer.parseInt(height), text);

        AtsCommentHolder.getInstance().add(comment);
    }

    void createOperator(Element element) {
        try {
            String id = element.getAttribute("id");
            String type = element.getAttribute("type");
            String x = element.getAttribute("x");
            String y = element.getAttribute("y");
            String width = element.getAttribute("width");
            String active = element.getAttribute("active");
            String selected = element.getAttribute("selected");
            String name = element.getAttribute("name");

            Map<String, Class<? extends AtsOperator>> plugins = AtsPlugInSystem.getInstance().getPlugins();
            Class<? extends AtsOperator> cls = plugins.get(type);

            AtsOperator op = cls.newInstance();

            if (op != null) {
                op.setId(Integer.parseInt(id));
                op.setPosition(Integer.parseInt(x), Integer.parseInt(y));
                op.setWidth(Integer.parseInt(width));
                op.setSelected(Boolean.parseBoolean(selected));
                op.setActiveView(Boolean.parseBoolean(active));
                op.setName(name);

                createParams(element, op);

                AtsOperatorGraph.getInstance().add(op);
            }

            // render();

        } catch (InstantiationException e1) {
        } catch (IllegalAccessException e1) {
        }
    }

    public void createParams(Element element, AtsOperator op) {
        NodeList list = element.getElementsByTagName("param");

        PropertyContainer propertyContainer = new PropertyContainer();
        op.edit(propertyContainer);

        for (int i = 0; i < list.getLength(); i++) {
            System.out.println("name " + ((Element) list.item(i)).getAttribute("name") + " ");

            if (propertyContainer.getParam(((Element) list.item(i)).getAttribute("name")) != null)
                propertyContainer.getParam(((Element) list.item(i)).getAttribute("name"))
                        .parse(((Element) list.item(i)).getAttribute("value"));
        }

    }

    public Document parseXmlFile(File file, boolean validating) {
        try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);
            // factory.setNamespaceAware(false);

            // Create the builder and parse the file
            Document doc = factory.newDocumentBuilder().parse(file);
            return doc;
        } catch (SAXException e) {
            // A parsing error occurred; the xml input is not valid
        } catch (ParserConfigurationException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
