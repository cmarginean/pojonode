package org.pojonode.service.model.annotationfilters;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Type;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.model.pojonode.PojoNode;

/**
 * @author Cosmin Marginean, 3/20/11
 */
public class TypeAnnotationFilter extends BaseAnnotationFilter {

    private static final Logger log = Logger.getLogger(TypeAnnotationFilter.class);

    public TypeAnnotationFilter(NamespacePrefixResolver namespacePrefixResolver, PojonodeModel model) {
        super(Type.class, namespacePrefixResolver, model);
    }

    @Override
    protected void loadPojonodeClass(Class<?> cls) throws PojonodeException {
        Type contentType = cls.getAnnotation(Type.class);
        log.debug("Loading annotation type: " + contentType);
        if (contentType != null) {
            if (!PojoNode.class.isAssignableFrom(cls)) {
                throw new RuntimeException("Class " + cls + " is annotated with @" + Type.class.getSimpleName() + ", but is not an instance of " + PojoNode.class);
            }

            TypeInfo typeInfo = (TypeInfo) getTypeInfo(cls);
            typeInfo.setMandatoryAspectClasses(contentType.mandatoryAspects());
            model.addTypeInfo(typeInfo);
        }
    }

    @Override
    protected EntityInfo newEntityMetadata() {
        return new TypeInfo();
    }

    @Override
    protected Class getAnnotationType() {
        return Type.class;
    }
}