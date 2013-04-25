package org.pojonode.codegen.config.install;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.util.PojonodeConstants;

import java.text.MessageFormat;

/**
 * @author Cosmin Marginean, 7/16/11
 */
public class AlfrescoModelXmlInstaller extends RepositoryConfigInstaller {

    private boolean activate;

    @Override
    protected String getFullDeployPath(PojonodeModel model) {
        return MessageFormat.format(PojonodeConstants.PATH_CUSTOM_MODEL_DEFINITION, PojonodeConstants.PREFIX_POJONODE_MODEL + model.getLocalName() + ".xml");
    }

    @Override
    protected void afterConfigDeploy(NodeRef nodeRef) {
        if (activate) {
            getServiceRegistry().getNodeService().setProperty(nodeRef, ContentModel.PROP_MODEL_ACTIVE, true);
        }
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
