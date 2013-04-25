package org.pojonode.model.pojonode;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cosmin Marginean, 10/05/2011
 */
@Type(name = "cm:folder")
public class FolderNode extends PojoNode {

    public void addChild(PojoNode child) {
        serviceRegistry.getNodeService().addChild(getNodeRef(), child.getNodeRef(), ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, child.getName()));
    }

    public List<PojoNode> getChildren() throws PojonodeException {
        List<PojoNode> children = new ArrayList<PojoNode>();
        List<ChildAssociationRef> childAssocs = serviceRegistry.getNodeService().getChildAssocs(getNodeRef());
        for (ChildAssociationRef childAssociation : childAssocs) {
            PojoNode child = pojonodeService.getNode(childAssociation.getChildRef(), PojoNode.class);
            children.add(child);
        }
        return children;
    }

    public void removeChild(PojoNode child) {
        serviceRegistry.getNodeService().removeChild(getNodeRef(), child.getNodeRef());
    }
}
