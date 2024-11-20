package org.cmps.tetrahedron;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.cmps.tetrahedron.config.CanvasProperties;
import org.cmps.tetrahedron.config.WindowProperties;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.controller.SceneController;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Creates a program window and inits all components (LWJGL and JavaFX parts).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Launcher {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetrahedron");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(WindowProperties.MIN_WIDTH, WindowProperties.MIN_HEIGHT));
        frame.setPreferredSize(WindowProperties.getLogicalSize());

        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setSize(WindowProperties.getLogicalSize());
        Scene scene = SceneController.getScene();
        fxPanel.setScene(scene);

        GLData data = new GLData();
        data.majorVersion = 3;
        data.minorVersion = 2;

        ModelCanvas canvas = new ModelCanvas(data);
        canvas.setLocation(CanvasProperties.X_SHIFT, CanvasProperties.Y_SHIFT);

        canvas.addMouseListener(MouseController.getInstance());
        canvas.addMouseWheelListener(MouseController.getInstance());
        canvas.addMouseMotionListener(MouseController.getInstance());

        JLayeredPane pane = frame.getLayeredPane();
        pane.add(canvas, Integer.valueOf(10));
        pane.add(fxPanel, Integer.valueOf(1));

        frame.pack();
        frame.setVisible(true);
        frame.transferFocus();
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                Dimension size = c.getSize();

                WindowProperties.setWidth((int) size.getWidth());
                WindowProperties.setHeight((int) size.getHeight());
            }
        });

        while (frame.isEnabled()) {
            if (!canvas.isValid()) {
                canvas.validate();
            }
            canvas.render();

            if (WindowProperties.isChanged()) {
                fxPanel.setSize(WindowProperties.getLogicalSize());
                canvas.setSize(CanvasProperties.getWidth(), CanvasProperties.getHeight());
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
