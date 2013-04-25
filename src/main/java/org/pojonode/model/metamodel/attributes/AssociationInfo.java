package org.pojonode.model.metamodel.attributes;

import java.lang.annotation.Annotation;


/**
 * @author Cosmin Marginean, 3/20/11
 */
public class AssociationInfo extends AbstractAttributeInfo {

    private boolean sourceMandatory;
    private boolean targetMandatory;
    private Class<? extends Annotation> annotationClass;
    private String addMethod;
    private String removeMethod;
    private boolean childAssociation;

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public boolean isSourceMandatory() {
        return sourceMandatory;
    }

    public void setSourceMandatory(boolean sourceManatory) {
        this.sourceMandatory = sourceManatory;
    }

    public boolean isTargetMandatory() {
        return targetMandatory;
    }

    public void setTargetMandatory(boolean targetMandatory) {
        this.targetMandatory = targetMandatory;
    }

    public String getAddMethod() {
        return addMethod;
    }

    public void setAddMethod(String addMethod) {
        this.addMethod = addMethod;
    }

    public String getRemoveMethod() {
        return removeMethod;
    }

    public void setRemoveMethod(String removeMethod) {
        this.removeMethod = removeMethod;
    }

    public boolean isChildAssociation() {
        return childAssociation;
    }

    public void setChildAssociation(boolean childAssociation) {
        this.childAssociation = childAssociation;
    }
}