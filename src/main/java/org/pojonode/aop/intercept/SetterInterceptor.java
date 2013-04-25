package org.pojonode.aop.intercept;

import org.alfresco.service.cmr.repository.NodeRef;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.pojonode.PojoNode;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class SetterInterceptor extends PropertyInterceptor {

    private static final Logger log = Logger.getLogger(SetterInterceptor.class);

    protected Object interceptProperty(Class<?> instanceClass, MethodInvocation methodInvocation, EntityInfo thisEntityInfo, PropertyInfo propertyInfo, NodeRef targetNodeRef) throws Throwable {
        String methodName = methodInvocation.getMethod().getName();
        if (propertyInfo != null) {
            nodeService.setProperty(targetNodeRef, propertyInfo.getQname(), (Serializable) methodInvocation.getArguments()[0]);
            return null;
        } else {
            AssociationInfo associationInfo = thisEntityInfo.getSettersToAssociations().get(methodName);
            if (associationInfo != null) {
                //TODO: Remove existing ones first (???). Also add support for add/remove methods
                List<PojoNode> targets = (List<PojoNode>) methodInvocation.getArguments()[0];
                for (PojoNode target : targets) {
                    nodeService.createAssociation(targetNodeRef, target.getNodeRef(), associationInfo.getQname());
                }
                return null;
            } else {
                log.warn("Setter " + methodName + "() for class " + instanceClass + " does not map to a content model property");
                return methodInvocation.proceed();
            }
        }
    }

    protected PropertyInfo findPropertyInfo(MethodInvocation methodInvocation, EntityInfo thisEntityInfo) {
        return thisEntityInfo.getSettersToProperties().get(methodInvocation.getMethod().getName());
    }
}