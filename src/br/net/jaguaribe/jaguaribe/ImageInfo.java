/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Get information about an image.
 *
 * @author Fernando Correa da Conceiçaor
 */
public class ImageInfo {

    /**
     * The absolute name of the file with the image.
     */
    private String filename = new String();

    /**
     * Constructor.
     *
     * @param file The absolute name of the file with the image.
     */
    ImageInfo(String file) {
        this.filename = file;
    }

    /**
     * Show info about the current file to stdout
     *
     * @todo trown an exception when there is not a filename
     * @todo trown an exception when the file is not a valid image
     */
    void showInfo() {
        File image = new File(getFilename());
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(image);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }
        } catch (ImageProcessingException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the raw information about the file.
     *
     * @todo trown an exception when the file is not a valid image
     */
    String getRawInfo() {
        StringBuilder answer = new StringBuilder();
        answer.append("<html>");
        File image = new File(getFilename());
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(image);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    answer.append(tag).append("<br />");
                }
            }
        } catch (ImageProcessingException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer.append("</html>").toString();
    }

    /**
     * Get the name of the file.
     *
     * @return The filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename.
     *
     * @param filename The filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
