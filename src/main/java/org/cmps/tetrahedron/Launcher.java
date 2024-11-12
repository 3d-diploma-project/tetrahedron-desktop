package org.cmps.tetrahedron;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.cmps.tetrahedron.config.WindowProperties;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Don't know why, but it's required to have a main method in a separate file.
 * We can't have it inside of class which extends {@link Application}.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Launcher {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        JFrame frame = new JFrame("AWT test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(1000, 800));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        frame.setPreferredSize(screenSize);
        JLayeredPane pane = frame.getLayeredPane();

        GLData data = new GLData();
        data.majorVersion = 3;
        data.minorVersion = 2;

        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setSize(screenSize);
        Scene scene = TetrahedronApp.start();
        fxPanel.setScene(scene);

        AWTGLCanvas canvas = new ModelCanvas(data, scene);

        frame.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                Dimension size = c.getSize();
                fxPanel.setSize(size);
                canvas.setSize((int) (size.getWidth() - 500), (int) (size.getHeight() - 300));
                canvas.validate();

                WindowProperties.setWidth((int) size.getWidth() * 2);
                WindowProperties.setHeight((int) size.getHeight() * 2);
            }
        });

        canvas.setLocation(250, 150);
        canvas.setSize((int) (width - 500), (int) (height - 300));

        //frame.add(canvas);
        pane.add(canvas, Integer.valueOf(1));
        pane.add(fxPanel, Integer.valueOf(10));

        frame.pack();
        frame.setVisible(true);
        frame.transferFocus();

        while (true) {
            if (!canvas.isValid()) {
                GL.setCapabilities(null);
                return;
            }
            canvas.render();
        }

    }
}
