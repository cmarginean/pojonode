package org.pojonode.codegen.revert;

import org.alfresco.service.namespace.QName;
import org.pojonode.PojonodeException;

import java.io.OutputStream;
import java.util.Set;

/**
 * @author Cosmin Marginean, 11/01/12
 */
public interface IAlfrescoModelConverter {

    void generatePojoModel(String outputDirectory, String basePackage, Set<QName> modelNames) throws PojonodeException;

    void generatePojoModel(String outputDirectory, String basePackage, QName... modelNames) throws PojonodeException;

    void generatePojoSrcJar(OutputStream outputStream, String basePackage, QName... modelNames) throws PojonodeException;
}
