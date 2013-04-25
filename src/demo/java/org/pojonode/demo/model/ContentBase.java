package org.pojonode.demo.model;

import org.pojonode.aop.annotations.Property;
import org.pojonode.aop.annotations.Type;
import org.pojonode.model.pojonode.ContentNode;

/**
 * @author Cosmin Marginean, 3/20/11
 */
@Type(name = "pnd:customContentBase", parent = "cm:content")
public abstract class ContentBase extends ContentNode {

    @Property(title = "Department ID")
    private String departmentId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
