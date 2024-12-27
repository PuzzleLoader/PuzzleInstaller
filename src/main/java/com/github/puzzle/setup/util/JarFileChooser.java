package com.github.puzzle.setup.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JarFileChooser extends JFileChooser {

    public JarFileChooser() {
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setDialogTitle("Choose Jar");
        setApproveButtonText("Select");
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return "*.jar";
            }
        });
        setMultiSelectionEnabled(false);
    }

}
