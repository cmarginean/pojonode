package org.pojonode.model.metamodel;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.pojonode.model.metamodel.types.AspectInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.service.model.annotationfilters.AspectAnnotationFilter;
import org.pojonode.service.model.annotationfilters.TypeAnnotationFilter;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.*;

/**
 * @author Cosmin Marginean, 16/01/12
 */
public class PojonodeModel {

    private String basePackage;
    private String name;
    private String description;
    private String author;
    private String version;
    private Map<String, TypeInfo> classesToTypes = new Hashtable<String, TypeInfo>();
    private Map<String, AspectInfo> classesToAspects = new Hashtable<String, AspectInfo>();
    private Set<String> constraintNames = new HashSet<String>();

    public PojonodeModel() {
    }

    public PojonodeModel(String basePackage, String name, String description, String author, String version) {
        this.basePackage = basePackage;
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
    }

    public void loadClasses(NamespacePrefixResolver namespacePrefixResolver) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new TypeAnnotationFilter(namespacePrefixResolver, this));
        provider.addIncludeFilter(new AspectAnnotationFilter(namespacePrefixResolver, this));
        provider.setResourceLoader(new PathMatchingResourcePatternResolver(PojonodeModel.class.getClassLoader()));
        provider.findCandidateComponents(basePackage);
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Collection<TypeInfo> getTypes() {
        return classesToTypes.values();
    }

    public void addAspectInfo(AspectInfo aspectInfo) {
        classesToAspects.put(aspectInfo.getPojoClassName(), aspectInfo);
    }

    public void addTypeInfo(TypeInfo typeInfo) {
        classesToTypes.put(typeInfo.getPojoClassName(), typeInfo);
    }

    public Collection<AspectInfo> getAspects() {
        return classesToAspects.values();
    }

    public Set<String> getConstraintNames() {
        return constraintNames;
    }

    public void setConstraintNames(Set<String> constraintNames) {
        this.constraintNames = constraintNames;
    }

    public void addConstraint(String shortName) {
        constraintNames.add(shortName);
    }

    public boolean constraintExists(String shortName) {
        return constraintNames.contains(shortName);
    }

    public TypeInfo getType(String pojoClass) {
        return classesToTypes.get(pojoClass);
    }

    public AspectInfo getAspect(String pojoClass) {
        return classesToAspects.get(pojoClass);
    }

    public TypeInfo getType(Class targetType) {
        return getType(targetType.getName());
    }

    public AspectInfo getAspect(Class targetType) {
        return getAspect(targetType.getName());
    }

    public String getLocalName() {
        return name != null ? name.split("\\:")[1] : null;
    }
}
