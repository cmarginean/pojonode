package org.pojonode.model.metamodel.types;

import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.pojonode.PojonodeException;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.util.Utils;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author Cosmin Marginean, 13/05/2011
 */
public abstract class EntityInfo {

    private QName type;
    private String parentPrefixString;
    private Class pojoClass;
    private String pojoClassName;
    private String parentPojoClassName;
    private Set<PropertyInfo> properties = new HashSet<PropertyInfo>();
    private Set<AssociationInfo> associations = new HashSet<AssociationInfo>();
    private Map<String, PropertyInfo> gettersToProperties = new Hashtable<String, PropertyInfo>();
    private Map<String, PropertyInfo> settersToProperties = new Hashtable<String, PropertyInfo>();
    private Map<String, AssociationInfo> gettersToAssociations = new Hashtable<String, AssociationInfo>();
    private Map<String, AssociationInfo> settersToAssociations = new Hashtable<String, AssociationInfo>();
    private Map<String, AssociationInfo> operationsToAssociations = new Hashtable<String, AssociationInfo>();

    public String getParentPrefixString() {
        return parentPrefixString;
    }

    public void setParentPrefixString(String parentPrefixString) {
        this.parentPrefixString = parentPrefixString;
    }

    public String getParentPojoClassName() {
        return parentPojoClassName;
    }

    public void setParentPojoClassName(String parentPojoClassName) {
        this.parentPojoClassName = parentPojoClassName;
    }

    public QName getType() {
        return type;
    }

    public void setType(QName type) {
        this.type = type;
    }

    public String getPojoClassName() {
        return pojoClassName;
    }

    public void setPojoClassName(String pojoClassName) {
        this.pojoClassName = pojoClassName;
    }

    public Set<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyInfo> properties) {
        this.properties = properties;
    }

    public Set<AssociationInfo> getAssociations() {
        return associations;
    }

    public Map<String, PropertyInfo> getGettersToProperties() {
        return gettersToProperties;
    }

    public Map<String, PropertyInfo> getSettersToProperties() {
        return settersToProperties;
    }

    public Map<String, AssociationInfo> getGettersToAssociations() {
        return gettersToAssociations;
    }

    public Map<String, AssociationInfo> getSettersToAssociations() {
        return settersToAssociations;
    }

    public void addProperty(PropertyInfo propertyInfo) {
        String capitalizedFieldName = StringUtils.capitalize(propertyInfo.getPojoProperty());
        gettersToProperties.put("get" + capitalizedFieldName, propertyInfo);
        settersToProperties.put("set" + capitalizedFieldName, propertyInfo);
        properties.add(propertyInfo);
    }

    public void addAssociation(AssociationInfo associationInfo) {
        String pojoProperty = associationInfo.getPojoProperty();
        String capitalizedFieldName = StringUtils.capitalize(pojoProperty);
        gettersToAssociations.put("get" + capitalizedFieldName, associationInfo);
        settersToAssociations.put("set" + capitalizedFieldName, associationInfo);
        if (!StringUtils.isBlank(associationInfo.getAddMethod())) {
            operationsToAssociations.put(associationInfo.getAddMethod(), associationInfo);
        }
        if (!StringUtils.isBlank(associationInfo.getRemoveMethod())) {
            operationsToAssociations.put(associationInfo.getRemoveMethod(), associationInfo);
        }
        associations.add(associationInfo);
    }

    public AssociationInfo getAssociationForOperation(String methodName) {
        return operationsToAssociations.get(methodName);
    }

    public Set<String> getAllAssocOperations() {
        return operationsToAssociations.keySet();
    }

    public Class getPojoClass() throws PojonodeException {
        if (pojoClass == null) {
            try {
                pojoClass = Class.forName(pojoClassName);
            } catch (ClassNotFoundException e) {
                throw new PojonodeException(e);
            }
        }
        return pojoClass;
    }

    public String getPrefix() {
        return Utils.getPrefix(type.getPrefixString());
    }
}
