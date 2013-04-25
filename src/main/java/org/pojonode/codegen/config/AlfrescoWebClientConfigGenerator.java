package org.pojonode.codegen.config;

import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Aspect;
import org.pojonode.aop.annotations.Type;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.AspectInfo;
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
 * @author Cosmin Marginean, Nov 2, 2010
 */
public class AlfrescoWebClientConfigGenerator extends AbstractAlfrescoConfigGenerator {

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

            for (AspectInfo aspectInfo : pojonodeModel.getAspects()) {
                handleTypeInfo(document, aspectInfo);
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
        Element crtPropertySheetElement = document.createElement("property-sheet");
        document.getChildNodes().item(0).appendChild(cfgElement);
        for (PropertyInfo propertyInfo : entityInfo.getProperties()) {
            handleProperty(crtPropertySheetElement, propertyInfo);
        }
        for (AssociationInfo assocInfo : entityInfo.getAssociations()) {
            handleAssociation(crtPropertySheetElement, assocInfo);
        }
        cfgElement.appendChild(crtPropertySheetElement);
    }

    private void handleAssociation(Element propSheetElement, AssociationInfo assocInfo) {
        if (assocInfo.isVisible()) {
            Element showPropElement = propSheetElement.getOwnerDocument().createElement("show-association");
            showPropElement.setAttribute("name", assocInfo.getQname().getPrefixString());
            propSheetElement.appendChild(showPropElement);
        }
    }

    private void handleProperty(Element propSheetElement, PropertyInfo propertyInfo) throws PojonodeException {
        if (propertyInfo.isVisible() && !propertyInfo.isInherited()) {
            Element showPropElement = propSheetElement.getOwnerDocument().createElement("show-property");
            showPropElement.setAttribute("name", propertyInfo.getQname().getPrefixString());
            propSheetElement.appendChild(showPropElement);
        }
    }
}
