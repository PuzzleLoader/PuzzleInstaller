package com.github.puzzle.setup.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIUtil {

    public static JButton titleButton(ImageIcon icon, String actionName, ActionListener listener) {
        JButton button = new JButton(icon);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setOpaque(false);
        button.setActionCommand(actionName);
        button.addActionListener(listener);
        button.setBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2));
        return button;
    }

    public static void setFont(Component component, int size) {
        component.setFont(new Font(component.getFont().getName(), Font.BOLD, size));
        component.setForeground(Color.WHITE);
    }

    public static void add(Container c, Component... components) {
        for (Component co : components) c.add(co);
    }

}
