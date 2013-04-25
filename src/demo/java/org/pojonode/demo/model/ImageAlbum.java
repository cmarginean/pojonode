package org.pojonode.demo.model;

import org.pojonode.aop.annotations.AssocCreate;
import org.pojonode.aop.annotations.AssocRemove;
import org.pojonode.aop.annotations.ManyToMany;
import org.pojonode.aop.annotations.Type;
import org.pojonode.demo.model.aspects.Container;
import org.pojonode.demo.model.aspects.ExternalResource;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Cosmin Marginean, 3/20/11
 */
@Type(name = "pnd:imageAlbum", mandatoryAspects = {Container.class, ExternalResource.class})
public class ImageAlbum extends ContentBase {

    @ManyToMany(target = Image.class, association = "pnd:album_images", title = "Album Images")
    private List<Image> images = new LinkedList<Image>();

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @AssocCreate(property = "pnd:images")
    public void addImage(Image img) {
    }

    @AssocRemove(property = "pnd:images")
    public void removeImage(Image img) {
    }
}
