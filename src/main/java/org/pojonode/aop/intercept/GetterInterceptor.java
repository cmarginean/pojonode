package org.pojonode.aop.intercept;

import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.pojonode.PojoNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class GetterInterceptor extends PropertyInterceptor {

    private static final Logger log = Logger.getLogger(GetterInterceptor.class);

    @Override
    protected Object interceptProperty(Class<?> instanceClass, MethodInvocation methodInvocation, EntityInfo thisEntityInfo, PropertyInfo propertyInfo, NodeRef nodeRef) throws Throwable {
        String methodName = methodInvocation.getMethod().getName();
        if (thisEntityInfo.getGettersToProperties().get(methodName) != null) {
            return getProperty(propertyInfo, nodeRef);
        } else {
            AssociationInfo associationInfo = thisEntityInfo.getGettersToAssociations().get(methodName);
            if (associationInfo != null) {
                List<PojoNode> result = new LinkedList<PojoNode>();
                if (associationInfo.isChildAssociation()) {
                    List<ChildAssociationRef> children = nodeService.getChildAssocs(nodeRef, associationInfo.getQname(), RegexQNamePattern.MATCH_ALL, true);
                    for (ChildAssociationRef childRef : children) {
                        PojoNode targetNode = pojonodeService.getNode(childRef.getChildRef(), associationInfo.getTargetClass());
                        result.add(targetNode);
                    }
                } else {
                    List<AssociationRef> targetAssocs = nodeService.getTargetAssocs(nodeRef, associationInfo.getQname());
                    for (AssociationRef assocRef : targetAssocs) {
                        PojoNode targetNode = pojonodeService.getNode(assocRef.getTargetRef(), associationInfo.getTargetClass());
                        result.add(targetNode);
                    }
                }
                return result;
            } else {
                log.warn("Getter " + methodName + "() for class " + instanceClass + " does not map to a content model property");
                return methodInvocation.proceed();
            }
        }
    }

    @Override
    protected PropertyInfo findPropertyInfo(MethodInvocation methodInvocation, EntityInfo thisEntityInfo) {
        return thisEntityInfo.getGettersToProperties().get(methodInvocation.getMethod().getName());
    }

    private Object getProperty(PropertyInfo propertyInfo, NodeRef nodeRef) throws PojonodeException {
        if (propertyInfo.getTargetClass().isEnum()) {
            String value = (String) nodeService.getProperty(nodeRef, propertyInfo.getQname());
            return Enum.valueOf(propertyInfo.getTargetClass(), value);
        } else {
            return nodeService.getProperty(nodeRef, propertyInfo.getQname());
        }
    }
}
