/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveImage {
    public static boolean saveImage(BufferedImage image) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Create custom file filters for supported image formats
        FileFilter pngFilter = new FileNameExtensionFilter("PNG Files (*.png)", "png");
        FileFilter jpgFilter = new FileNameExtensionFilter("JPG Files (*.jpg)", "jpg", "jpeg");
        FileFilter svgFilter = new FileNameExtensionFilter("SVG Files (*.svg)", "svg");

        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.addChoosableFileFilter(svgFilter);

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            FileFilter selectedFilter = fileChooser.getFileFilter();

            // Determine the format based on the selected filter
            String format = null;
            if (selectedFilter == pngFilter) {
                format = "PNG";
            } else if (selectedFilter == jpgFilter) {
                format = "JPEG";
            } else if (selectedFilter == svgFilter) {
                // Handle saving as SVG (you may need a different approach)
                // You might want to use a library like Batik for SVG generation
                JOptionPane.showMessageDialog(
                    null,
                    "SVG format is not supported in this example.",
                    "Save Image Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            try {
                ImageIO.write(image, format, selectedFile);
                JOptionPane.showMessageDialog(
                    null,
                    "Image saved successfully!",
                    "Save Image",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error saving the image: " + e.getMessage(),
                    "Save Image Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return false;
    }
}


