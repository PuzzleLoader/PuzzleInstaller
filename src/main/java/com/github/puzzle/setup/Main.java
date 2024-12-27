package com.github.puzzle.setup;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.github.puzzle.setup.gui.MainWindow;
import com.github.puzzle.setup.tabs.ParadoxInstallTab;
import com.github.puzzle.setup.tabs.PuzzleClientInstallTab;
import com.github.puzzle.setup.tabs.PuzzleServerInstallTab;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static MainWindow window;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatMacDarkLaf.setup();

            window = new MainWindow();
            window.setTitle("Puzzle installer")
                    .setSize(400, 500)
                    .addResizeFix()
                    .setResizable(false);

            var installerTabs = new JTabbedPane();
            installerTabs.addTab("Puzzle Client", new PuzzleClientInstallTab());
            installerTabs.addTab("Puzzle Server", new PuzzleServerInstallTab());
            installerTabs.addTab("Paradox", new ParadoxInstallTab());
            window.add(installerTabs);

            window.setIcon(Toolkit.getDefaultToolkit().createImage(Main.class.getResource("/assets/icons/icon.png")));
            window.show();
        });
    }

}
