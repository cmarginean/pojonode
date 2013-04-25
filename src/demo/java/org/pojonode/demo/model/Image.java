package org.pojonode.demo.model;

import org.alfresco.service.cmr.repository.NodeRef;
import org.pojonode.aop.annotations.Property;
import org.pojonode.aop.annotations.Type;
import org.pojonode.demo.model.aspects.ExternalResource;

import java.util.List;

/**
 * @author Cosmin Marginean, 3/17/11
 */
@Type(name = "pnd:image", mandatoryAspects = ExternalResource.class)
public class Image extends ContentBase {

    @Property(name = "pnd:imageWidth", title = "Width", mandatory = true)
    private int width;

    @Property(name = "pnd:imageHeight", title = "Height", mandatory = true)
    private int height;

    @Property(title = "Image ImageResolution")
    private ImageResolution resolution;

    @Property(visible = false)
    private double scale;

    @Property(visible = false, target = NodeRef.class)
    private List<NodeRef> imgRenditions;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImageResolution getResolution() {
        return resolution;
    }

    public void setResolution(ImageResolution resolution) {
        this.resolution = resolution;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public List<NodeRef> getImgRenditions() {
        return imgRenditions;
    }

    public void setImgRenditions(List<NodeRef> imgRenditions) {
        this.imgRenditions = imgRenditions;
    }
}
