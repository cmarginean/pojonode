package org.pojonode.model.metamodel.attributes;

/**
 * @author Cosmin Marginean, 3/20/11
 */
public class PropertyInfo extends AbstractAttributeInfo {

    private boolean mandatory;
    private String defaultValue;
    private boolean multiple;

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
