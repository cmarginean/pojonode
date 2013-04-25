package org.pojonode.codegen.config.install;

import org.alfresco.repo.config.xml.RepoXMLConfigService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.pojonode.codegen.config.AlfrescoWebClientConfigGenerator;
import org.pojonode.codegen.config.IAlfrescoConfigGenerator;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.util.PojonodeConstants;

/**
 * @author Cosmin Marginean, 7/16/11
 */
public class AlfrescoWebClientConfigInstaller extends RepositoryConfigInstaller {

    @Override
    protected String getFullDeployPath(PojonodeModel model) {
        return PojonodeConstants.PATH_WEB_CLIENT_CONFIG_CUSTOM;
    }

    @Override
    protected void afterConfigDeploy(NodeRef nodeRef) {
        getApplicationContext().getBean(RepoXMLConfigService.class).reset();
    }
}
