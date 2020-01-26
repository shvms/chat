package com.shvms.chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/*
 * TODO: Handle edge cases
 *  Assumption: Images in the same directory.
 */
public class ImageFile {
    private File fileObj;
    private String name;
    private String extension;

    public ImageFile(String file) {
        name = file.substring(0, file.lastIndexOf('.'));
        extension = file.substring(file.lastIndexOf('.')+1);
        fileObj = new File(file);
    }

    public File getFileObj() {
        return fileObj;
    }

    public String getExtension() {
        return extension;
    }

    public String getName() {
        return name;
    }

    public String getBase64String(boolean withHeaders) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.fileObj);
        byte[] fileBytes = new byte[(int) fileObj.length()];
        fileInputStream.read(fileBytes); // returns total bytes read; ignored here

        String base64String = new String(Base64.getEncoder().encode(fileBytes), StandardCharsets.UTF_8);

        if (withHeaders) {
            base64String = "data:image/" + extension + ";base64," + base64String;
        }

        return base64String;
    }

    public String getBase64String() throws IOException {
        return getBase64String(true);
    }

    public String toString() {
        try {
            return getBase64String();
        } catch (IOException e) {
            return "Exception has occurred.";
        }
    }
}
