package org.pojonode.service.model;

import org.alfresco.service.ServiceRegistry;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.model.ModelMappings;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.types.AspectInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.service.PojonodeNamespacePrefixResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * @author Cosmin Marginean, 16/01/12
 */
public class PojonodeModelService implements ApplicationContextAware {

    private static final Logger log = Logger.getLogger(PojonodeModelService.class);

    public static final String BEAN_MODEL_MAPPINGS = "pojonode.modelMappings";

    private ModelMappings modelMappings;
    private PojonodeNamespacePrefixResolver namespacePrefixResolver;
    private Map<String, TypeInfo> classesToTypesCache = new Hashtable<String, TypeInfo>();
    private Map<String, AspectInfo> classesToAspectsCache = new Hashtable<String, AspectInfo>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ApplicationContext applicationContext;

    //    @Override
    protected void loadModelMappings() {
        modelMappings = applicationContext.getBean(ModelMappings.class);
        if (modelMappings != null) {
            namespacePrefixResolver.registerNamespaces(modelMappings.getNamespaceMappings());

            if (modelMappings == null) {
                log.warn("No model mappings found by Pojonode Model Service");
                return;
            }

            log.info("\n\nInitializing Pojonode Model Mapper");
            try {
                //Load base model
                PojonodeModel coreModel = new PojonodeModel();
                coreModel.setBasePackage("org.pojonode.model");
                loadModelClasses(coreModel);

                for (PojonodeModel model : modelMappings.getModels()) {
                    loadModelClasses(model);
                }
            } catch (Exception e) {
                log.error("Could not load Pojonode model mappings", e);
                throw new RuntimeException("Could not load Pojonode model mappings", e);
            }
        } else {
            log.warn("No Model Mappings defined. Pojonode failed to bootstrap");
        }
    }

    private void loadModelClasses(PojonodeModel coreModel) throws PojonodeException {
        coreModel.loadClasses(namespacePrefixResolver);
        loadClassesCache(coreModel);
    }

//    @Override
//    protected void onShutdown(ApplicationEvent applicationEvent) {
//    }

    private void loadClassesCache(PojonodeModel model) throws PojonodeException {
        for (TypeInfo type : model.getTypes()) {
            classesToTypesCache.put(type.getPojoClass().getName(), type);
        }
        for (AspectInfo aspect : model.getAspects()) {
            classesToAspectsCache.put(aspect.getPojoClass().getName(), aspect);
        }
    }

    //TODO: Null checks and warnings for all methods below
    public Properties getNamespaceMappings() {
        return modelMappings.getNamespaceMappings();
    }

    public String getNamespaceUri(String prefix) {
        return modelMappings.getNamespaceUri(prefix);
    }

    public PojonodeModel getModel(String modelName) throws PojonodeException {
        if (modelMappings != null) {
            return modelMappings.getModel(modelName);
        } else {
            return null;
        }
    }

    public void addNamespaceMappings(Properties namespaceMappings) {
        modelMappings.addNamespaceMappings(namespaceMappings);
    }

    public TypeInfo getType(Class cls) throws PojonodeException {
        return classesToTypesCache.get(cls.getName());
    }

    public AspectInfo getAspect(Class cls) throws PojonodeException {
        return classesToAspectsCache.get(cls.getName());
    }

    public void setNamespacePrefixResolver(PojonodeNamespacePrefixResolver namespacePrefixResolver) {
        this.namespacePrefixResolver = namespacePrefixResolver;
    }
}
