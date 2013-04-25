package org.pojonode.aop.matchers;

import org.springframework.aop.support.StaticMethodMatcher;

import java.lang.reflect.Method;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class GetterMethodMatcher extends StaticMethodMatcher {

    public boolean matches(Method method, Class<?> aClass) {
        return (method.getName().startsWith("get"));
    }
}
