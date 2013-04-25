package org.pojonode.codegen.config.install;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.codegen.config.IAlfrescoConfigGenerator;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.service.model.PojonodeModelService;
import org.pojonode.util.SearchUtils;
import org.pojonode.util.Utils;
import org.springframework.context.ApplicationEvent;
import org.springframework.extensions.surf.util.AbstractLifecycleBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * @author Cosmin Marginean, 6/14/11
 */
public abstract class RepositoryConfigInstaller extends AbstractLifecycleBean {

    private static final Logger log = Logger.getLogger(RepositoryConfigInstaller.class);

    private boolean overwrite;
    private Set<String> models;
    private PojonodeModelService pojonodeModelService;
    private ServiceRegistry serviceRegistry;
    private IAlfrescoConfigGenerator configGenerator;

    protected abstract String getFullDeployPath(PojonodeModel model);

    protected abstract void afterConfigDeploy(NodeRef nodeRef);

    @Override
    protected void onBootstrap(ApplicationEvent applicationEvent) {
        serviceRegistry = getApplicationContext().getBean(ServiceRegistry.class);

        for (final String modelName : models) {
            try {
                AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Object>() {
                    @Override
                    public Object doWork() throws Exception {
                        deploy(modelName);
                        return null;
                    }
                }, AuthenticationUtil.SYSTEM_USER_NAME);
            } catch (Exception e) {
                log.fatal(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void onShutdown(ApplicationEvent applicationEvent) {
    }

    //TODO: Review overwrite logic
    public NodeRef deploy(String modelName) throws PojonodeException {
        PojonodeModel model = pojonodeModelService.getModel(modelName);
        if (model != null) {
            String fullDeployPath = getFullDeployPath(model);
            int lastSlash = fullDeployPath.lastIndexOf('/');
            String deployPath = fullDeployPath.substring(0, lastSlash);
            String deployName = fullDeployPath.substring(lastSlash + 1);
            NodeRef configNode = SearchUtils.findNodeByPath(serviceRegistry.getNodeService(), fullDeployPath);
            if (configNode == null || overwrite) {
                ByteArrayOutputStream output = null;
                ByteArrayInputStream input = null;

                try {
                    output = new ByteArrayOutputStream();
                    configGenerator.generateConfig(model, output);
                    input = new ByteArrayInputStream(output.toByteArray());
                    configNode = Utils.storeContentInPath(serviceRegistry, deployPath, deployName, configGenerator.getMimeType(), input);
                    afterConfigDeploy(configNode);
                } catch (PojonodeException e) {
                    log.error(e.getMessage(), e);
                    throw new PojonodeException(e);
                } finally {
                    IOUtils.closeQuietly(output);
                    IOUtils.closeQuietly(input);
                }
            }
            return configNode;
        } else {
            log.warn("Config Deployer could not run. Model mapping for " + modelName + " could not be found");
            return null;
        }
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setConfigGenerator(IAlfrescoConfigGenerator configGenerator) {
        this.configGenerator = configGenerator;
    }

    public void setModelsToInstall(Set<String> modelsToInstall) {
        this.models = modelsToInstall;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setPojonodeModelService(PojonodeModelService pojonodeModelService) {
        this.pojonodeModelService = pojonodeModelService;
    }
}

