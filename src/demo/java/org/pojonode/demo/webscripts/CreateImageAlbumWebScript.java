package org.pojonode.demo.webscripts;

import org.alfresco.repo.content.MimetypeMap;
import org.pojonode.service.core.IPojonodeService;
import org.pojonode.model.pojonode.FolderNode;
import org.pojonode.demo.model.Image;
import org.pojonode.demo.model.ImageAlbum;
import org.pojonode.demo.model.ImageFolder;
import org.pojonode.demo.model.ImageResolution;
import org.pojonode.demo.model.aspects.Container;
import org.pojonode.demo.model.aspects.ExternalResource;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

/**
 * @author Cosmin Marginean, 3/17/11
 */
public class CreateImageAlbumWebScript extends AbstractWebScript {

    private IPojonodeService pojonodeService;

    public void execute(WebScriptRequest webScriptRequest, WebScriptResponse webScriptResponse) throws IOException {
        try {
            String imgFolderPath = "/Company Home/Guest Home";

            FolderNode guestHome = pojonodeService.getNodeByPath(imgFolderPath, FolderNode.class);

            ImageFolder folder = pojonodeService.createNode(guestHome, ImageFolder.class);
            folder.setName("My Image Folder");

            Image image1 = pojonodeService.createNode(folder, Image.class);
            image1.setName("Image1");
            image1.setTitle("Image content 1");
            image1.setDepartmentId("MD");
            image1.setWidth(800);
            image1.setHeight(600);
            image1.setResolution(ImageResolution.HDPI);
            image1.setScale(1);
            image1.setContentFromClasspath("/org/pojonode/demo/sampleimages/1.png", MimetypeMap.MIMETYPE_IMAGE_PNG);
            image1.getAspect(ExternalResource.class).setExternalId("EXT_IMG_01");

            Image image2 = pojonodeService.createNode(folder, Image.class);
            image2.setName("Image2");
            image2.setTitle("Image content 2");
            image2.setDepartmentId("HR");
            image2.setWidth(500);
            image2.setHeight(200);
            image2.setResolution(ImageResolution.MDPI);
            image2.setScale(0.5);
            image2.setContentFromClasspath("/org/pojonode/demo/sampleimages/2.png", MimetypeMap.MIMETYPE_IMAGE_PNG);
            image2.getAspect(ExternalResource.class).setExternalId("EXT_IMG_02");

            ImageAlbum album = pojonodeService.createNode(imgFolderPath, ImageAlbum.class);
            album.setName("My Image Album");
            album.setDepartmentId("PR");
            album.addImage(image1);
            album.addImage(image2);
            album.getAspect(ExternalResource.class).setExternalId("EXT_IMG_ALBUM_01");
            album.getAspect(Container.class).setContainerId("CONTAINER_001");

            folder.addAlbum(album);
            
            webScriptResponse.getWriter().write("Successfully creaeted image album containing 2 images!");
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void setPojonodeService(IPojonodeService pojonodeService) {
        this.pojonodeService = pojonodeService;
    }
}
