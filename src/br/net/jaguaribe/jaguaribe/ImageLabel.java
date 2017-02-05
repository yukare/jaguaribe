/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.jaguaribe.jaguaribe;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import static javax.swing.SwingConstants.CENTER;
import org.imgscalr.Scalr;

/**
 *
 * @author Administrador
 */
public class ImageLabel extends JLabel implements ComponentListener {

    private String imageFile = "";

    /**
     * Default constructor.
     */
    public ImageLabel() {
        super();
        this.addComponentListener(this);
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
        this.updateImage();
    }

    private void updateImage() {
        if (!this.imageFile.isEmpty()) {
            File imageObj = new File(imageFile);
            // System.out.println("altura:"+tamanho.height);
            // System.out.println("largura:"+tamanho.width);
            try {
                BufferedImage imageOriginal = ImageIO.read(imageObj);
                BufferedImage imageResized = Scalr.resize(imageOriginal, this.getWidth(),
                        this.getHeight());
                this.setIcon(new ImageIcon(imageResized));
                this.setHorizontalAlignment(CENTER);
                this.setVerticalAlignment(CENTER);
            } catch (IOException ex) {
                Logger.getLogger(ImageLabel.class.getName()).log(Level.SEVERE,
                        "Error while reading the image", ex);
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateImage();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        this.updateImage();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
