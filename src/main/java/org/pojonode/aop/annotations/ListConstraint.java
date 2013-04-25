package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate an enum as being a list constraint in the Alfresco content model. The values in this enum are mapped to &lt;value&gt; elements in the Alfresco XML content model.
 *
 * @author Cosmin Marginean, Oct 11, 2010
 * @see ListConstraintValue
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListConstraint {

    /**
     * The name of the constraint, including namespace prefix: <code>mymodel:my_list_constraint</code>.
     */
    String name();
}
