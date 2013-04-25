package org.pojonode.aop.matchers;

import org.pojonode.model.metamodel.types.EntityInfo;
import org.springframework.aop.support.StaticMethodMatcher;

import java.lang.reflect.Method;

/**
 * @author Cosmin Marginean, 29/04/2011
 */
public class AssocOperationMethodMatcher extends StaticMethodMatcher {

    private EntityInfo type;

    public AssocOperationMethodMatcher(EntityInfo type) {
        this.type = type;
    }

    public boolean matches(Method method, Class<?> aClass) {
        String methodName = method.getName();
        return (type.getAllAssocOperations().contains(methodName));
    }
}
