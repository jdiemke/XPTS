package org.polygonize.ats.core.xml;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JViewport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.polygonize.ats.core.RecentFiles.RecentFiles;
import org.polygonize.ats.core.comment.AtsComment;
import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.ui.AtsCommentHolder;
import org.polygonize.ats.core.ui.AtsMenuBar;
import org.polygonize.ats.core.ui.AtsOperatorGraph;
import org.polygonize.ats.core.ui.AtsOperatorGraphView;
import org.polygonize.ats.util.PropertyContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AtsXMLEncoder {

    private static AtsXMLEncoder instance_;

    private AtsXMLEncoder() {
    }

    public static AtsXMLEncoder getInstance() {
        if (instance_ == null) {
            instance_ = new AtsXMLEncoder();
        }
        return instance_;
    }

    public Element createParam(Document document, String name, String value) {
        Element param = document.createElement("param");
        param.setAttribute("name", name);
        param.setAttribute("value", value);

        return param;
    }

    public Element createParams(Document document, AtsOperator operator) {

        PropertyContainer propertyContainer = new PropertyContainer();
        operator.edit(propertyContainer);

        Vector<String> paramNames = propertyContainer.getParamNames();

        if (paramNames.size() == 0)
            return null;

        Element params = document.createElement("params");

        for (String paramName : paramNames) {
            params.appendChild(createParam(document, paramName, propertyContainer.getParam(paramName).toString()));
        }

        return params;
    }

    public Element createInput(Document document, String inputId) {
        Element input = document.createElement("input");
        input.setAttribute("idref", inputId);

        return input;
    }

    public Element createInputs(Document document, AtsOperator operator) {
        Element inputs = document.createElement("inputs");

        Vector<AtsOperator> inputVector = operator.getInputs();

        if (inputVector.size() == 0)
            return null;

        for (AtsOperator input : inputVector) {
            inputs.appendChild(createInput(document, Integer.toString(input.getId())));
        }

        return inputs;
    }

    public Element createOperator(Document document, AtsOperator operator) {
        Element op = document.createElement("operator");

        op.setAttribute("id", Integer.toString(operator.getId()));
        op.setAttribute("type", operator.getClass().getCanonicalName());
        op.setAttribute("x", Integer.toString(operator.getX()));
        op.setAttribute("y", Integer.toString(operator.getY()));
        op.setAttribute("width", Integer.toString(operator.getWidth()));
        op.setAttribute("selected", Boolean.toString(operator.isSelected()));
        op.setAttribute("active", Boolean.toString(operator.isActive()));
        op.setAttribute("name", operator.getName());

        Element inputs = createInputs(document, operator);
        Element params = createParams(document, operator);

        if (inputs != null)
            op.appendChild(inputs);

        if (params != null)
            op.appendChild(params);

        return op;
    }

    public Element createOperatorStack(Document document, AtsOperatorGraph operatorGraph) {
        Element operatorStack = document.createElement("operatorstack");

        Vector<AtsOperator> operators = operatorGraph.getOperators();

        for (AtsOperator op : operators) {
            operatorStack.appendChild(createOperator(document, op));
        }

        return operatorStack;
    }

    public Element createComment(Document document, AtsComment comment) {
        Element commentElement = document.createElement("comment");

        commentElement.setAttribute("x", Integer.toString(comment.getX()));
        commentElement.setAttribute("y", Integer.toString(comment.getY()));
        commentElement.setAttribute("width", Integer.toString(comment.getWidth()));
        commentElement.setAttribute("height", Integer.toString(comment.getHeight()));

        Element param = document.createElement("param");
        param.setAttribute("name", "comment");
        param.setAttribute("value", comment.getText());
        commentElement.appendChild(param);

        return commentElement;
    }

    public Element createComments(Document document) {
        Element commentsElement = document.createElement("comments");

        Vector<AtsComment> comments = AtsCommentHolder.getInstance().getComments();

        for (AtsComment comment : comments) {
            commentsElement.appendChild(createComment(document, comment));
        }

        return commentsElement;
    }

    public void save(File file, AtsOperatorGraph operatorGraph) {
        Document document = createDomDocument();

        Element root = document.createElement("xptsml");
        root.setAttribute("version", "0.1.3");

        // compute IDs for the stack to save
        operatorGraph.computeIds();
        Element operatorStack = createOperatorStack(document, operatorGraph);
        root.appendChild(operatorStack);

        Element comments = createComments(document);
        root.appendChild(comments);

        document.appendChild(root);

        writeXmlFile(document, file);

        Point position = ((JViewport) AtsOperatorGraphView.getInstance().getParent()).getViewPosition();
        RecentFiles.getInstance().put(file);
        AtsMenuBar.getInstance().update();
    }

    public Document createDomDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (ParserConfigurationException e) {
        }
        return null;
    }

    public void writeXmlFile(Document doc, File file) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            Result result = // new StreamResult(file);
                    // new StreamResult(new OutputStreamWriter(System.out));
                    new StreamResult(new FileWriter(file));

            // Write the DOM document to the file

            TransformerFactory xformerFactory = TransformerFactory.newInstance();
            xformerFactory.setAttribute("indent-number", new Integer(4));
            Transformer xformer = xformerFactory.newTransformer();

            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "publicId");
            // xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
            // "http://www.polygonize.net/2008/xptsml/xptsml.dtd");
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        } catch (IOException e) {
        }
    }

}
