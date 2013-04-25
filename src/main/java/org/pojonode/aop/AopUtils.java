package org.pojonode.aop;

import org.alfresco.service.cmr.repository.NodeRef;
import org.aopalliance.intercept.MethodInvocation;
import org.pojonode.model.pojonode.PojoAspect;
import org.pojonode.model.pojonode.PojoNode;

/**
 * @author Cosmin Marginean, Oct 22, 2010
 */
public class AopUtils {

    public static NodeRef getNodeRef(MethodInvocation methodInvocation) {
        Object instance = methodInvocation.getThis();
        if (instance instanceof PojoNode) {
            return ((PojoNode) instance).getNodeRef();
        } else if (instance instanceof PojoAspect) {
            return ((PojoAspect) instance).getTarget().getNodeRef();
        } else {
            throw new RuntimeException("Object " + instance + " is not an instance of a " + PojoNode.class);
        }
    }
}
