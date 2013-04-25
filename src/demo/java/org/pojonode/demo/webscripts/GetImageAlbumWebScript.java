package org.pojonode.demo.webscripts;

import org.pojonode.PojonodeException;
import org.pojonode.service.core.IPojonodeService;
import org.pojonode.demo.model.Image;
import org.pojonode.demo.model.ImageAlbum;
import org.pojonode.demo.model.ImageFolder;
import org.pojonode.demo.model.aspects.Container;
import org.pojonode.demo.model.aspects.ExternalResource;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Cosmin Marginean, 17/03/11
 */
public class GetImageAlbumWebScript extends AbstractWebScript {

    private IPojonodeService pojonodeService;

    public void execute(WebScriptRequest webScriptRequest, WebScriptResponse res) throws IOException {
        try {
            ImageAlbum imgAlbum = pojonodeService.getNodeByPath("/Company Home/Guest Home/My Image Album", ImageAlbum.class);
            ImageFolder imgFolder = pojonodeService.getNodeByPath("/Company Home/Guest Home/My Image Folder", ImageFolder.class);

            PrintWriter out = new PrintWriter(res.getWriter());
            out.println("Found Image Folder: " + imgFolder.getName());
            out.println("Found Image Album: " + imgAlbum.getName());
            out.println("Department Id    : " + imgAlbum.getDepartmentId());
            out.println("Image count      : " + imgAlbum.getImages().size());
            out.println("Has aspect ER: " + imgAlbum.hasAspect(ExternalResource.class));
            out.println("Aspect ER-eid: " + imgAlbum.getAspect(ExternalResource.class).getExternalId());
            out.println("Has aspect C : " + imgAlbum.hasAspect(Container.class));
            out.println("Aspect C-eid : " + imgAlbum.getAspect(Container.class).getContainerId());
            for (Image imageDocument : imgAlbum.getImages()) {
                out.println("\nAlbum Image");
                out.println("\tName         : " + imageDocument.getName());
                out.println("\tTitle        : " + imageDocument.getTitle());
                out.println("\tDepartment Id: " + imageDocument.getDepartmentId());
                out.println("\tWidth:       : " + imageDocument.getWidth());
                out.println("\tHeight       : " + imageDocument.getHeight());
                out.println("\tResolution   : " + imageDocument.getResolution());
                out.println("\tScale        : " + imageDocument.getScale());
                out.println("\tHas aspect ER: " + imageDocument.hasAspect(ExternalResource.class));
                out.println("\tAspect ER-eid: " + imageDocument.getAspect(ExternalResource.class).getExternalId());
            }

        } catch (PojonodeException pne) {
            throw new IOException(pne.getMessage(), pne);
        }
    }

    public void setPojonodeService(IPojonodeService pojonodeService) {
        this.pojonodeService = pojonodeService;
    }
}
