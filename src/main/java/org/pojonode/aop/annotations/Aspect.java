package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as being the container definition of an aspect.
 *
 * @author Cosmin Marginean, May 12,, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {

    /**
     * The name of the Alfresco aspect, as defined in the content model XML, including the namespace. For example: <code>mymodel:my_aspect</code>
     */
    String name();

    /**
     * The qualified name of the Alfresco aspect from which this aspect inherits. For example: <code>cm:auditable</code> or <code>mymodel:my_parent_aspect</code>.
     * If not present it will be infered from the class from which the current aspect inherits.
     */
    String parent() default "";
}
