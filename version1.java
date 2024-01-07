/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;  // Import for IOException
import javax.imageio.ImageIO;  // Import for ImageIO
import java.util.Stack;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author jiks
 */
public class version1 {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ImageIcon icon;
    private JLabel imageDisplayLabel;
    private CropImage cropImage;
    private RotateImage rotateImage;
    private BlurAdjustment blurAdjustment;
    private ResizeImage resizeImage;
    private BufferedImage originalImage;
    private UndoAdjustment undoAdjustment;
    private JSlider blurIntensitySlider;
    private JLabel blurIntensityLabel;
    private boolean blurButtonClicked = false;
    private Brightness brightnessAdjustment;
    private Monochrome monochrome;
    private Reset reset;
    private SaveImage saveImage;
    private BufferedImage drawingImage;
    private boolean drawingMode = false;
    private Point startPoint = null;
   
    public version1() {
        
        frame = new JFrame();
        tabbedPane = new JTabbedPane();
        JPanel panel = homePage();

        frame.add(tabbedPane);
        tabbedPane.addTab("Home", panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Photo Editor");
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        cropImage = new CropImage();
        rotateImage = new RotateImage();
        blurAdjustment = new BlurAdjustment();
        resizeImage = new ResizeImage();
        undoAdjustment = new UndoAdjustment();
        brightnessAdjustment = new Brightness();
        monochrome = new Monochrome();
        reset = new Reset();
        saveImage = new SaveImage();
       
        blurIntensitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        blurIntensitySlider.setPreferredSize(new Dimension(200, 50));
        blurIntensitySlider.setVisible(false); // Initially, hide the slider
        
        blurIntensityLabel = new JLabel("Blur Intensity: " + blurIntensitySlider.getValue());
        blurIntensityLabel.setPreferredSize(new Dimension(200, 50));
        blurIntensityLabel.setVisible(false); // Initially, hide the label
        
        blurIntensitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
                blurIntensityLabel.setText("Blur Intensity: " + blurIntensitySlider.getValue());
            
            }
        });
    }
    
    private JPanel homePage() {
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 50, 100));
        panel.setBackground(Color.decode("#f7f1e6"));

        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(150, 150, 50, 150);

        JLabel titleLabel = createTitleLabel("Photo Editor");
        panel.add(titleLabel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(10, 0, 10, 0);

        JButton importButton = createButton("Import Image");
        importButton.setPreferredSize(new Dimension(200, 50));
        importButton.setBackground(Color.decode("#fae6c0"));
        importButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                JFileChooser filechooser = new JFileChooser();
                filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = filechooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    
                    File selectedFile = filechooser.getSelectedFile();
                    showImage(selectedFile);
                    
                }
            }
        });

        constraints.gridy = 2;
        panel.add(importButton, constraints);
        return panel;
        
    }

    private JLabel createTitleLabel(String text) {
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.BOLD, 48));
        label.setForeground(Color.decode("#ff7733"));
        return label;
        
    }

    private JButton createButton(String text) {
        
        JButton button = new JButton(text);
        button.setFont(new Font("Poppins", Font.PLAIN, 16));
        button.setForeground(Color.BLACK);
        Color initialBackgroundColor = Color.decode("#fae6c0");

        button.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                
                button.setBackground(Color.decode("#f7d38f"));
                button.setOpaque(true);
                button.requestFocus();
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
                button.setBackground(initialBackgroundColor);
                button.setOpaque(false);
                
            }
        });

        return button;
        
    }
    

    private void showImage(File selectedFile) {
         
        ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
         
         int originalWidth = icon.getIconWidth();
         int originalHeight = icon.getIconHeight();
         int scaledWidth, scaledHeight;
         
         if (originalWidth > originalHeight) {
             
             scaledWidth = 700;
             scaledHeight = (int) ((double) originalHeight / originalWidth * scaledWidth);
         
         } else {
             
             scaledHeight = 700;
             scaledWidth = (int) ((double) originalWidth / originalHeight * scaledHeight);
         
         }
         
         Image image = icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
         this.icon = new ImageIcon(image);
         originalImage = loadImage(selectedFile.getAbsolutePath());
         
         JPanel imagePanel = new JPanel(new BorderLayout());
         JPanel labelPanel = new JPanel();
         labelPanel.setLayout(new GridBagLayout());
         labelPanel.setPreferredSize(new Dimension(700, 700));
         
         imageDisplayLabel = new JLabel(this.icon);
         labelPanel.add(imageDisplayLabel);
         imagePanel.add(imageDisplayLabel, BorderLayout.CENTER);
         imagePanel.add(labelPanel, BorderLayout.CENTER);
       
        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        cropImage = new CropImage();
        cropImage.imageDisplayLabel = imageDisplayLabel;
        cropImage.loadImage(selectedFile);

        JButton cropButton = createButton("Crop");
        cropButton.setPreferredSize(new Dimension(148, 36));
        cropButton.setBackground(Color.decode("#fae6c0"));
        cropButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                undoAdjustment.pushUndo(icon);
                cropImage.setCropMode(true);
                
            }
        });
        
        JButton rotateButton = createButton("Rotate");
        rotateButton.setPreferredSize(new Dimension(148, 36));
        rotateButton.setBackground(Color.decode("#fae6c0"));
        rotateButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                undoAdjustment.pushUndo(icon);
                rotateImage.showRotateMenu(imageDisplayLabel);
                
            }
        });
        
        JButton blurButton = createButton("Blur");
        blurButton.setPreferredSize(new Dimension(200, 50));
        blurButton.setBackground(Color.decode("#fae6c0"));
        blurButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                undoAdjustment.pushUndo(icon);
                float blurIntensity = blurIntensitySlider.getValue() / 100.0f;
                BufferedImage blurredImage = blurAdjustment.applyBlur(originalImage, blurIntensity);
                if (blurredImage != null) {
                    
                    ImageIcon blurredIcon = new ImageIcon(blurredImage);
                    imageDisplayLabel.setIcon(blurredIcon);
                
                } else {
                
                }
                
                blurButtonClicked = !blurButtonClicked;
                blurIntensitySlider.setVisible(blurButtonClicked);
                blurIntensityLabel.setVisible(blurButtonClicked);
            
            }
        
        });
        
        JButton brightnessButton = createButton("Brightness");
        brightnessButton.setPreferredSize(new Dimension(200, 50));
        brightnessButton.setBackground(Color.decode("#fae6c0"));
        brightnessButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                undoAdjustment.pushUndo(icon);
                
                String brightnessInput = JOptionPane.showInputDialog(frame, "Enter brightness factor (e.g., 0.5 for 50% brightness):");
                try {
                    
                    float brightnessFactor = Float.parseFloat(brightnessInput);
                    applyBrightness(brightnessFactor);
                
                } catch (NumberFormatException ex) {
                    
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid brightness factor.");
                
                }
            
            }
        
        });

        JButton monochromeButton = createButton("Monochrome");
        monochromeButton.setPreferredSize(new Dimension(200, 50));
        monochromeButton.setBackground(Color.decode("#fae6c0"));
        monochromeButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                applyMonochrome();
            
            }
        
        });

        JButton resizeButton = createButton("Resize");
        resizeButton.setPreferredSize(new Dimension(200, 50));
        resizeButton.setBackground(Color.decode("#fae6c0"));
        resizeButton.addActionListener(new ActionListener() {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                
                undoAdjustment.pushUndo(icon);
                
                int newWidth = 0;
                int newHeight = 0;
                
                try {
                    
                    String widthInput = JOptionPane.showInputDialog(frame, "Enter new width:");
                    String heightInput = JOptionPane.showInputDialog(frame, "Enter new height:");
                    newWidth = Integer.parseInt(widthInput);
                    newHeight = Integer.parseInt(heightInput);
                } catch (NumberFormatException ex) {
                    
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values for width and height.");
                    return;
                    
                }
                
                BufferedImage resizedImage = resizeImage.resize(originalImage, newWidth, newHeight);

                if (resizedImage != null) {
                    
                    ImageIcon resizedIcon = new ImageIcon(resizedImage);
                    imageDisplayLabel.setIcon(resizedIcon);
                
                } else {
                
                }
            
            }
        
        });


        JButton drawButton = createButton("Draw");
        drawButton.setPreferredSize(new Dimension(200, 50));
        drawButton.setBackground(Color.decode("#fae6c0"));
        drawButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (!drawingMode) {
                    
                    drawingMode = true;
                    imageDisplayLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    
                    drawingImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = drawingImage.createGraphics();
                    g2d.drawImage(originalImage, 0, 0, null);
                    g2d.dispose();
                    
                    imageDisplayLabel.addMouseListener(new MouseAdapter() {
                        
                        @Override
                        public void mousePressed(MouseEvent e) {
                            
                            startPoint = e.getPoint();
                        
                        }
                        
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            
                            startPoint = null;
                        
                        }
                    
                    });
                    
                    imageDisplayLabel.addMouseMotionListener(new MouseMotionAdapter() {
                        
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            
                            if (startPoint != null) {
                                
                                Point endPoint = e.getPoint();
                                drawLines(startPoint, endPoint);
                                startPoint = endPoint;
                                ImageIcon drawingIcon = new ImageIcon(drawingImage);
                                imageDisplayLabel.setIcon(drawingIcon);
                            
                            }
                        
                        }
                    
                    });
                
                } else {
                    
                    drawingMode = false;
                    imageDisplayLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    imageDisplayLabel.removeMouseListener(imageDisplayLabel.getMouseListeners()[imageDisplayLabel.getMouseListeners().length - 1]);
                    imageDisplayLabel.removeMouseMotionListener(imageDisplayLabel.getMouseMotionListeners()[imageDisplayLabel.getMouseMotionListeners().length - 1]);
                
                }
            
            }
        
        });
        
        JButton resetButton = createButton("Reset");
        resetButton.setPreferredSize(new Dimension(200, 50));
        resetButton.setBackground(Color.decode("#fae6c0"));
        resetButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (drawingMode) {
                    
                    drawingMode = false;
                    imageDisplayLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    imageDisplayLabel.removeMouseListener(imageDisplayLabel.getMouseListeners()[imageDisplayLabel.getMouseListeners().length - 1]);
                    imageDisplayLabel.removeMouseMotionListener(imageDisplayLabel.getMouseMotionListeners()[imageDisplayLabel.getMouseMotionListeners().length - 1]);
                
                }
                
                showResetConfirmation();
            
            }
        
        });

        JButton saveButton = createButton("Save");
        saveButton.setPreferredSize(new Dimension(200, 50));
        saveButton.setBackground(Color.decode("#fae6c0"));
        saveButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                saveImage();
            
            }
        
        });
        
        JButton undoButton = createButton("Undo");
        undoButton.setPreferredSize(new Dimension(200, 50));
        undoButton.setBackground(Color.decode("#fae6c0"));
        undoButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                ImageIcon previousIcon = undoAdjustment.popUndo();
                if (previousIcon != null) {
                    
                    imageDisplayLabel.setIcon(previousIcon);
                    frame.revalidate();
                    frame.repaint();
                
                }
            
            }
        
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(cropButton);
        buttonPanel.add(rotateButton);
        buttonPanel.add(blurButton);
        buttonPanel.add(brightnessButton);
        buttonPanel.add(monochromeButton);
        buttonPanel.add(resizeButton);
        buttonPanel.add(drawButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(blurIntensityLabel);
        buttonPanel.add(blurIntensitySlider);

        imagePanel.add(imageDisplayLabel, BorderLayout.CENTER);
        imagePanel.add(buttonPanel, BorderLayout.SOUTH);

        JLabel closeButton = new JLabel(new ImageIcon("H:\\Year 2 Semester 1\\WIX1002\\PhotoEditor\\icons\\cross.png"));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                int tabIndex = tabbedPane.indexOfComponent(imagePanel);
                if (tabIndex != -1) {
                    
                    tabbedPane.removeTabAt(tabIndex);
                    
                }
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });


        JPanel tabPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(selectedFile.getName());

        tabPanel.add(titleLabel, BorderLayout.WEST);
        tabPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        tabPanel.add(closeButton, BorderLayout.EAST);

        tabbedPane.addTab(null, imagePanel);
        int lastIndex = tabbedPane.getTabCount() - 1;
        tabbedPane.setTabComponentAt(lastIndex, tabPanel);

        tabbedPane.setTitleAt(lastIndex, selectedFile.getName());
        
    }
    
    private static BufferedImage loadImage(String filePath) {
        
        BufferedImage img = null;
        
        try {
           
            img = ImageIO.read(new File(filePath));
            
        } catch (IOException e) {
            
            e.printStackTrace();
            
        }
        
        return img;
    
    }
    
    private void applyBrightness(float brightnessFactor) {
       
        if (originalImage != null) {
            
            BufferedImage adjustedImage = brightnessAdjustment.adjustBrightness(originalImage, brightnessFactor);
            if (adjustedImage != null) {
                
                ImageIcon adjustedIcon = new ImageIcon(adjustedImage);
                imageDisplayLabel.setIcon(adjustedIcon);
                
            } else {
                
            }
        }
    }
    
    private void applyMonochrome() {
       
        if (originalImage != null) {
           
            BufferedImage monochromeImage = monochrome.convertToMonochrome(originalImage);
            if (monochromeImage != null) {
               
                ImageIcon monochromeIcon = new ImageIcon(monochromeImage);
                imageDisplayLabel.setIcon(monochromeIcon);
                
            } else {
                // Handle conversion error, display a message or take appropriate action
            }
        }
    }
    
    private void drawLines(Point startPoint, Point endPoint) {
   
        if (drawingImage != null) {
        
            Graphics2D g2d = drawingImage.createGraphics();
            g2d.setColor(Color.BLACK); 
            g2d.setStroke(new BasicStroke(5)); 
            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            g2d.dispose();
        
        }
    
    }
    
    
    private void showResetConfirmation() {
        
        boolean confirmed = Reset.showConfirmationDialog();
        if (confirmed) {
           
            resetImage();
            
        }
        
    }

    private void resetImage() {
        
        if (originalImage != null) {
            
            originalImage = Reset.resetImage(originalImage);
            ImageIcon resetIcon = new ImageIcon(originalImage);
            imageDisplayLabel.setIcon(resetIcon);
        
        }
        
    }
    
    private void saveImage() {
    
        if (originalImage != null) {
        
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image");
            
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Images", "png");
            FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg");
            FileNameExtensionFilter svgFilter = new FileNameExtensionFilter("SVG Images", "svg");
            
            fileChooser.addChoosableFileFilter(pngFilter);
            
            fileChooser.addChoosableFileFilter(jpegFilter);
            
            fileChooser.addChoosableFileFilter(svgFilter);
            
            int userChoice = fileChooser.showSaveDialog(frame);
            
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                
                File outputFile = fileChooser.getSelectedFile();
                FileFilter selectedFilter = fileChooser.getFileFilter();
                String format = "";
                
                if (selectedFilter.equals(pngFilter)) {
                    
                    format = "PNG";
                
                } else if (selectedFilter.equals(jpegFilter)) {
                    
                    format = "JPEG";
                
                } else if (selectedFilter.equals(svgFilter)) {
                    
                    format = "SVG";
                
                }
                
                String fileName = outputFile.getName();
                
                if (!fileName.toLowerCase().endsWith("." + format.toLowerCase())) {
                    
                    outputFile = new File(outputFile.getAbsolutePath() + "." + format.toLowerCase());
                    
            }
                
                BufferedImage combinedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = combinedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, null);
                if (drawingImage != null) {
                    
                    g2d.drawImage(drawingImage, 0, 0, null);
                
                }
                
                g2d.dispose();
                
                try {
                    
                    ImageIO.write(combinedImage, format, outputFile);
                    JOptionPane.showMessageDialog(frame, "Image saved successfully!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
                
                } catch (IOException e) {
                    
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to save the image.", "Save Error", JOptionPane.ERROR_MESSAGE);
                
                }
            
            }
        
        }
    
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new version1();
        new RotateImage();
        new CropImage();
        new ResizeImage();
        new UndoAdjustment();
        new BlurAdjustment();
        new Brightness();
        new Monochrome();
        new Reset();
       new SaveImage();
       new DrawImage();
    });
    }

}
