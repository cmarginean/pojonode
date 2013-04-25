package org.pojonode.service.core;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Type;
import org.pojonode.aop.intercept.AssocOperationInterceptor;
import org.pojonode.aop.intercept.GetterInterceptor;
import org.pojonode.aop.intercept.SetterInterceptor;
import org.pojonode.aop.matchers.AssocOperationMethodMatcher;
import org.pojonode.aop.matchers.GetterMethodMatcher;
import org.pojonode.aop.matchers.SetterMethodMatcher;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.pojonode.FolderNode;
import org.pojonode.model.pojonode.PojoAspect;
import org.pojonode.model.pojonode.PojoBase;
import org.pojonode.model.pojonode.PojoNode;
import org.pojonode.service.model.PojonodeModelService;
import org.pojonode.util.SearchUtils;
import org.pojonode.util.Utils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Field;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class PojonodeServiceImpl implements IPojonodeService {

    private static final Logger log = Logger.getLogger(PojonodeServiceImpl.class);

    private static ComposablePointcut getterPointcut = new ComposablePointcut(new GetterMethodMatcher());
    private static ComposablePointcut setterPointcut = new ComposablePointcut(new SetterMethodMatcher());

    private GetterInterceptor getterInterceptor;
    private SetterInterceptor setterInterceptor;
    private AssocOperationInterceptor assocOperationInterceptor;
    private PojonodeModelService pojonodeModelService;
    private NodeService nodeService;
    private ServiceRegistry serviceRegistry;

    public <T extends PojoNode> T getNode(String strNodeRef, Class<T> pojoClass) throws PojonodeException {
        NodeRef nodeRef = new NodeRef(strNodeRef);
        return getNode(nodeRef, pojoClass);
    }

    public <T extends PojoNode> T getNode(NodeRef nodeRef, Class<T> pojoClass) throws PojonodeException {
        ProxyFactory factory = null;
        T pojonode = null;
        try {
            pojonode = pojoClass.newInstance();

            Field nodeRefField = PojoNode.class.getDeclaredField("nodeRef");
            nodeRefField.setAccessible(true);
            nodeRefField.set(pojonode, nodeRef);

            setServices(pojonode, pojoClass);
            factory = new ProxyFactory(pojonode);
            addPojoInterceptors(pojoClass, factory);
        } catch (Exception e) {
            throw new PojonodeException(e.getMessage(), e);
        }
        return (T) factory.getProxy();
    }

    private void addPojoInterceptors(Class pojoClass, ProxyFactory factory) throws PojonodeException {
        factory.addAdvisor(new DefaultPointcutAdvisor(getterPointcut, getterInterceptor));
        factory.addAdvisor(new DefaultPointcutAdvisor(setterPointcut, setterInterceptor));
        EntityInfo type = null;
        if (PojoNode.class.isAssignableFrom(pojoClass)) {
            type = pojonodeModelService.getType(pojoClass);
        } else if (PojoAspect.class.isAssignableFrom(pojoClass)) {
            type = pojonodeModelService.getAspect(pojoClass);
        }
        if (null == type) {
            log.error("addPojoInterceptors: Cannot find type info for " + pojoClass);
            //throw new PojonodeException("addPojoInterceptors: Cannot find type info for " + pojoClass);
        }
        ComposablePointcut assocOperationPointcut = new ComposablePointcut(new AssocOperationMethodMatcher(type));
        factory.addAdvisor(new DefaultPointcutAdvisor(assocOperationPointcut, assocOperationInterceptor));
    }

    private void setServices(Object pojo, Class pojonodeClass) throws NoSuchFieldException, IllegalAccessException {
        Field serviceRegistryField = PojoBase.class.getDeclaredField("serviceRegistry");
        serviceRegistryField.setAccessible(true);
        serviceRegistryField.set(pojo, serviceRegistry);

        Field pojonodeServiceField = PojoBase.class.getDeclaredField("pojonodeService");
        pojonodeServiceField.setAccessible(true);
        pojonodeServiceField.set(pojo, this);

        Field metadataStoreField = PojoBase.class.getDeclaredField("pojonodeModelService");
        metadataStoreField.setAccessible(true);
        metadataStoreField.set(pojo, pojonodeModelService);
    }

    public <T extends PojoNode> T getNodeByPath(String path, Class<T> pojoClass) throws PojonodeException {
        NodeRef nodeRef = SearchUtils.findNodeByPath(nodeService, path);
        return nodeRef != null ? getNode(nodeRef, pojoClass) : null;
    }

    public <T extends PojoNode> T createNode(String repositoryPath, Class<T> pojoClass) throws PojonodeException {
        NodeRef parentNode = SearchUtils.findNodeByPath(nodeService, repositoryPath);
        if (parentNode == null) {
            throw new PojonodeException("Could not create node in path: " + repositoryPath + ". Space doesn't exist");
        }
        return createNode(parentNode, pojoClass);
    }

    public <T extends PojoNode> T createNode(NodeRef parentFolder, Class<T> pojoClass) throws PojonodeException {
        if (parentFolder == null) {
            throw new IllegalArgumentException("parentFolder cannot be null");
        }
        String nodeType = pojoClass.getAnnotation(Type.class).name();
        String namespaceUri = pojonodeModelService.getNamespaceUri(Utils.getPrefix(nodeType));
        QName typeQName = pojonodeModelService.getType(pojoClass).getType();
        QName assocQname = QName.createQName(namespaceUri, "contains");//????
        ChildAssociationRef childAssociationRef = nodeService.createNode(parentFolder, ContentModel.ASSOC_CONTAINS, assocQname, typeQName);
        return getNode(childAssociationRef.getChildRef(), pojoClass);
    }

    public <T extends PojoNode> T createNode(FolderNode parentFolder, Class<T> pojoClass) throws PojonodeException {
        return createNode(parentFolder.getNodeRef(), pojoClass);
    }

    public <T extends PojoAspect> T getAspect(PojoNode pojoNode, Class<T> pojoClass) throws PojonodeException {
        ProxyFactory factory = null;
        T aspect = null;
        try {
            aspect = pojoClass.newInstance();
            aspect.setTarget(pojoNode);
            setServices(aspect, pojoClass);
            factory = new ProxyFactory(aspect);
            addPojoInterceptors(pojoClass, factory);
        } catch (Exception e) {
            throw new PojonodeException(e.getMessage(), e);
        }
        return (T) factory.getProxy();
    }

    public void setGetterInterceptor(GetterInterceptor getterInterceptor) {
        this.getterInterceptor = getterInterceptor;
    }

    public void setSetterInterceptor(SetterInterceptor setterInterceptor) {
        this.setterInterceptor = setterInterceptor;
    }

    public void setAssocOperationInterceptor(AssocOperationInterceptor assocOperationInterceptor) {
        this.assocOperationInterceptor = assocOperationInterceptor;
    }

    public void setPojonodeModelService(PojonodeModelService pojonodeModelService) {
        this.pojonodeModelService = pojonodeModelService;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        this.nodeService = serviceRegistry.getNodeService();
    }
}
