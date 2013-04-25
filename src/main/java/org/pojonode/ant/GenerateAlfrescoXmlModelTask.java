package org.pojonode.ant;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.pojonode.codegen.config.AlfrescoModelXmlGenerator;
import org.pojonode.codegen.config.IAlfrescoConfigGenerator;
import org.pojonode.service.PojonodeNamespacePrefixResolver;

/**
 * @author Cosmin Marginean, 22/02/12
 */
public class GenerateAlfrescoXmlModelTask extends ConfigGeneratorTask {

    @Override
    protected IAlfrescoConfigGenerator createConfigGenerator(PojonodeNamespacePrefixResolver namespacePrefixResolver) {
        AlfrescoModelXmlGenerator alfrescoModelXmlGenerator = new AlfrescoModelXmlGenerator();
        alfrescoModelXmlGenerator.setNamespacePrefixResolver(namespacePrefixResolver);
        return alfrescoModelXmlGenerator;
    }
}
