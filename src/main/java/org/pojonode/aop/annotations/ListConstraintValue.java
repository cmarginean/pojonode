package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Value to render inside the &lt;value&gt; in the constraint definition. When missing, the value will be the one defined in the enum.
 *
 * @author Cosmin Marginean, Oct 11, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListConstraintValue {

    /**
     * The Alfresco constraint value mapped to this enum value.
     */
    String value();
}
