package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate a method that will be used to create an association with the type defined by the class defining the method as source..
 * The method should have exactly one parameter of the type representing the target of the association.
 *
 * @author Cosmin Marginean, 29/04/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssocCreate {

    String property();
}
