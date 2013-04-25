package org.pojonode.service;

import org.alfresco.service.namespace.NamespaceException;
import org.alfresco.service.namespace.NamespacePrefixResolver;

import java.util.*;

/**
 * @author Cosmin Marginean, 22/02/12
 */
public class PojonodeNamespacePrefixResolver implements NamespacePrefixResolver {

    private NamespacePrefixResolver delegate;
    private Properties pojonodeNamespaces = new Properties();

    public PojonodeNamespacePrefixResolver() {
        this(null);
    }

    public PojonodeNamespacePrefixResolver(NamespacePrefixResolver delegate) {
        super();
        this.delegate = delegate;
    }

    public void registerNamespace(String prefix, String uri) {
        pojonodeNamespaces.put(prefix, uri);
    }

    public void unregisterNamespace(String prefix) {
        pojonodeNamespaces.remove(prefix);
    }

    public String getNamespaceURI(String prefix) throws NamespaceException {
        String uri = pojonodeNamespaces.getProperty(prefix);
        if ((uri == null) && (delegate != null)) {
            uri = delegate.getNamespaceURI(prefix);
        }
        return uri;
    }

    public Collection<String> getPrefixes(String namespaceURI) throws NamespaceException {
        Collection<String> prefixes = new ArrayList<String>();
        for (String key : pojonodeNamespaces.stringPropertyNames()) {
            String uri = pojonodeNamespaces.getProperty(key);
            if ((uri != null) && (uri.equals(namespaceURI))) {
                prefixes.add(key);
            }
        }
        if (delegate != null) {
            for (String prefix : delegate.getPrefixes(namespaceURI)) {
                if (!pojonodeNamespaces.containsKey(prefix)) {
                    prefixes.add(prefix);
                }
            }
        }
        return prefixes;
    }

    public Collection<String> getPrefixes() {
        Set<String> prefixes = new HashSet<String>();
        if (delegate != null) {
            prefixes.addAll(delegate.getPrefixes());
        }
        prefixes.addAll(pojonodeNamespaces.stringPropertyNames());
        return prefixes;
    }

    public Collection<String> getURIs() {
        Set uris = new HashSet();
        if (delegate != null) {
            uris.addAll(delegate.getURIs());
        }
        uris.addAll(pojonodeNamespaces.values());
        return uris;
    }

    public void registerNamespaces(Properties namespaceMappings) {
        if (namespaceMappings != null) {
            for (String key : namespaceMappings.stringPropertyNames()) {
                registerNamespace(key, namespaceMappings.getProperty(key));
            }
        }
    }

    public boolean isPojonodeNamespace(String namespacePrefix) {
        return pojonodeNamespaces.containsKey(namespacePrefix);
    }

}
