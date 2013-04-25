package org.pojonode.model.pojonode;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Property;
import org.pojonode.aop.annotations.Type;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
@Type(name = "cm:cmobject", parent = "sys:base")
public class PojoNode extends PojoBase {

    private NodeRef nodeRef;

    @Property(name = "cm:name")
    private String name;

    //TODO: All of these should be in a proper aspect
    @Property(name = "cm:title")
    private String title;

    @Property(name = "cm:description")
    private String description;

    @Property(name = "cm:created")
    private Date created;

    @Property(name = "cm:modified")
    private Date modified;

    public NodeRef getNodeRef() {
        return nodeRef;
    }

    public <T extends PojoAspect> boolean hasAspect(Class<T> pojoAspectClass) throws PojonodeException {
        return serviceRegistry.getNodeService().hasAspect(nodeRef, pojonodeModelService.getAspect(pojoAspectClass).getType());
    }

    public <T extends PojoAspect> T getAspect(Class<T> pojoAspectClass) throws PojonodeException {
        if (!hasAspect(pojoAspectClass)) {
            return null;
        }
        return pojonodeService.getAspect(this, pojoAspectClass);
    }

    public void addAspect(PojoAspect pojoAspect) throws PojonodeException {
        serviceRegistry.getNodeService().addAspect(this.getNodeRef(), pojonodeModelService.getAspect(pojoAspect.getClass()).getType(), pojoAspect.getProperties());
    }

    protected void setProperty(QName propName, Serializable propValue) {
        serviceRegistry.getNodeService().setProperty(nodeRef, propName, propValue);
    }

    protected Date getDate(QName propName) {
        return (Date) serviceRegistry.getNodeService().getProperty(nodeRef, propName);
    }

    protected String getString(QName propName) {
        return (String) serviceRegistry.getNodeService().getProperty(nodeRef, propName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
