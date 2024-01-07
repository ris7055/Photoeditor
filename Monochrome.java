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
import java.awt.Color;

public class Monochrome {
    public BufferedImage convertToMonochrome(BufferedImage image) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage monochromeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                int grayscale = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                int newRgb = new Color(grayscale, grayscale, grayscale).getRGB();
                monochromeImage.setRGB(x, y, newRgb);
            }
        }

        return monochromeImage;
    }
}

