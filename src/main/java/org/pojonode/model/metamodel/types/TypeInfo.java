package org.pojonode.model.metamodel.types;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Cosmin Marginean, 3/16/11
 */
public class TypeInfo extends EntityInfo {

    private Set<Class> mandatoryAspectClasses = new LinkedHashSet<Class>();
    private Set<String> mandatoryAspectClassNames = new LinkedHashSet<String>();

    public Set<String> getMandatoryAspectClassNames() {
        return mandatoryAspectClassNames;
    }

    public void setMandatoryAspectClassNames(Set<String> mandatoryAspectClassNames) {
        this.mandatoryAspectClassNames = mandatoryAspectClassNames;
    }

    public void setMandatoryAspectClasses(Class[] mandatoryAspectClasses) {
        this.mandatoryAspectClasses.clear();
        this.mandatoryAspectClassNames.clear();
        if (mandatoryAspectClasses != null) {
            for (Class aspectClass : mandatoryAspectClasses) {
                this.mandatoryAspectClasses.add(aspectClass);
                this.mandatoryAspectClassNames.add(aspectClass.getName());
            }
        }
    }
}
