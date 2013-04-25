package org.pojonode.util;

import org.alfresco.model.ContentModel;
import org.alfresco.service.namespace.QName;
import org.pojonode.model.pojonode.ContentNode;
import org.pojonode.model.pojonode.FolderNode;
import org.pojonode.model.pojonode.PojoBase;
import org.pojonode.model.pojonode.PojoNode;

import java.util.*;

/**
 * @author Cosmin Marginean, Nov 2, 2010
 */
public class PojonodeConstants {


    public static final String MIMETYPE_XML = "text/xml";

    public static final String PATH_WEB_CLIENT_CONFIG_CUSTOM = "/Company Home/Data Dictionary/Web Client Extension/web-client-config-custom.xml";
    public static final String PATH_CUSTOM_MODEL_DEFINITION = "/Company Home/Data Dictionary/Models/{0}";

    public static final String PREFIX_POJONODE_MODEL = "pojonode-";

    public static final Map<QName, Class> BASE_TYPE_TO_POJO_TYPE;
    public static final Set<Class> POJONODE_BASE_TYPES;

    private static final Map<Class, Class> WRAPPERS_TO_PRIMITIVES;

    static {
        POJONODE_BASE_TYPES = new HashSet<Class>();
        POJONODE_BASE_TYPES.add(PojoNode.class);
        POJONODE_BASE_TYPES.add(FolderNode.class);
        POJONODE_BASE_TYPES.add(PojoBase.class);

        BASE_TYPE_TO_POJO_TYPE = new HashMap<QName, Class>();
        BASE_TYPE_TO_POJO_TYPE.put(ContentModel.TYPE_CONTENT, ContentNode.class);
        BASE_TYPE_TO_POJO_TYPE.put(ContentModel.TYPE_FOLDER, FolderNode.class);
        BASE_TYPE_TO_POJO_TYPE.put(QName.createQName("http://www.alfresco.org/model/content/1.0", "cmobject"), PojoNode.class); //TODO: ??????
        BASE_TYPE_TO_POJO_TYPE.put(ContentModel.TYPE_BASE, PojoNode.class);//TODO: ??????

        WRAPPERS_TO_PRIMITIVES = new Hashtable<Class, Class>();
        WRAPPERS_TO_PRIMITIVES.put(Boolean.class, boolean.class);
        WRAPPERS_TO_PRIMITIVES.put(Character.class, char.class);
        WRAPPERS_TO_PRIMITIVES.put(Byte.class, byte.class);
        WRAPPERS_TO_PRIMITIVES.put(Short.class, short.class);
        WRAPPERS_TO_PRIMITIVES.put(Integer.class, int.class);
        WRAPPERS_TO_PRIMITIVES.put(Long.class, long.class);
        WRAPPERS_TO_PRIMITIVES.put(Float.class, float.class);
        WRAPPERS_TO_PRIMITIVES.put(Double.class, double.class);
        WRAPPERS_TO_PRIMITIVES.put(Void.class, void.class);
    }

    public static boolean isWrapperType(Class<?> cls) {
        return WRAPPERS_TO_PRIMITIVES.containsKey(cls);
    }

    public static Class getPrimitiveClass(Class wrapperClass) {
        return WRAPPERS_TO_PRIMITIVES.get(wrapperClass);
    }

    public static boolean isPojoCoreType(Class modelType) {
        return POJONODE_BASE_TYPES.contains(modelType);
    }
}
