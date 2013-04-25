package org.pojonode.model.metamodel.attributes;

import org.alfresco.service.cmr.repository.NodeRef;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class AlfrescoPropertyType {

    private static Map<Class, String> classesToAlfrescoTypes;

    static {
        classesToAlfrescoTypes = new Hashtable<Class, String>();

        classesToAlfrescoTypes.put(String.class, "d:text");

        classesToAlfrescoTypes.put(int.class, "d:int");
        classesToAlfrescoTypes.put(Integer.class, "d:int");

        classesToAlfrescoTypes.put(long.class, "d:long");
        classesToAlfrescoTypes.put(Long.class, "d:long");

        classesToAlfrescoTypes.put(float.class, "d:float");
        classesToAlfrescoTypes.put(Float.class, "d:float");

        classesToAlfrescoTypes.put(double.class, "d:double");
        classesToAlfrescoTypes.put(Double.class, "d:double");

        classesToAlfrescoTypes.put(boolean.class, "d:boolean");
        classesToAlfrescoTypes.put(Boolean.class, "d:boolean");

        classesToAlfrescoTypes.put(Date.class, "d:date");

        classesToAlfrescoTypes.put(NodeRef.class, "d:noderef");

        classesToAlfrescoTypes.put(Enum.class, "d:text");
    }

    public static String getAlfrescoPropertyType(Class typeClass) {
        if (typeClass.isEnum()) {
            return classesToAlfrescoTypes.get(Enum.class);
        } else {
            return classesToAlfrescoTypes.get(typeClass);
        }
    }
}
