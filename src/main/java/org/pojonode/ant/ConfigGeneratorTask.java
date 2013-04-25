package org.pojonode.ant;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.pojonode.codegen.config.IAlfrescoConfigGenerator;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.service.PojonodeNamespacePrefixResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author Cosmin Marginean, 23/02/12
 */
public abstract class ConfigGeneratorTask extends Task {

    private String basePackage;
    private String name;
    private String description;
    private String author;
    private String version;
    private Properties namespaceMappings = new Properties();
    private File output;

    @Override
    public void execute() throws BuildException {
        FileOutputStream outputStream = null;
        try {
            System.out.println("Using Namespace Mappings: " + namespaceMappings);
            output.getParentFile().mkdirs();
            output.createNewFile();
            outputStream = new FileOutputStream(output);

            PojonodeNamespacePrefixResolver namespacePrefixResolver = new PojonodeNamespacePrefixResolver(new AlfrescoNamespaces());
            namespacePrefixResolver.registerNamespaces(namespaceMappings);

            PojonodeModel model = new PojonodeModel(basePackage, name, description, author, version);
            model.loadClasses(namespacePrefixResolver);

            IAlfrescoConfigGenerator alfrescoConfigGenerator = createConfigGenerator(namespacePrefixResolver);
            alfrescoConfigGenerator.generateConfig(model, outputStream);
        } catch (Exception e) {
            throw new BuildException("Could not generate Alfresco XML Model for " + name, e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    protected abstract IAlfrescoConfigGenerator createConfigGenerator(PojonodeNamespacePrefixResolver namespacePrefixResolver);

    public void addConfiguredNamespace(NamespaceMapping namespaceMapping) {
        namespaceMappings.setProperty(namespaceMapping.getPrefix(), namespaceMapping.getUrl());
    }

    public static class NamespaceMapping {
        private String prefix;
        private String url;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setOutput(File output) {
        this.output = output;
    }

}
