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
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurAdjustment {
    public BufferedImage applyBlur(BufferedImage image, float blurIntensity) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage blurredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a blur filter
        float[] blurMatrix = {
            1.0f / 9, 1.0f / 9, 1.0f / 9,
            1.0f / 9, 1.0f / 9, 1.0f / 9,
            1.0f / 9, 1.0f / 9, 1.0f / 9
        };
        Kernel kernel = new Kernel(3, 3, blurMatrix);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        // Apply the blur filter
        convolveOp.filter(image, blurredImage);

        // Create a new image to apply blur with adjusted intensity
        BufferedImage adjustedBlurredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Adjust the blur intensity by iterating over the pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = blurredImage.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;

                // Adjust the RGB values based on blur intensity
                red = (int) (red + (blurIntensity * (128 - red)));
                green = (int) (green + (blurIntensity * (128 - green)));
                blue = (int) (blue + (blurIntensity * (128 - blue)));

                // Clamp the RGB values to the valid range (0-255)
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                // Recreate the ARGB pixel with adjusted RGB values
                int adjustedArgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                adjustedBlurredImage.setRGB(x, y, adjustedArgb);
            }
        }

        return adjustedBlurredImage;
    }
}





