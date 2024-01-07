/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class ResizeImage {
    private Stack<BufferedImage> undoStack; // Stack to store previous images

    public ResizeImage() {
        undoStack = new Stack<>();
    }

    public BufferedImage resize(BufferedImage inputImage, int newWidth, int newHeight) {
        // Push the current image onto the undo stack before resizing
        undoStack.push(inputImage);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    // Implement an undo method if needed
    public BufferedImage undo() {
        if (!undoStack.isEmpty()) {
            return undoStack.pop();
        }
        return null; // No image to undo to
    }
}


