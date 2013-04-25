package org.pojonode.util;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class SearchUtils {

    private static final Logger log = Logger.getLogger(SearchUtils.class);

    public static final StoreRef WORKSPACE_STORE_REF = new StoreRef("workspace://SpacesStore");

    public static final String QUERY_COMPANY_HOME = "+PATH:\"/app:company_home\"";
    public static final String QUERY_DATA_DICTIONARY_MODELS = "PATH:\"/app:company_home/app:dictionary/app:models\"";
    public static final String QUERY_DATA_DICTIONARY_WEB_CLIENT_CONFIG = "PATH:\"/app:company_home/app:dictionary/app:webclient_extension/cm:web-client-config-custom.xml\"";

    public static final String PARAMQUERY_DATA_DICTIONARY_MODEL = "PATH:\"/app:company_home/app:dictionary/app:models/cm:{0:escape}\"";

    public static NodeRef findNode(SearchService searchService, String luceneQuery) {
        List<NodeRef> list = findNodes(searchService, luceneQuery, false);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }

    public static List<NodeRef> findNodes(SearchService searchService, String luceneQuery, boolean sorted) {
        ResultSet results = null;
        try {
            SearchParameters sp = new SearchParameters();
            sp.addStore(WORKSPACE_STORE_REF);
            sp.setLanguage(SearchService.LANGUAGE_LUCENE);
            sp.setQuery(luceneQuery);
            if (sorted) {
                sp.addSort("@" + ContentModel.PROP_NAME.toString(), true);
            }
            results = searchService.query(sp);
            if (results.length() == 0) {
                return null;
            }
            return results.getNodeRefs();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (results != null) {
                results.close();
            }
        }
    }

    public static NodeRef findNodeByPath(NodeService nodeService, String path) throws PojonodeException {
        if (!path.startsWith("/")) {
            throw new PojonodeException("Path '" + path + "' must start with a / (slash)");
        }

        if (path.endsWith("/")) {
            StringUtils.removeEnd(path, "/");
        }

        path = StringUtils.removeStart(path, "/");

        String firstChild = null;
        if (!path.contains("/")) {
            firstChild = path;
        } else {
            firstChild = path.substring(0, path.indexOf('/'));
        }
        //TODO: For some reason we cannot retrieve children with ContentModel.ASSOC_CHILDREN for the store root node. Could use some investigation
        NodeRef rootNode = nodeService.getRootNode(Utils.getSpacesStore());
        List<ChildAssociationRef> associationRefs = nodeService.getChildAssocs(rootNode);
        for (ChildAssociationRef assocRef : associationRefs) {
            NodeRef child = assocRef.getChildRef();
            String nodeName = (String) nodeService.getProperty(child, ContentModel.PROP_NAME);
            if (firstChild.equals(nodeName)) {
                return path.contains("/") ? findChildByPath(nodeService, child, path.substring(path.indexOf('/') + 1)) : child;
            }
        }
        return null;
    }

    public static NodeRef findChildByPath(NodeService nodeService, NodeRef parent, String path) {
        NodeRef result = null;
        StringTokenizer t = new StringTokenizer(path, "/");
        if (t.hasMoreTokens()) {
            List<String> names = new ArrayList<String>(1);
            names.add(null);
            result = parent;
            while (t.hasMoreTokens() && result != null) {
                names.set(0, t.nextToken());
                List<ChildAssociationRef> children = nodeService.getChildrenByName(result, ContentModel.ASSOC_CONTAINS, names);
                result = (children.size() == 1 ? children.get(0).getChildRef() : null);
            }
        }
        return result;
    }

    public static NodeRef findNodeParams(SearchService searchService, String luceneQueryFormat, String... params) {
        String luceneQuery = formatLuceneQuery(luceneQueryFormat, params);
        return findNode(searchService, luceneQuery);
    }

    public static List<NodeRef> findNodesParams(SearchService searchService, String luceneQueryFormat, String... params) {
        String luceneQuery = formatLuceneQuery(luceneQueryFormat, params);
        return findNodes(searchService, luceneQuery, false);
    }

    private static String formatLuceneQuery(String luceneQueryFormat, String[] params) {
        String luceneQuery = luceneQueryFormat;
        for (int i = 0; i < params.length; i++) {
            if (luceneQuery.indexOf("{" + i + "}") > 0) {
                luceneQuery = luceneQuery.replace("{" + i + "}", params[i]);
            } else if (luceneQuery.indexOf("{" + i + ":escape}") > 0) {
                luceneQuery = luceneQuery.replace("{" + i + ":escape}", params[i]);
            } else {
                log.warn("Could not find replacement pattern for lucene param at: " + i + ". Lucene query format is: " + luceneQueryFormat);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Formatting for Lucene query '" + luceneQueryFormat + "' is '" + luceneQuery + "'");
        }
        return luceneQuery;
    }

}
