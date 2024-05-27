package md.ceiti.frontend.util;

import com.vaadin.flow.server.StreamResource;
import md.ceiti.frontend.exception.FrontendException;
import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {

    public static StreamResource toStreamResource(String fileName, byte[] bytes) {
        return new StreamResource(
                fileName,
                () -> new ByteArrayInputStream(bytes));
    }

    public static FileSystemResource toFileSystemResource(String fileNameWithExtension, byte[] bytes) {
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');
        String fileName = fileNameWithExtension.substring(0, lastDotIndex);
        String extension = fileNameWithExtension.substring(lastDotIndex + 1);

        try {
            File tempFile = File.createTempFile(fileName, "." + extension);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bytes);
            fos.close();

            return new FileSystemResource(tempFile);
        } catch (IOException e) {
            throw new FrontendException("Cannot convert from byte[] to FileSystemResource, cause: " + e.getMessage());
        }
    }
}
