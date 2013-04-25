package org.pojonode.model;

import org.pojonode.PojonodeException;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.util.PojonodeConstants;

import java.util.*;

/**
 * @author Cosmin Marginean, 20/02/12
 */
public class ModelMappings {

    private Properties namespaceMappings = new Properties();
    private Set<PojonodeModel> models = new HashSet<PojonodeModel>();
    private Map<String, PojonodeModel> namesToModels = new Hashtable<String, PojonodeModel>();

    public ModelMappings() {
    }

    public Properties getNamespaceMappings() {
        return namespaceMappings;
    }

    public void setNamespaceMappings(Properties namespaceMappings) {
        this.namespaceMappings = namespaceMappings;
    }

    public Set<PojonodeModel> getModels() {
        return models;
    }

    public void setModels(Set<PojonodeModel> models) throws PojonodeException {
        for (PojonodeModel model : models) {
            addModel(model);
        }
    }

    public void addModel(PojonodeModel model) throws PojonodeException {
        models.add(model);
        namesToModels.put(model.getName(), model);
    }

    public String getNamespaceUri(String prefix) {
        return namespaceMappings.getProperty(prefix);
    }

    public PojonodeModel getModel(String modelName) throws PojonodeException {
        return namesToModels.get(modelName);
    }

    public void addNamespaceMappings(Properties namespaceMappings) {
        namespaceMappings.putAll(namespaceMappings);
    }
}
