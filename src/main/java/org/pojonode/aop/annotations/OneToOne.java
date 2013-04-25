package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapps to an one-to-one  &lt;association&gt; inside the Alfresco content model. This will render as:
 * <pre>
 *    &lt;source&gt;
 *      ...
 *      &lt;many&gt;false&lt;/many&gt;
 *  &lt;/source&gt;
 *  &lt;target&gt;
 *      ...
 *      &lt;many&gt;false&lt;/many&gt;
 *  &lt;/target&gt;
 *  </pre>
 *
 * @author Cosmin Marginean, Oct 11, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToOne {

    /**
     * The target class of this association
     */
    Class target();

    /**
     * The full name of this association, including namespace prefix.
     *
     * @see Property#name()
     */
    String association() default "";

    /**
     * @see Property#title()
     */
    String title() default "";

    /**
     * @see Property#visible()
     */
    boolean visible() default true;

    /**
     * Indicates whether to mark source with &lt;mandatory&gt;true&lt;/mandatory&gt; or &lt;mandatory&gt;false&lt;/mandatory&gt;
     */
    boolean sourceMandatory() default false;

    /**
     * Indicates whether to mark target with &lt;mandatory&gt;true&lt;/mandatory&gt; or &lt;mandatory&gt;false&lt;/mandatory&gt;
     */
    boolean targetMandatory() default false;

    /**
     * Indicates whether or not childassociation semantics should be used with this relationship
     */
    boolean childAssociation() default false;

}
