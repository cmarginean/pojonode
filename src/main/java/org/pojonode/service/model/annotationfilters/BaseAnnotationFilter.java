package org.pojonode.service.model.annotationfilters;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.*;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.util.Utils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Cosmin Marginean, 13/05/2011
 */
//TODO: The logic here should be kept simple and only for filtering purposes. The actual model loading should be moved in a dedicated service
public abstract class BaseAnnotationFilter extends AnnotationTypeFilter {

    private static final Logger log = Logger.getLogger(BaseAnnotationFilter.class);

    protected NamespacePrefixResolver namespacePrefixResolver;
    protected PojonodeModel model;

    //TODO: Refactor this into a Spring-bean
    public BaseAnnotationFilter(Class annotationClass, NamespacePrefixResolver namespacePrefixResolver, PojonodeModel model) {
        super(annotationClass, true);
        this.namespacePrefixResolver = namespacePrefixResolver;
        this.model = model;
    }

    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        try {
            boolean match = super.match(metadataReader, metadataReaderFactory);
            if (match) {
                Class<?> cls = Class.forName(metadataReader.getClassMetadata().getClassName());
                loadPojonodeClass(cls);
            }
            return match;
        } catch (Exception e) {
            log.error("Could not load class " + metadataReader.getClassMetadata().getClassName(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

    protected abstract void loadPojonodeClass(Class<?> cls) throws PojonodeException;

    protected abstract EntityInfo newEntityMetadata();

    protected abstract Class getAnnotationType();

    protected EntityInfo getTypeInfo(Class<?> cls) throws PojonodeException {
        EntityInfo entityInfo = newEntityMetadata();

        Annotation annotation = cls.getAnnotation(getAnnotationType());
        String typeFullName = Utils.getNameFromAnnotation(annotation);
        String typeNamespacePrefix = Utils.getPrefix(typeFullName);
        entityInfo.setPojoClassName(cls.getName());
        entityInfo.setType(QName.createQName(typeFullName, namespacePrefixResolver));

        //TODO: A bit messy here... Null checks and such. Plus, if there is a parent attribute, validation against implementing class (?)
        String parent = Utils.getParentTypeFromAnnotation(annotation);
        if (StringUtils.isBlank(parent)) { //Could not find @Type(parent="") or @Aspect(parent="") - Trying to retrieve it from the super class
            Class<?> superclass = cls.getSuperclass();
            Annotation superClassTypeAnnotation = superclass.getAnnotation(getAnnotationType());
            parent = Utils.getNameFromAnnotation(superClassTypeAnnotation);
        }
        entityInfo.setParentPrefixString(parent);

        try {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Property.class) != null) {
                    handleProperty(entityInfo, typeNamespacePrefix, field, false);
                } else if (field.getAnnotation(ManyToMany.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(ManyToMany.class), false);
                } else if (field.getAnnotation(OneToMany.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(OneToMany.class), false);
                } else if (field.getAnnotation(OneToOne.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(OneToOne.class), false);
                }
            }

            List<Field> inheritedFields = new ArrayList<Field>();
            inheritedFields = Utils.getAllClassFields(inheritedFields, cls.getSuperclass());
            for (Field field : inheritedFields) {
                if (field.getAnnotation(Property.class) != null) {
                    handleProperty(entityInfo, typeNamespacePrefix, field, true);
                } else if (field.getAnnotation(ManyToMany.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(ManyToMany.class), true);
                } else if (field.getAnnotation(OneToMany.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(OneToMany.class), true);
                } else if (field.getAnnotation(OneToOne.class) != null) {
                    handleRelation(entityInfo, typeNamespacePrefix, field, field.getAnnotation(OneToOne.class), true);
                }
            }
            return entityInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PojonodeException(e);
        }
    }

