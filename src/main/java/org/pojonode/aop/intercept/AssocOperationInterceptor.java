package org.pojonode.aop.intercept;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.pojonode.aop.AopUtils;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.model.pojonode.PojoNode;
import org.pojonode.service.model.PojonodeModelService;

/**
 * @author Cosmin Marginean, 29/04/2011
 */
public class AssocOperationInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(AssocOperationInterceptor.class);

    private PojonodeModelService pojonodeModelService;
    private NodeService nodeService;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String methodName = methodInvocation.getMethod().getName();
        Class<?> instanceClass = methodInvocation.getThis().getClass();
        TypeInfo thisTypeInfo = pojonodeModelService.getType(instanceClass);
        AssociationInfo assoc = thisTypeInfo.getAssociationForOperation(methodName);

        NodeRef nodeRef = AopUtils.getNodeRef(methodInvocation);
        PojoNode child = (PojoNode) methodInvocation.getArguments()[0];
        if (methodName.equals(assoc.getAddMethod())) {
            createAssociation(nodeRef, child, assoc);
        } else if (methodName.equals(assoc.getRemoveMethod())) {
            removeAssociation(nodeRef, child, assoc);
        }
        return methodInvocation.proceed();
    }

    private void removeAssociation(NodeRef nodeRef, PojoNode child, AssociationInfo assoc) {
        if (assoc.isChildAssociation()) {
            nodeService.removeChild(nodeRef, child.getNodeRef());
        } else {
            nodeService.removeAssociation(nodeRef, child.getNodeRef(), assoc.getQname());
        }
    }

    private void createAssociation(NodeRef nodeRef, PojoNode child, AssociationInfo assoc) {
        if (assoc.isChildAssociation()) {
            nodeService.addChild(nodeRef, child.getNodeRef(), assoc.getQname(), assoc.getQname());
        } else {
            nodeService.createAssociation(nodeRef, child.getNodeRef(), assoc.getQname());
        }
    }

    public void setPojonodeModelService(PojonodeModelService pojonodeModelService) {
        this.pojonodeModelService = pojonodeModelService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}