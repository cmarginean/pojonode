package org.pojonode.codegen.config;

import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Aspect;
import org.pojonode.aop.annotations.Type;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.attributes.AbstractAttributeInfo;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.util.PojonodeConstants;
import org.pojonode.util.Utils;
import org.pojonode.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.OutputStream;

/**
 * @author Cosmin Marginean, Jun 14, 2010
 */
public class AlfrescoShareConfigGenerator extends AbstractAlfrescoConfigGenerator {

    @Override
    public void generateConfig(PojonodeModel pojonodeModel, OutputStream outputStream) throws PojonodeException {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element rootElement = document.createElement("alfresco-config");
            document.appendChild(rootElement);

            for (TypeInfo typeInfo : pojonodeModel.getTypes()) {
                handleTypeInfo(document, typeInfo);
            }

            XmlUtils.serializeDocument(document, outputStream);
        } catch (Exception e) {
            throw new PojonodeException(e.getMessage(), e);
        }
    }

    @Override
    public String getMimeType() {
        return PojonodeConstants.MIMETYPE_XML;
    }

    private void handleTypeInfo(Document document, EntityInfo entityInfo) throws PojonodeException {
        Element cfgElement = document.createElement("config");
        cfgElement.setAttribute("evaluator", entityInfo instanceof TypeInfo ? "node-type" : "aspect-name");
        cfgElement.setAttribute("condition", Utils.getNameFromAnnotation(entityInfo.getPojoClass().getAnnotation(entityInfo instanceof TypeInfo ? Type.class : Aspect.class)));
        Element forms = document.createElement("forms");
        cfgElement.appendChild(forms);
        Element form = document.createElement("form");
        forms.appendChild(form);

        Element fieldVisibility = document.createElement("field-visibility");
        forms.appendChild(fieldVisibility);

        for (PropertyInfo propertyInfo : entityInfo.getProperties()) {
            handleAttribute(fieldVisibility, propertyInfo);
        }
        for (AssociationInfo assocInfo : entityInfo.getAssociations()) {
            handleAttribute(fieldVisibility, assocInfo);
        }
        document.getChildNodes().item(0).appendChild(cfgElement);
    }

    private void handleAttribute(Element propSheetElement, AbstractAttributeInfo propertyInfo) throws PojonodeException {
        if (propertyInfo.isVisible() && !propertyInfo.isInherited()) {
            Element showPropElement = propSheetElement.getOwnerDocument().createElement("show");
            showPropElement.setAttribute("id", propertyInfo.getQname().getPrefixString());
            propSheetElement.appendChild(showPropElement);
        }
    }
}
