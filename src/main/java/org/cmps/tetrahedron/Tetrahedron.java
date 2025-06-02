package org.cmps.tetrahedron;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.cmps.tetrahedron.view.ModelFilesPicker;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.controller.SceneController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Creates a program window and inits all components (LWJGL and JavaFX parts).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Tetrahedron {

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 800;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetrahedron");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        maximizeWindow(frame);
        setIcon(frame);

        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setScene(SceneController.getScene());

        fxPanel.addMouseListener(MouseController.getInstance());
        fxPanel.addMouseWheelListener(MouseController.getInstance());
        fxPanel.addMouseMotionListener(MouseController.getInstance());

        frame.add(fxPanel);

        frame.pack();
        frame.setVisible(true);
        frame.transferFocus();

        Platform.runLater(ModelFilesPicker::openDialogWindow);
    }

    private static void setIcon(JFrame frame) {
        try {
            InputStream inputStream = Tetrahedron.class.getClassLoader().getResourceAsStream("logo.png");
            if (inputStream != null) {
                BufferedImage iconImage = ImageIO.read(inputStream);
                frame.setIconImage(iconImage);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void maximizeWindow(JFrame frame) {
        if (org.lwjgl.system.Platform.get().equals(org.lwjgl.system.Platform.MACOSX)) {
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            frame.setPreferredSize(new Dimension(width, height));
        }
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
