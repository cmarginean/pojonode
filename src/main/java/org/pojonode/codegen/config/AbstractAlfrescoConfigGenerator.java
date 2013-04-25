package org.pojonode.codegen.config;


import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.pojonode.service.PojonodeNamespacePrefixResolver;

/**
 * @author Cosmin Marginean, 6/14/11
 */
//TODO: Do we really need this?
public abstract class AbstractAlfrescoConfigGenerator implements IAlfrescoConfigGenerator {

    private PojonodeNamespacePrefixResolver namespacePrefixResolver;

    public void setNamespacePrefixResolver(PojonodeNamespacePrefixResolver namespacePrefixResolver) {
        this.namespacePrefixResolver = namespacePrefixResolver;
    }

    public PojonodeNamespacePrefixResolver getNamespacePrefixResolver() {
        return namespacePrefixResolver;
    }
}
