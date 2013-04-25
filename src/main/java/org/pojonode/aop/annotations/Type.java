package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as being a Pojonode-managed entity and associates it with a type in your Alfresco content model.
 *
 * @author Cosmin Marginean, Oct 11, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Type {

    /**
     * The name of the Alfresco type, as defined in the content model XML, including the namespace. For example: <code>mymodel:my_type</code>
     */
    String name();

    /**
     * The qualified name of the Alfresco type from which this type inherits. For example: <code>cm:content</code> or <code>mymodel:my_base_type</code>.
     * If not present it will be infered from the class from which the current type inherits.
     */
    String parent() default "";


    /**
     * A list of @Aspect annotated classes representing the aspects to apply to this type
     *
     * @return
     */
    Class[] mandatoryAspects() default {};
}
