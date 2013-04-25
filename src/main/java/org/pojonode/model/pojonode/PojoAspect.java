package org.pojonode.model.pojonode;

import org.alfresco.service.namespace.QName;
import org.pojonode.PojonodeException;
import org.pojonode.model.metamodel.attributes.PropertyInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Cosmin Marginean, 12/05/2011
 */
public abstract class PojoAspect extends PojoBase {

    private PojoNode target;

    public PojoNode getTarget() {
        return target;
    }

    public void setTarget(PojoNode target) {
        this.target = target;
    }

    public Map<QName, Serializable> getProperties() throws PojonodeException {
        Map<QName, Serializable> result = new HashMap<QName, Serializable>();
        Set<PropertyInfo> properties = pojonodeModelService.getAspect(this.getClass()).getProperties();
        for (PropertyInfo propertyInfo : properties) {
            Serializable propValue = serviceRegistry.getNodeService().getProperty(target.getNodeRef(), propertyInfo.getQname());
            result.put(propertyInfo.getQname(), propValue);
        }
        return result;
    }
}
