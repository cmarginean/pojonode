package org.pojonode.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Matches a Pojo field with an Alfresco content model property. This maps to a &lt;property&gt; element inside the &lt;properties&gt; section of a an Alfresco type/aspect definition.
 *
 * @author Cosmin Marginean, Oct 11, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {

    /**
     * The name of the Alfresco property mapped to this field. For example <code>mymodel:field</code>. It is optional and when missing the field considered to be the Alfresco cm property.
     * The namespace prefix is also optional and when missing is determined from @Type annotation on the containing class..
     */
    String name() default "";

    /**
     * The title of this property, mapped to the &lt;title&gt; element of the property in the content model XML. It is optional and the property name is used when missing.
     */
    String title() default "";

    /**
     * Used to indicate whether this property will be rendered in a Web Client Config file, so it can be listed in the JSF client on the details page. Defaults to true;
     */
    boolean visible() default true;

    /**
     * Indicates whether this property is mandatory or not
     */
    boolean mandatory() default false;

    /**
     * Default value for this property (As a string value)
     */
    String defaultValue() default "";

    /**
     * Required for multi-valued properties
     */
    Class target() default Class.class; //Ahem... What?
}
