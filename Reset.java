/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;

public class Reset {
    public static boolean showConfirmationDialog() {
        int response = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to reset the image?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION
        );

        return response == JOptionPane.YES_OPTION;
    }

    public static BufferedImage resetImage(BufferedImage originalImage) {
        // You can implement the reset logic here, e.g., by returning the original image
        return originalImage;
    }
}
