/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt.tumblr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas
 */
public class IFTTTTumblr {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here

        URL location = IFTTTTumblr.class.getProtectionDomain().getCodeSource().getLocation();
        String root = location.getFile().substring(0, location.getFile().lastIndexOf("/") + 1);

        if (root.indexOf("NetBeans") != 0) {
            root = "/Users/Thomas/Dropbox/IFTTT/";
        }

        String[] fromFolders = new String[]{"Tumblr", "Feedly", "Active", "Instagram"};
        
        String toFolder = "Processing";
        toFolder = root + toFolder + "/";
        
        String delFolder = "Deleting";
        delFolder = root + delFolder + "/";

        for (String eachFromFolder : fromFolders) {
            eachFromFolder = root + eachFromFolder + "/";

            File dir = new File(eachFromFolder);
            File[] filesList = dir.listFiles();
            for (File eachFile : filesList) {
                if (eachFile.isFile()) {

                    String fileName = eachFile.getName();

                    if (fileName.endsWith(".jar")) {
                        continue;
                    }

                    MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
                    mtftp.addMimeTypes("image png tif jpg jpeg bmp");

                    String mime = mtftp.getContentType(eachFile);

                    if ((mime != null && mime.indexOf("image") != -1) || fileName.indexOf(".jpg") != -1) {
                        fileName = MD5CheckSum.getMD5Checksum(eachFile.getAbsolutePath());

                        File file2 = new File(toFolder + fileName + ".jpg");
//                        for (int i = 1; file2.exists(); i++) {
//                            file2 = new File(eachToFolder + fileName + " (" + i + ").jpg");
//                        }

                        if (file2.exists()) {
                            eachFile.delete();
                        } else {
                            eachFile.renameTo(file2);
                        }
                    }
                }
            }
        }

        File dir = new File(toFolder);
        File[] filesList = dir.listFiles();
        for (File eachImage : filesList) {
            if (eachImage.isFile()) {
                try {
                    BufferedImage bimg = ImageIO.read(eachImage);
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();
                    
                    if(width < 960 && height < 960) {
                        File file2 = new File(delFolder + eachImage.getName());
                        
                        if (file2.exists()) {
                            eachImage.delete();
                        } else {
                            eachImage.renameTo(file2);
                        }
                    }
                } catch(Exception e) {
                    eachImage.delete();
                }
            }
        }
    }
}
