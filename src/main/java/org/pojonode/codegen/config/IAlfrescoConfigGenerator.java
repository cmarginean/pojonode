package org.pojonode.codegen.config;

import org.pojonode.PojonodeException;
import org.pojonode.model.metamodel.PojonodeModel;

import java.io.OutputStream;

/**
 * @author Cosmin Marginean, 6/14/11
 */
public interface IAlfrescoConfigGenerator {

    void generateConfig(PojonodeModel pojonodeModel, OutputStream outputStream) throws PojonodeException;

    String getMimeType();
}
