package com.github.puzzle.setup.util;

import com.formdev.flatlaf.ui.FlatTitlePane;
import com.github.puzzle.setup.gui.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.Objects;

public class WindowUtil {

    public static FlatTitlePane pane;
    public static JPanel leftPanel;
    public static Container rightPanel;

    public static void initFrame(JFrame frame, boolean addMaximize, boolean addMinimize) {
        removeRightPanelButtons(frame.getRootPane());
        try {
            findLeftPanel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        WindowUtil.rightPanel.add(GUIUtil.titleButton(Resources.EXIT_ICON, "EXIT", (l) -> {
            if (Objects.equals(l.getActionCommand(), "EXIT")) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }));

        if (addMaximize)
            WindowUtil.rightPanel.add(GUIUtil.titleButton(Resources.MAXIMIZE_ICON, "MAXIMIZE", (l) -> {
                if (l.getActionCommand().equals("MAXIMIZE")) frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            }));

        if (addMinimize)
            WindowUtil.rightPanel.add(GUIUtil.titleButton(Resources.MINIMIZE_ICON, "MINIMIZE", (l) -> {
                if (l.getActionCommand().equals("MINIMIZE")) frame.setExtendedState(Frame.ICONIFIED);
            }));
    }

    public static void removeRightPanelButtons(Component comp) {
        if (comp instanceof FlatTitlePane) {
            pane = (FlatTitlePane) comp;
        }
        if(comp instanceof JButton) {
            String accName = comp.getAccessibleContext().getAccessibleName();
            rightPanel = comp.getParent();
            if(accName.equals("Maximize")|| accName.equals("Iconify")||
                    accName.equals("Close")) comp.getParent().remove(comp);
        }
        if (comp instanceof Container) {
            Component[] comps = ((Container)comp).getComponents();
            for (Component component : comps) {
                removeRightPanelButtons(component);
            }
        }
    }

    public static void findLeftPanel() throws Exception {
        Field leftPanelField = FlatTitlePane.class.getDeclaredField("leftPanel");
        leftPanelField.setAccessible(true);
        leftPanel = (JPanel) leftPanelField.get(pane);
    }

}
