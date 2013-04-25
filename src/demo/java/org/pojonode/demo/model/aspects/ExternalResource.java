package org.pojonode.demo.model.aspects;

import org.pojonode.aop.annotations.Aspect;
import org.pojonode.aop.annotations.Property;
import org.pojonode.model.pojonode.PojoAspect;

/**
 * @author Cosmin Marginean, 13/05/2011
 */
@Aspect(name = "pnd:externalResource")
public class ExternalResource extends PojoAspect {

    @Property(defaultValue = "NOT_SET")
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
