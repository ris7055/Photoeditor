/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class CropImage {
    private BufferedImage image;
    private Rectangle clip;
    private boolean showCrop;
    private Area mask;
    private Stack<ImageIcon> undoStack;
    JLabel imageDisplayLabel;
    private boolean cropMode;

    public CropImage() {
        clip = new Rectangle();
        showCrop = false;
        undoStack = new Stack<>();
        cropMode = false;
    }

    public void loadImageFromUser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadImage(selectedFile);
        }
    }

    public void loadImage(File file) {
        try {
            image = ImageIO.read(file);
            undoStack.push(new ImageIcon(image));
            showCrop = false;
            mask = new Area(new Rectangle(0, 0, image.getWidth(), image.getHeight()));
            repaint();
        } catch (IOException ioe) {
            System.err.println("read: " + ioe.getMessage());
        }
    }
    
    
    public void setCropMode(boolean enable) {
        cropMode = enable;
        if (cropMode) {
            imageDisplayLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startCrop(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    endCrop(e);
                    imageDisplayLabel.removeMouseListener(this);
                    highlightCropArea((Graphics2D) imageDisplayLabel.getGraphics(), imageDisplayLabel, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height);
                    int confirm = JOptionPane.showConfirmDialog(null, "Do you want to crop the selected area?", "Crop Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        applyConfirmedCrop(image, new ImageIcon(((ImageIcon) imageDisplayLabel.getIcon()).getImage()), imageDisplayLabel, (JFrame) SwingUtilities.getWindowAncestor(imageDisplayLabel));
                    } else {
                        clearCropHighlights(imageDisplayLabel);
                    }
                }
            });

            imageDisplayLabel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    updateCrop(e);
                    highlightCropArea((Graphics2D) imageDisplayLabel.getGraphics(), imageDisplayLabel, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height);
                }
            });
        }
    }

    public void startCrop(MouseEvent e) {
        clip.setLocation(e.getPoint());
    }

    public void endCrop(MouseEvent e) {
        clip.setSize(new Dimension((int) (e.getPoint().getX() - clip.getX()), (int) (e.getPoint().getY() - clip.getY())));
    }

    public void updateCrop(MouseEvent e) {
        clip.setSize(new Dimension((int) (e.getPoint().getX() - clip.getX()), (int) (e.getPoint().getY() - clip.getY())));
    }

    public void highlightCropArea(Graphics2D g2d, JLabel imageDisplayLabel, int startX, int startY, int endX, int endY) {
        g2d.setXORMode(Color.WHITE);
        g2d.drawRect(startX, startY, endX - startX, endY - startY);
        g2d.setPaintMode();
    }

    public void clearCropHighlights(JLabel imageDisplayLabel) {
        Graphics2D g2d = (Graphics2D) imageDisplayLabel.getGraphics();
        g2d.clearRect(0, 0, imageDisplayLabel.getWidth(), imageDisplayLabel.getHeight());
        g2d.drawImage(image, 0, 0, imageDisplayLabel.getWidth(), imageDisplayLabel.getHeight(), null);
    }

    public void applyConfirmedCrop(BufferedImage originalImage, ImageIcon icon, JLabel imageDisplayLabel, JFrame frame) {
        Rectangle cropRectangle = clip.getBounds();
        BufferedImage croppedImage = originalImage.getSubimage(cropRectangle.x, cropRectangle.y, cropRectangle.width, cropRectangle.height);
        icon.setImage(croppedImage);
        imageDisplayLabel.setIcon(icon);
        showCrop = false;
        frame.revalidate();
        frame.repaint();
    }

    public ImageIcon undo() {
        if (!undoStack.isEmpty()) {
            ImageIcon previousIcon = undoStack.pop();
            imageDisplayLabel.setIcon(previousIcon);
            imageDisplayLabel.revalidate();
            imageDisplayLabel.repaint();
            return previousIcon;
        }
        return null;
    }

    public void repaint() {
        imageDisplayLabel.setIcon(new ImageIcon(image));
        imageDisplayLabel.revalidate();
        imageDisplayLabel.repaint();
    }
}
