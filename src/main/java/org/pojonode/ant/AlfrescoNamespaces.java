package org.pojonode.ant;

import org.alfresco.service.namespace.NamespaceException;
import org.alfresco.service.namespace.NamespacePrefixResolver;

import java.util.*;

/**
 * @author Cosmin Marginean, 17/03/12
 */
public class AlfrescoNamespaces implements NamespacePrefixResolver {

    private Properties alfrescoNamespaces = new Properties();

    public AlfrescoNamespaces() {
        alfrescoNamespaces.setProperty("", "");
        alfrescoNamespaces.setProperty("nt", "http://www.jcp.org/jcr/nt/1.0");
        alfrescoNamespaces.setProperty("gd", "http://www.alfresco.org/model/googledocs/1.0");
        alfrescoNamespaces.setProperty("twitter", "http://www.alfresco.org/model/publishing/twitter/1.0");
        alfrescoNamespaces.setProperty("fm", "http://www.alfresco.org/model/forum/1.0");
        alfrescoNamespaces.setProperty("facebook", "http://www.alfresco.org/model/publishing/facebook/1.0");
        alfrescoNamespaces.setProperty("wcm", "http://www.alfresco.org/model/wcmmodel/1.0");
        alfrescoNamespaces.setProperty("youtube", "http://www.alfresco.org/model/publishing/youtube/1.0");
        alfrescoNamespaces.setProperty("sv", "http://www.jcp.org/jcr/sv/1.0");
        alfrescoNamespaces.setProperty("st", "http://www.alfresco.org/model/site/1.0");
        alfrescoNamespaces.setProperty("custom", "custom.model");
        alfrescoNamespaces.setProperty("exif", "http://www.alfresco.org/model/exif/1.0");
        alfrescoNamespaces.setProperty("alfcmis", "http://www.alfresco.org/model/cmis/1.0/alfcmis");
        alfrescoNamespaces.setProperty("xml", "http://www.w3.org/XML/1998/namespace");
        alfrescoNamespaces.setProperty("pub", "http://www.alfresco.org/model/publishing/1.0");
        alfrescoNamespaces.setProperty("wca", "http://www.alfresco.org/model/wcmappmodel/1.0");
        alfrescoNamespaces.setProperty("inwf", "http://www.alfresco.org/model/workflow/invite/nominated/1.0");
        alfrescoNamespaces.setProperty("flickr", "http://www.alfresco.org/model/publishing/flickr/1.0");
        alfrescoNamespaces.setProperty("cm", "http://www.alfresco.org/model/content/1.0");
        alfrescoNamespaces.setProperty("imap", "http://www.alfresco.org/model/imap/1.0");
        alfrescoNamespaces.setProperty("stcp", "http://www.alfresco.org/model/sitecustomproperty/1.0");
        alfrescoNamespaces.setProperty("ver2", "http://www.alfresco.org/model/versionstore/2.0");
        alfrescoNamespaces.setProperty("view", "http://www.alfresco.org/view/repository/1.0");
        alfrescoNamespaces.setProperty("imwf", "http://www.alfresco.org/model/workflow/invite/moderated/1.0");
        alfrescoNamespaces.setProperty("cmisext", "http://www.alfresco.org/model/cmis/1.0/cs01ext");
        alfrescoNamespaces.setProperty("wf", "http://www.alfresco.org/model/workflow/1.0");
        alfrescoNamespaces.setProperty("trx", "http://www.alfresco.org/model/transfer/1.0");
        alfrescoNamespaces.setProperty("rn", "http://www.alfresco.org/model/rendition/1.0");
        alfrescoNamespaces.setProperty("slideshare", "http://www.alfresco.org/model/publishing/slideshare/1.0");
        alfrescoNamespaces.setProperty("sys", "http://www.alfresco.org/model/system/1.0");
        alfrescoNamespaces.setProperty("lnk", "http://www.alfresco.org/model/linksmodel/1.0");
        alfrescoNamespaces.setProperty("webdav", "http://www.alfresco.org/model/webdav/1.0");
        alfrescoNamespaces.setProperty("pubwf", "http://www.alfresco.org/model/publishingworkflow/1.0");
        alfrescoNamespaces.setProperty("ver", "http://www.alfresco.org/model/versionstore/1.0");
        alfrescoNamespaces.setProperty("cmiscustom", "http://www.alfresco.org/model/cmis/custom");
        alfrescoNamespaces.setProperty("jcr", "http://www.jcp.org/jcr/1.0");
        alfrescoNamespaces.setProperty("emailserver", "http://www.alfresco.org/model/emailserver/1.0");
        alfrescoNamespaces.setProperty("ia", "http://www.alfresco.org/model/calendar");
        alfrescoNamespaces.setProperty("rule", "http://www.alfresco.org/model/rule/1.0");
        alfrescoNamespaces.setProperty("dl", "http://www.alfresco.org/model/datalist/1.0");
        alfrescoNamespaces.setProperty("usr", "http://www.alfresco.org/model/user/1.0");
        alfrescoNamespaces.setProperty("app", "http://www.alfresco.org/model/application/1.0");
        alfrescoNamespaces.setProperty("d", "http://www.alfresco.org/model/dictionary/1.0");
        alfrescoNamespaces.setProperty("module", "http://www.alfresco.org/system/modules/1.0");
        alfrescoNamespaces.setProperty("audio", "http://www.alfresco.org/model/audio/1.0");
        alfrescoNamespaces.setProperty("blg", "http://www.alfresco.org/model/blogintegration/1.0");
        alfrescoNamespaces.setProperty("linkedin", "http://www.alfresco.org/model/publishing/linkedin/1.0");
        alfrescoNamespaces.setProperty("alf", "http://www.alfresco.org");
        alfrescoNamespaces.setProperty("cmis", "http://www.alfresco.org/model/cmis/1.0/cs01");
        alfrescoNamespaces.setProperty("mix", "http://www.jcp.org/jcr/mix/1.0");
        alfrescoNamespaces.setProperty("bpm", "http://www.alfresco.org/model/bpm/1.0");
        alfrescoNamespaces.setProperty("reg", "http://www.alfresco.org/system/registry/1.0");
        alfrescoNamespaces.setProperty("wcmwf", "http://www.alfresco.org/model/wcmworkflow/1.0");
        alfrescoNamespaces.setProperty("act", "http://www.alfresco.org/model/action/1.0");
    }

    @Override
    public String getNamespaceURI(String prefix) throws NamespaceException {
        return alfrescoNamespaces.getProperty(prefix);
    }

    @Override
    public Collection<String> getPrefixes(String namespaceURI) throws NamespaceException {
        Collection<String> prefixes = new ArrayList<String>();
        for (String key : alfrescoNamespaces.stringPropertyNames()) {
            String uri = alfrescoNamespaces.getProperty(key);
            if ((uri != null) && (uri.equals(namespaceURI))) {
                prefixes.add(key);
            }
        }
        return prefixes;
    }

    @Override
    public Collection<String> getPrefixes() {
        return alfrescoNamespaces.stringPropertyNames();
    }

    @Override
    public Collection<String> getURIs() {
        Set uris = new HashSet();
        uris.addAll(alfrescoNamespaces.values());
        return uris;
    }
}
