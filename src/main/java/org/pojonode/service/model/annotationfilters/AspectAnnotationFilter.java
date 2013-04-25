package org.pojonode.service.model.annotationfilters;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Aspect;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.types.AspectInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.pojonode.PojoAspect;

/**
 * @author Cosmin Marginean, 3/20/11
 */
public class AspectAnnotationFilter extends BaseAnnotationFilter {

    private static final Logger log = Logger.getLogger(AspectAnnotationFilter.class);

    public AspectAnnotationFilter(NamespacePrefixResolver namespacePrefixResolver, PojonodeModel model) {
        super(Aspect.class, namespacePrefixResolver, model);
    }

    @Override
    protected void loadPojonodeClass(Class<?> cls) throws PojonodeException {
        Aspect contentType = cls.getAnnotation(Aspect.class);
        log.debug("Loading annotation aspect: " + contentType);
        if (contentType != null) {
            if (!PojoAspect.class.isAssignableFrom(cls)) {
                throw new RuntimeException("Class " + cls + " is annotated with @" + Aspect.class.getSimpleName() + ", but is not an instance of " + PojoAspect.class);
            }

            AspectInfo aspectInfo = (AspectInfo) getTypeInfo(cls);
            model.addAspectInfo(aspectInfo);
        }
    }

    @Override
    protected EntityInfo newEntityMetadata() {
        return new AspectInfo();
    }

    @Override
    protected Class getAnnotationType() {
        return Aspect.class;
    }
}