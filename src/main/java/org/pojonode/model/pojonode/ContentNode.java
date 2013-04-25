package org.pojonode.model.pojonode;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.util.Content;
import org.apache.commons.io.IOUtils;
import org.pojonode.aop.annotations.Type;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Cosmin Marginean, 10/05/2011
 */
@Type(name = "cm:content")
public class ContentNode extends PojoNode {

    public InputStream getContent() {
        ContentReader reader = serviceRegistry.getContentService().getReader(getNodeRef(), ContentModel.PROP_CONTENT);
        return reader.getContentInputStream();
    }

    public void setContent(String contentAsString, String mimeType) {
        ContentWriter contentWriter = serviceRegistry.getContentService().getWriter(getNodeRef(), ContentModel.PROP_CONTENT, true);
        contentWriter.setMimetype(mimeType);
        contentWriter.putContent(contentAsString);
    }

    private void setContent(InputStream inputStream, String mimeType) {
        ContentWriter contentWriter = serviceRegistry.getContentService().getWriter(getNodeRef(), ContentModel.PROP_CONTENT, true);
        contentWriter.setMimetype(mimeType);
        contentWriter.putContent(inputStream);
    }

    public void setContent(Content content) {
        setContent(content.getInputStream(), content.getMimetype());
    }

    public void setContent(byte[] content, String mimeType) {
        ContentWriter contentWriter = serviceRegistry.getContentService().getWriter(getNodeRef(), ContentModel.PROP_CONTENT, true);
        contentWriter.setMimetype(mimeType);
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(content);
            contentWriter.putContent(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public void setContentFromClasspath(String classpath, String mimeType) {
        setContent(getClass().getResourceAsStream(classpath), mimeType);
    }
}
