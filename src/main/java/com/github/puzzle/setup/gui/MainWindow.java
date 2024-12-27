package com.github.puzzle.setup.gui;

import com.github.puzzle.setup.util.GUIUtil;
import com.github.puzzle.setup.util.WindowUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class MainWindow {

    public JFrame frame = new JFrame();

    public int sizeX, sizeY, centerX, centerY;

    public MainWindow() {
        frame.setUndecorated(true);

        setTitle("Base Window")
                .setSize(400, 400)
                .initBase();
    }

    private MainWindow initBase() {
        frame.getRootPane().setBorder(new LineBorder(Color.DARK_GRAY, 1, false));
        frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WindowUtil.initFrame(frame, false, true);

        return this;
    }

    public MainWindow addResizeFix() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Point p = frame.getLocation();
                frame.setSize(sizeX, sizeY);
                frame.setShape(new RoundRectangle2D.Float(0, 0, sizeX, sizeY, 15, 15));
                frame.setLocation(p);
            }
        });
        return this;
    }

    public MainWindow setSize(int width, int height) {
        this.sizeX = width;
        this.sizeY = height;
        this.centerX = width / 2;
        this.centerY = height / 2;

        frame.setPreferredSize(new Dimension(width, height));
        frame.setShape(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));

        return this;
    }

    public MainWindow setResizable(boolean resizable) {
        frame.setResizable(resizable);
        return this;
    }

    public MainWindow setTitle(String title) {
        frame.setTitle(title);
        return this;
    }

    public void add(Component... components) {
        GUIUtil.add(frame, components);
    }

    public void setIcon(Image icon) {
        frame.setIconImage(icon);
    }

    public void show() {
        frame.setVisible(true);
    }
}
