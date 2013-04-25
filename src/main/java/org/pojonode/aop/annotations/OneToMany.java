package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapps to an one-to-many  &lt;association&gt; inside the Alfresco content model. This will render as:
 * <pre>
 *    &lt;source&gt;
 *      ...
 *      &lt;many&gt;false&lt;/many&gt;
 *  &lt;/source&gt;
 *  &lt;target&gt;
 *      ...
 *      &lt;many&gt;true&lt;/many&gt;
 *  &lt;/target&gt;
 *  </pre>
 *
 * @author Cosmin Marginean, Oct 11, 2010
 * @see OneToOne
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {

    Class target();

    String association() default "";

    String title() default "";

    boolean visible() default true;

    boolean sourceMandatory() default false;

    boolean targetMandatory() default false;

    boolean childAssociation() default false;

}