    private void handleRelation(EntityInfo typeInfo, String typeNamespacePrefix, Field field, Annotation assocAnnotation, boolean inherited) throws Exception {
        Class targetType = null;
        String annotatedName = null;
        String title = null;
        boolean visible = false;
        boolean srcMandatory = false;
        boolean targetMandatory = false;
        boolean childAssociation = false;

        //TODO: Right.... no inheritance for annotations so all fun here
        if (assocAnnotation instanceof OneToOne) {
            OneToOne oneToOne = (OneToOne) assocAnnotation;
            targetType = oneToOne.target();
            annotatedName = oneToOne.association();
            title = oneToOne.title();
            visible = oneToOne.visible();
            srcMandatory = oneToOne.sourceMandatory();
            targetMandatory = oneToOne.targetMandatory();
            childAssociation = oneToOne.childAssociation();
        } else if (assocAnnotation instanceof OneToMany) {
            OneToMany oneToMany = (OneToMany) assocAnnotation;
            targetType = oneToMany.target();
            annotatedName = oneToMany.association();
            title = oneToMany.title();
            visible = oneToMany.visible();
            srcMandatory = oneToMany.sourceMandatory();
            targetMandatory = oneToMany.targetMandatory();
            childAssociation = oneToMany.childAssociation();
        } else if (assocAnnotation instanceof ManyToMany) {
            ManyToMany manyToMany = (ManyToMany) assocAnnotation;
            targetType = manyToMany.target();
            annotatedName = manyToMany.association();
            title = manyToMany.title();
            visible = manyToMany.visible();
            srcMandatory = manyToMany.sourceMandatory();
            targetMandatory = manyToMany.targetMandatory();
            childAssociation = manyToMany.childAssociation();
        } else if (assocAnnotation instanceof ManyToOne) {
            ManyToOne manyToOne = (ManyToOne) assocAnnotation;
            targetType = manyToOne.target();
            annotatedName = manyToOne.association();
            title = manyToOne.title();
            visible = manyToOne.visible();
            srcMandatory = manyToOne.sourceMandatory();
            targetMandatory = manyToOne.targetMandatory();
            childAssociation = manyToOne.childAssociation();
        }

        String assocShortName;
        QName propertyQName;
        if (!StringUtils.isEmpty(annotatedName)) {
            assocShortName = annotatedName;
        } else {
            // If there is no property name being set with @Property then assume field name is property name and namespace is same as containing type (class)
            assocShortName = typeNamespacePrefix + ":" + field.getName();
        }
        propertyQName = QName.createQName(assocShortName, namespacePrefixResolver);
        //TODO: For now only field-level annotations are supported - so both getters and setters should be available.
        String propertyTitle = !StringUtils.isBlank(title) ? title : assocShortName;
        AssociationInfo assocInfo = createAssocInfo(typeInfo.getPojoClassName(), field.getName(), assocShortName, propertyQName, targetType, visible, propertyTitle);
        assocInfo.setAnnotationClass(assocAnnotation.getClass());
        assocInfo.setSourceMandatory(srcMandatory);
        assocInfo.setTargetMandatory(targetMandatory);
        assocInfo.setChildAssociation(childAssociation);
        assocInfo.setInherited(inherited);
        typeInfo.addAssociation(assocInfo);
    }

    private AssociationInfo createAssocInfo(String pojoClassName, String fieldName, String propertyShortName, QName propertyQName, Class<?> targetType, boolean propVisible, String propertyTitle) throws Exception {
        AssociationInfo assocInfo = new AssociationInfo();
        assocInfo.setPojoProperty(fieldName);
        assocInfo.setQname(propertyQName);
        assocInfo.setTitle(propertyTitle);
        assocInfo.setTargetClass(targetType);
        assocInfo.setVisible(propVisible);
        Class pojoClass = Class.forName(pojoClassName);
        Method[] allMethods = pojoClass.getMethods();
        for (Method method : allMethods) {
            AssocCreate assocAdd = method.getAnnotation(AssocCreate.class);
            if (assocAdd != null && fieldName.equals(assocAdd.property())) {
                assocInfo.setAddMethod(method.getName());
            }
            AssocRemove assocRemove = method.getAnnotation(AssocRemove.class);
            if (assocRemove != null && fieldName.equals(assocRemove.property())) {
                assocInfo.setRemoveMethod(method.getName());
            }
        }
        log.info("Added AssociationInfo: for field name " + fieldName + " for property " + propertyShortName + " with propertyQName " + propertyQName + " with targetType " + targetType);
        return assocInfo;
    }

    private void handleProperty(EntityInfo typeInfo, String typeNamespacePrefix, Field field, boolean inherited) throws PojonodeException {
        String fieldName = field.getName();
        Property propAnnotation = field.getAnnotation(Property.class);
        QName propertyQName = null;
        String annotatedName = propAnnotation.name();
        String propertyShortName = null;
        if (!StringUtils.isEmpty(annotatedName)) {
            propertyShortName = annotatedName;
        } else {
            // If there is no property name being set with @Property then assume field name is property name and namespace is same as containing type (class)
            propertyShortName = typeNamespacePrefix + ":" + fieldName;
        }
        propertyQName = QName.createQName(propertyShortName, namespacePrefixResolver);
        //Only field-level annotations are supported - so both getters and setters should be available.
        String propertyTitle = !StringUtils.isBlank(propAnnotation.title()) ? propAnnotation.title() : propertyShortName;
        PropertyInfo propertyInfo = createProperty(propAnnotation, fieldName, propertyQName, field.getType(), propAnnotation.visible(), propertyTitle);
        propertyInfo.setMandatory(propAnnotation.mandatory());
        propertyInfo.setInherited(inherited);
        typeInfo.addProperty(propertyInfo);
    }

    private PropertyInfo createProperty(Property propAnnotation, String fieldName, QName propertyQName, Class<?> propertyType, boolean propVisible, String propertyTitle) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setPojoProperty(fieldName);
        propertyInfo.setQname(propertyQName);
        propertyInfo.setTitle(propertyTitle);
        boolean multiple = Collection.class.isAssignableFrom(propertyType);
        propertyInfo.setTargetClass(multiple ? propAnnotation.target() : propertyType);
        propertyInfo.setVisible(propVisible);
        propertyInfo.setMultiple(multiple);
        return propertyInfo;
    }
}
