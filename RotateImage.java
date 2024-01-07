/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.Stack;

/**
 *
 * @author jiks
 */
public class RotateImage {
    
     private Stack<ImageIcon> undoStack = new Stack<>();
     
     public void showRotateMenu(JLabel imageDisplayLabel) {
        JPopupMenu rotateMenu = new JPopupMenu();

        JMenuItem rotate90Clockwise = new JMenuItem("Rotate 90° Clockwise");
        rotate90Clockwise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(imageDisplayLabel, 90);
            }
        });
        rotateMenu.add(rotate90Clockwise);

        JMenuItem rotate90CounterClockwise = new JMenuItem("Rotate 90° Counter-clockwise");
        rotate90CounterClockwise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(imageDisplayLabel, -90);
            }
        });
        rotateMenu.add(rotate90CounterClockwise);

        JMenuItem rotate180 = new JMenuItem("Rotate 180°");
        rotate180.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(imageDisplayLabel, 180);
            }
        });
        rotateMenu.add(rotate180);

        rotateMenu.show(imageDisplayLabel, 0, -rotateMenu.getHeight()); // Adjust the position
    }

    public void rotateImage(JLabel imageDisplayLabel, int degrees) {
        ImageIcon icon = (ImageIcon) imageDisplayLabel.getIcon();
        if (icon != null) {
            undoStack.push(new ImageIcon(icon.getImage())); // Store the previous state before rotation

            Image image = icon.getImage();
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            int newWidth = (degrees % 180 == 0) ? width : height;
            int newHeight = (degrees % 180 == 0) ? height : width;

            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
            g2d.rotate(Math.toRadians(degrees), width / 2.0, height / 2.0);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            ImageIcon newIcon = new ImageIcon(bufferedImage);
            imageDisplayLabel.setIcon(newIcon);
            imageDisplayLabel.revalidate(); // Ensure proper repainting
            imageDisplayLabel.repaint();
        }
    }
    
   /* public ImageIcon undo() {
        if (!undoStack.isEmpty()) {
            return undoStack.pop();
        }
        return null;
    }*/
    
    public ImageIcon undo(JLabel imageDisplayLabel) {
        if (!undoStack.isEmpty()) {
            ImageIcon previousIcon = undoStack.pop();
            imageDisplayLabel.setIcon(previousIcon);
            imageDisplayLabel.revalidate();
            imageDisplayLabel.repaint();
            return previousIcon;
        }
        return null;
    }
}
