package org.pojonode.model.metamodel.attributes;

import org.alfresco.service.namespace.QName;
import org.pojonode.PojonodeException;

/**
 * @author Cosmin Marginean, 6/14/11
 */
public abstract class AbstractAttributeInfo {

    private QName qname;
    private Class targetClass;
    private String targetClassName;
    private String pojoProperty;
    private String title;
    private boolean visible;
    private boolean inherited;

    public String getPojoProperty() {
        return pojoProperty;
    }

    public void setPojoProperty(String pojoProperty) {
        this.pojoProperty = pojoProperty;
    }

    public QName getQname() {
        return qname;
    }

    public void setQname(QName qname) {
        this.qname = qname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public Class getTargetClass() throws PojonodeException {
        if (targetClass == null) {
            try {
                targetClass = Class.forName(targetClassName);
            } catch (ClassNotFoundException e) {
                throw new PojonodeException(e);
            }
        }
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        setTargetClassName(targetClass.getName());
    }

    public boolean isPrimitive() {
        return targetClass != null && targetClass.isPrimitive();
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }
}
