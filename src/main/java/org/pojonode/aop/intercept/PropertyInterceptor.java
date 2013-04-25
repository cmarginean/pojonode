package org.pojonode.aop.intercept;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.pojonode.aop.AopUtils;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.service.core.IPojonodeService;
import org.pojonode.service.model.PojonodeModelService;

/**
 * @author Cosmin Marginean, 13/05/2011
 */
public abstract class PropertyInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(PropertyInterceptor.class);

    protected IPojonodeService pojonodeService;
    protected PojonodeModelService pojonodeModelService;
    protected NodeService nodeService;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class<?> instanceClass = methodInvocation.getThis().getClass();
        EntityInfo thisEntityInfo = pojonodeModelService.getType(instanceClass);
        if (thisEntityInfo == null) {
            thisEntityInfo = pojonodeModelService.getAspect(instanceClass);
        }

        PropertyInfo propertyInfo = findPropertyInfo(methodInvocation, thisEntityInfo);
        NodeRef nodeRef = AopUtils.getNodeRef(methodInvocation);
        return interceptProperty(instanceClass, methodInvocation, thisEntityInfo, propertyInfo, nodeRef);
    }

    protected abstract PropertyInfo findPropertyInfo(MethodInvocation methodInvocation, EntityInfo thisEntityInfo);

    protected abstract Object interceptProperty(Class<?> instanceClass, MethodInvocation methodInvocation, EntityInfo thisEntityInfo, PropertyInfo propertyInfo, NodeRef nodeRef) throws Throwable;

    public void setPojonodeService(IPojonodeService pojonodeService) {
        this.pojonodeService = pojonodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setPojonodeModelService(PojonodeModelService pojonodeModelService) {
        this.pojonodeModelService = pojonodeModelService;
    }
}
