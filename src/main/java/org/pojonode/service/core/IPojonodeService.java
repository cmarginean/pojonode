package org.pojonode.service.core;

import org.alfresco.service.cmr.repository.NodeRef;
import org.pojonode.PojonodeException;
import org.pojonode.model.pojonode.FolderNode;
import org.pojonode.model.pojonode.PojoAspect;
import org.pojonode.model.pojonode.PojoNode;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public interface IPojonodeService {

    <T extends PojoNode> T getNode(String strNodeRef, Class<T> pojoClass) throws PojonodeException;

    <T extends PojoNode> T getNode(NodeRef nodeRef, Class<T> pojoClass) throws PojonodeException;

    <T extends PojoNode> T getNodeByPath(String path, Class<T> pojoClass) throws PojonodeException;

    <T extends PojoNode> T createNode(String repositoryPath, Class<T> pojoClass) throws PojonodeException;

    <T extends PojoNode> T createNode(NodeRef parentFolder, Class<T> pojoClass) throws PojonodeException;

    <T extends PojoNode> T createNode(FolderNode parentFolder, Class<T> pojoClass) throws PojonodeException;

    //TODO: This shouldn't be here
    <T extends PojoAspect> T getAspect(PojoNode pojoNode, Class<T> pojoClass) throws PojonodeException;
}