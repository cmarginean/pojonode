package org.pojonode.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

/**
 * @author Cosmin Marginean, Nov 2, 2010
 */
public class XmlUtils {

    public static void appendTextChild(Element parent, String tagname, String text) {
        if (text == null) {
            text = "";
        }
        Document doc = parent.getOwnerDocument();
        Element childElement = doc.createElement(tagname);
        Text textNode = doc.createTextNode(text);
        childElement.appendChild(textNode);
        parent.appendChild(childElement);
    }

    public static void addNamespaceMapping(Element importsElement, String dictionaryPrefix, String dictionaryUri) {
        Element importElement = importsElement.getOwnerDocument().createElement("import");
        importElement.setAttribute("uri", dictionaryUri);
        importElement.setAttribute("prefix", dictionaryPrefix);
        importsElement.appendChild(importElement);
    }

    public static void addNamespaceElement(Element parent, String dictionaryPrefix, String dictionaryUri) {
        Element namespace = parent.getOwnerDocument().createElement("namespace");
        namespace.setAttribute("uri", dictionaryUri);
        namespace.setAttribute("prefix", dictionaryPrefix);
        parent.appendChild(namespace);
    }

    public static void serializeDocument(Document doc, OutputStream outputStream) throws Exception {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer = tfactory.newTransformer();
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        serializer.transform(new DOMSource(doc), new StreamResult(outputStream));
    }
}
