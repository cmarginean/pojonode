package org.pojonode.codegen.revert;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.dictionary.*;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.*;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.AspectInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.model.pojonode.FolderNode;
import org.pojonode.model.pojonode.PojoNode;
import org.pojonode.service.model.PojonodeModelService;
import org.pojonode.util.FormattingUtils;
import org.pojonode.util.PojonodeConstants;
import org.pojonode.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Cosmin Marginean, 11/01/12 (Largely based on contributions from Aaron Lee)
 */
public class AlfrescoModelConverterImpl implements IAlfrescoModelConverter {

    private static final Logger log = Logger.getLogger(AlfrescoModelConverterImpl.class);
    public static final String TEMPLATE_POJONODE = "org/pojonode/codegen/revert/templates/pojo-node.java.ftl";

    private PojonodeModelService pojonodeModelService;
    private ServiceRegistry serviceRegistry;
    private Configuration freemarkerConfig;

    public AlfrescoModelConverterImpl() {
        freemarkerConfig = new Configuration();
        freemarkerConfig.setTemplateLoader(new ClassTemplateLoader(AlfrescoModelConverterImpl.class, "/"));
    }

    @Override
    public void generatePojoModel(String outputDirectory, String basePackage, QName... modelNames) throws PojonodeException {
        Set<QName> qnames = new LinkedHashSet<QName>();
        qnames.addAll(Arrays.asList(modelNames));
        generatePojoModel(outputDirectory, basePackage, qnames);
    }

