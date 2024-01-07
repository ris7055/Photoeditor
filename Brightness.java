/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import java.awt.image.BufferedImage;

public class Brightness {
    public BufferedImage adjustBrightness(BufferedImage image, float brightnessFactor) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage adjustedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int alpha = (rgb >> 24) & 0xFF;
                int red = (int) (((rgb >> 16) & 0xFF) * brightnessFactor);
                int green = (int) (((rgb >> 8) & 0xFF) * brightnessFactor);
                int blue = (int) ((rgb & 0xFF) * brightnessFactor);

                // Ensure values are within the valid range [0, 255]
                red = Math.min(Math.max(0, red), 255);
                green = Math.min(Math.max(0, green), 255);
                blue = Math.min(Math.max(0, blue), 255);

                int adjustedRgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                adjustedImage.setRGB(x, y, adjustedRgb);
            }
        }

        return adjustedImage;
    }
}

