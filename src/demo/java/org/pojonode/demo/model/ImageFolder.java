package org.pojonode.demo.model;

import org.pojonode.aop.annotations.AssocCreate;
import org.pojonode.aop.annotations.AssocRemove;
import org.pojonode.aop.annotations.ManyToMany;
import org.pojonode.aop.annotations.Type;
import org.pojonode.model.pojonode.FolderNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Cosmin Marginean, 10/05/2011
 */
@Type(name = "pnd:imageFolder")
public class ImageFolder extends FolderNode {

    @ManyToMany(target = ImageAlbum.class, association = "pnd:albums", title = "Albums", childAssociation = true)
    private List<ImageAlbum> albums = new LinkedList<ImageAlbum>();

    public List<ImageAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(List<ImageAlbum> albums) {
        this.albums = albums;
    }

    @AssocCreate(property = "pnd:albums")
    public void addAlbum(ImageAlbum imageAlbum) {
    }

    @AssocRemove(property = "pnd:albums")
    public void removeAlbum(ImageAlbum imageAlbum) {
    }
}