    @Override
    public void generatePojoSrcJar(OutputStream outputStream, String basePackage, QName... modelNames) throws PojonodeException {
        try {
            String tempDir = Utils.getTempDir();
            if (log.isInfoEnabled()) {
                log.info("Generating src-jar for model. Sources generated in directory: " + tempDir);
            }
            generatePojoModel(tempDir, basePackage, modelNames);
            Utils.jarDirectory(tempDir, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PojonodeException(e);
        }
    }

    @Override
    public void generatePojoModel(String outputDirectory, String basePackage, Set<QName> modelNames) throws PojonodeException {
        try {
            Properties namespaceMappings = getAllNamespaceMappings();
            pojonodeModelService.addNamespaceMappings(namespaceMappings);

            for (QName modelName : modelNames) {
                PojonodeModel pojonodeModel = populateMetadataStore(basePackage, modelName);
                log.info("Generating POJO classes for Types");
                Collection<TypeInfo> types = pojonodeModel.getTypes();
                if (types != null) {
                    for (TypeInfo typeInfo : types) {
                        log.info("Generating Java Class for Type " + typeInfo.getType());
                        renderClass(outputDirectory, basePackage, typeInfo);
                    }
                }

                log.info("Generating POJO classes for Aspects");
                Collection<AspectInfo> aspects = pojonodeModel.getAspects();
                if (aspects != null) {
                    for (AspectInfo aspectInfo : aspects) {
                        log.info("Generating Java Class for Aspect " + aspectInfo.getType());
                        renderClass(outputDirectory, basePackage, aspectInfo);
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void renderClass(String rootOutputDir, String basePackage, EntityInfo entity) throws PojonodeException {
        try {
            Map<String, Object> model = new Hashtable<String, Object>();
            model.put("nowDatetime", FormattingUtils.formatDate(new Date(), FormattingUtils.DATEFMT_TIME));
            model.put("entity", entity);
            model.put("annotation", entity instanceof TypeInfo ? Type.class.getSimpleName() : Aspect.class.getSimpleName());
            Template template = freemarkerConfig.getTemplate(TEMPLATE_POJONODE);
            File javaFile = new File(rootOutputDir, StringUtils.replace(entity.getPojoClassName(), ".", File.separator) + ".java");
            javaFile.getParentFile().mkdirs();
            javaFile.createNewFile();
            Writer output = new FileWriter(javaFile);
            template.process(model, output);
            output.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PojonodeException(e);
        }
    }

    protected PojonodeModel populateMetadataStore(String basePackage, QName modelName) throws PojonodeException {
        PojonodeModel pojonodeModel = new PojonodeModel();
        DictionaryService dictionaryService = serviceRegistry.getDictionaryService();

        Set<QName> modelTypes = new LinkedHashSet<QName>();
        Set<QName> modelAspects = new LinkedHashSet<QName>();
        Collection<QName> models = dictionaryService.getAllModels();
        for (QName model : models) {
            if (modelName.equals(model)) {
                // Add types and aspects from model
                modelTypes.addAll(dictionaryService.getTypes(model));
                modelAspects.addAll(dictionaryService.getAspects(model));
            }
        }

        // Load types
        for (QName type : modelTypes) {
            TypeDefinition typeDef = dictionaryService.getType(type);
            if (typeDef == null) {
                throw new PojonodeException("Could not find TypeDefinition for type " + type);
            }

            TypeInfo typeInfo = new TypeInfo();
            ClassDefinition parentTypeDef = null;
            if (typeDef.getParentName() != null) { // has parent
                parentTypeDef = dictionaryService.getType(typeDef.getParentName());
                if (parentTypeDef == null) {
                    throw new PojonodeException("Could not find (parent) TypeDefinition for type " + type);
                }
            } else { // Default parent
                log.info("No parent type found for " + typeDef.getName() + ", defaulting to " + ContentModel.TYPE_CONTENT);
                parentTypeDef = dictionaryService.getType(ContentModel.TYPE_CONTENT);
            }

            typeInfo.setMandatoryAspectClassNames(getMandatoryAspects(typeDef, basePackage));
            populateEntityMetadata(typeInfo, typeDef, parentTypeDef, basePackage, modelTypes);
            pojonodeModel.addTypeInfo(typeInfo);
            log.info("Added to metadata store the type " + type);
        }

        //Load aspects
        for (QName aspect : modelAspects) {
            AspectDefinition aspectDef = dictionaryService.getAspect(aspect);
            AspectInfo aspectInfo = new AspectInfo();
            AspectDefinition parentAspectDef = aspectDef.getParentName() != null ? dictionaryService.getAspect(aspectDef.getParentName()) : null;
            populateEntityMetadata(aspectInfo, aspectDef, parentAspectDef, basePackage, modelAspects);
            pojonodeModel.addAspectInfo(aspectInfo);
        }

        return pojonodeModel;
    }

    protected Set<String> getMandatoryAspects(TypeDefinition typedef, String basePackage) {
        // Deal only with TypeDefinition-specific properties, specifically, mandatory aspects
        // Return list of all mandatory aspect QNames found so that they are resolved/generated even if not contained by a model being introspected.
        Set<String> mandatoryAspects = new LinkedHashSet<String>();
        Set<QName> defaultAspectNames = typedef.getDefaultAspectNames();
        if (defaultAspectNames != null && defaultAspectNames.size() > 0) {
            for (QName aspect : defaultAspectNames) {
                mandatoryAspects.add(getPojoClassName(basePackage, aspect, true));
            }
        }
        return mandatoryAspects;
    }

    private void populateEntityMetadata(EntityInfo entity, ClassDefinition classDef, ClassDefinition parentClassDef, String basePackage, Set<QName> allModelClasses) throws PojonodeException {
        // TODO: Populate constraints, type indexing properties
        // Essential properties
        entity.setType(classDef.getName());
        entity.setPojoClassName(getPojoClassName(basePackage, classDef.getName(), entity instanceof AspectInfo));
        if (parentClassDef != null) {
            QName parentType = parentClassDef.getName();
            entity.setParentPrefixString(parentType.getPrefixString());
            if (allModelClasses.contains(parentType)) { //this means that a class will be generated for this parent
                entity.setParentPojoClassName(getPojoClassName(basePackage, parentType, entity instanceof AspectInfo));
            } else if (ContentModel.TYPE_CONTENT.equals(parentType)) {
                entity.setParentPojoClassName(PojoNode.class.getName());
            } else if (ContentModel.TYPE_FOLDER.equals(parentType)) {
                entity.setParentPojoClassName(FolderNode.class.getName());
            }
        }

        // Do a set intersection with the parent's properties (if they exist) to exclude inherited properties
        Map<QName, PropertyDefinition> parentProps = parentClassDef != null ? parentClassDef.getProperties() : new Hashtable<QName, PropertyDefinition>();
        Map<QName, PropertyDefinition> typeProps = classDef.getProperties();
        for (QName typePropName : typeProps.keySet()) {
            if (!parentProps.containsKey(typePropName)) {
                PropertyDefinition propertyDef = typeProps.get(typePropName);
                PropertyInfo propInfo = getPropertyInfo(propertyDef);
                entity.addProperty(propInfo);
            }
        }

        //Handle all assocs in the same loop
        Map<QName, AssociationDefinition> parentAsssocs = new Hashtable<QName, AssociationDefinition>();
        if (parentClassDef != null) {
            parentAsssocs.putAll(parentClassDef.getAssociations());
            parentAsssocs.putAll(parentClassDef.getChildAssociations());
        }
        Map<QName, AssociationDefinition> typeAssocs = new Hashtable<QName, AssociationDefinition>();
        typeAssocs.putAll(classDef.getAssociations());
        typeAssocs.putAll(classDef.getChildAssociations());
        for (QName typeAssocName : typeAssocs.keySet()) {
            if (!parentAsssocs.containsKey(typeAssocName)) {
                AssociationDefinition assocDef = typeAssocs.get(typeAssocName);
                AssociationInfo assoc = getAssocInfo(basePackage, assocDef);
                entity.addAssociation(assoc);
            }
        }
    }

    private AssociationInfo getAssocInfo(String basePackage, AssociationDefinition assocDef) {
        AssociationInfo assoc = new AssociationInfo();
        assoc.setQname(assocDef.getName());
        assoc.setTitle(assocDef.getTitle());
        assoc.setVisible(true);

        String collectionMethodBaseName = localNameToCamelCase(assocDef.getName(), true);
        assoc.setAddMethod("add" + collectionMethodBaseName);
        assoc.setRemoveMethod("remove" + collectionMethodBaseName);
        assoc.setPojoProperty(localNameToCamelCase(assocDef.getName(), false));
        assoc.setSourceMandatory(assocDef.isSourceMandatory());
        assoc.setTargetMandatory(assocDef.isTargetMandatory());
        assoc.setTargetClassName(getPojoClassName(basePackage, assocDef.getTargetClass().getName(), false));
        Class<? extends Annotation> annotationClass = null;
        if (assocDef.isSourceMany()) {
            annotationClass = assocDef.isTargetMany() ? ManyToMany.class : ManyToOne.class;
        } else {
            annotationClass = assocDef.isTargetMany() ? OneToMany.class : OneToOne.class;
        }
        assoc.setAnnotationClass(annotationClass);
        assoc.setChildAssociation(assocDef.isChild());
        return assoc;
    }

    private PropertyInfo getPropertyInfo(PropertyDefinition propertyDef) throws PojonodeException {
        PropertyInfo propInfo = new PropertyInfo();
        propInfo.setPojoProperty(localNameToCamelCase(propertyDef.getName(), false));
        //TODO: Handle enum (constraint) + parameterize "primitiveIfAvailable" + Handle multiple (Lists)
        String javaClassName = propertyDef.getDataType().getJavaClassName();
        try {
            Class javaClass = Class.forName(javaClassName);
            if (PojonodeConstants.isWrapperType(javaClass)) { //Try to get the primitive type
                propInfo.setTargetClass(PojonodeConstants.getPrimitiveClass(javaClass));
            } else {
                propInfo.setTargetClass(javaClass);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PojonodeException(e);
        }
        propInfo.setQname(propertyDef.getName());
        propInfo.setTitle(propertyDef.getTitle());
        propInfo.setDefaultValue(propertyDef.getDefaultValue());
        propInfo.setMultiple(propertyDef.isMultiValued());
        propInfo.setVisible(true); // TODO: Find this in the alf dictionary (???)
        propInfo.setMandatory(propertyDef.isMandatory());
        return propInfo;
    }

    private String getPojoClassName(String basePackage, QName type, boolean isAspect) {
        if (PojonodeConstants.BASE_TYPE_TO_POJO_TYPE.containsKey(type)) {
            return PojonodeConstants.BASE_TYPE_TO_POJO_TYPE.get(type).getName();
        }

        String[] nameTokens = type.getLocalName().trim().split("[_-]", 0);

        StringBuilder sb = new StringBuilder();
        sb.append(basePackage)
                .append(".")
                .append(Utils.getPrefix(type.getPrefixString()))
                .append(".")
                .append(isAspect ? "aspects" : "types")
                .append(".");
        for (int i = 0; i < nameTokens.length; i++) {
            sb.append(StringUtils.capitalize(nameTokens[i]));
        }
        return sb.toString();
    }

    private String localNameToCamelCase(QName name, boolean capitalizeFirstLetter) {
        String[] tokens = name.getLocalName().trim().split("[_-]", 0);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            result.append((0 == i && !capitalizeFirstLetter) ? tokens[i] : StringUtils.capitalize(tokens[i]));
        }
        return result.toString();
    }

    private Properties getAllNamespaceMappings() throws PojonodeException {
        Properties namespaceMappings = new Properties();
        NamespaceService namespaceService = serviceRegistry.getNamespaceService();
        for (String uri : namespaceService.getURIs()) {
            Collection<String> prefixes = namespaceService.getPrefixes(uri);
            if (prefixes == null || prefixes.size() == 0) {
                throw new PojonodeException("Could not find prefixes for namespace " + uri);
            }
            String prefix = prefixes.iterator().next(); //Fetch first prefix - not 100% sure if there should be a rule here, but usually there's only one anyways
            namespaceMappings.setProperty(prefix, uri);
        }
        return namespaceMappings;
    }

    public void setPojonodeModelService(PojonodeModelService pojonodeModelService) {
        this.pojonodeModelService = pojonodeModelService;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
}
