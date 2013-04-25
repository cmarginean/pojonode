package org.pojonode.util;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.Aspect;
import org.pojonode.aop.annotations.Type;
import org.w3c.dom.Document;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @author Cosmin Marginean, Oct 11, 2010
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class);

    private static StoreRef spaceStore;

    public static String getPrefix(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return value.split("\\:")[0];
    }

    public static String getLocalName(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        if (!StringUtils.contains(value, ":")) {
            return value;
        }

        String[] strings = StringUtils.split(value, ':');
        return ArrayUtils.isEmpty(strings) ? null : strings[1];
    }

    public static StoreRef getSpacesStore() {
        if (spaceStore == null) {
            spaceStore = new StoreRef("workspace://SpacesStore");
        }
        return spaceStore;
    }

    public static NodeRef storeContentInPath(ServiceRegistry services, String path, String contentName, String contentMimeType, InputStream inputStream) throws PojonodeException {
        return storeContentInPath(services, path, contentName, ContentModel.TYPE_CONTENT, contentMimeType, inputStream);
    }

    public static NodeRef storeContentInPath(ServiceRegistry services, String path, String contentName, QName contentType, String contentMimeType, InputStream inputStream) throws PojonodeException {
        NodeService nodeService = services.getNodeService();

        NodeRef parentFolder = SearchUtils.findNodeByPath(nodeService, path);
        NodeRef contentRef = nodeService.getChildByName(parentFolder, ContentModel.ASSOC_CONTAINS, contentName);
        if (contentRef == null) {
            QName qName = QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, contentName);
            ChildAssociationRef childAssociationRef = nodeService.createNode(parentFolder, ContentModel.ASSOC_CONTAINS, qName, contentType);
            contentRef = childAssociationRef.getChildRef();
        }

        ContentWriter writer = services.getContentService().getWriter(contentRef, ContentModel.PROP_CONTENT, true);
        writer.setMimetype(contentMimeType);
        writer.putContent(inputStream);
        nodeService.setProperty(contentRef, ContentModel.PROP_NAME, contentName);
        nodeService.setProperty(contentRef, ContentModel.PROP_TITLE, contentName);
        return contentRef;
    }

    public static NodeRef storeDocumentInPath(ServiceRegistry services, Document document, String contentName, QName contentType, String contentMimeType, String path) throws PojonodeException {
        NodeRef result = null;
        ByteArrayOutputStream outputStream = null;
        InputStream is = null;
        try {
            outputStream = new ByteArrayOutputStream();
            XmlUtils.serializeDocument(document, outputStream);
            is = new ByteArrayInputStream(outputStream.toByteArray());
            result = storeContentInPath(services, path, contentName, contentType, contentMimeType, is);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PojonodeException(e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static File getTempFile(String filename) throws IOException {
        return File.createTempFile(FilenameUtils.removeExtension(filename), "." + FilenameUtils.getExtension(filename));
    }

    public static String getTempDir() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(SystemUtils.getJavaIoTmpDir().getAbsolutePath())
                .append(File.separator)
                .append(UUID.randomUUID().toString());
        String dirPath = sb.toString();
        new File(dirPath).mkdirs();
        return dirPath;
    }

    public static List<Field> getAllClassFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllClassFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static String getParentTypeFromAnnotation(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        return (Type.class.equals(annotation.annotationType())) ? ((Type) annotation).parent() : ((Aspect) annotation).parent(); //This is just sad...
    }

    public static String getNameFromAnnotation(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        return (Type.class.equals(annotation.annotationType())) ? ((Type) annotation).name() : ((Aspect) annotation).name();
    }

    public static void jarDirectory(String directory, OutputStream outputStream) {
        File directoryToZip = new File(directory);
        List<File> fileList = new ArrayList<File>();
        getAllFiles(directoryToZip, fileList);
        writeZipFile(directoryToZip, fileList, outputStream);
    }

    public static void getAllFiles(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        for (File file : files) {
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }
    }

    public static void writeZipFile(File directoryToZip, List<File> fileList, OutputStream outputStream) {
        try {
            JarOutputStream zos = new JarOutputStream(outputStream);

            for (File file : fileList) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToZip(File directoryToZip, File file, JarOutputStream zos) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(file);

        // we want the JarEntry's path to be a relative path that is relative
        // to the directory being zipped, so chop off the rest of the path
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
        JarEntry JarEntry = new JarEntry(zipFilePath);
        zos.putNextEntry(JarEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
