package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapps to a many-to-one  &lt;association&gt; inside the Alfresco content model. It is not clear that this makes
 * any sense, but this relationship is present and defined in the core alfresco data model, and this annotation is therefore
 * required to ensure that code generated from the core types can be compiled.
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
 * @author Aaron M. Lee
 * @see OneToOne
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {

    Class target();

    String association() default "";

    String title() default "";

    boolean visible() default true;

    boolean sourceMandatory() default false;

    boolean targetMandatory() default false;

    boolean childAssociation() default false;

}
