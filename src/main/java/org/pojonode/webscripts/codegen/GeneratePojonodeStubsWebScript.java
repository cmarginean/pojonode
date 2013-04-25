package org.pojonode.webscripts.codegen;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.pojonode.codegen.revert.IAlfrescoModelConverter;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Cosmin Marginean, 16/01/12
 */
public class GeneratePojonodeStubsWebScript extends AbstractWebScript {

    private static final Logger log = Logger.getLogger(GeneratePojonodeStubsWebScript.class);

    private ServiceRegistry serviceRegistry;
    private IAlfrescoModelConverter alfrescoModelConverter;

    @Override
    public void execute(WebScriptRequest webScriptRequest, WebScriptResponse webScriptResponse) throws IOException {
        try {
            String basePackage = webScriptRequest.getParameter("basePackage");
            String modelName = webScriptRequest.getParameter("modelName");
            QName modelQName = QName.createQName(modelName, serviceRegistry.getNamespaceService());
            String jarname = basePackage + "-src.jar";
            webScriptResponse.setHeader("Content-disposition", "attachment; filename=\"" + jarname + "\"");
            alfrescoModelConverter.generatePojoSrcJar(webScriptResponse.getOutputStream(), basePackage, modelQName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace(new PrintWriter(webScriptResponse.getOutputStream()));
        }
    }

    public void setAlfrescoModelConverter(IAlfrescoModelConverter alfrescoModelConverter) {
        this.alfrescoModelConverter = alfrescoModelConverter;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
}
