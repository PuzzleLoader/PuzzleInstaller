package com.github.puzzle.setup.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class DirChooser extends JFileChooser {

    public DirChooser() {
        setDialogTitle("Choose Dir");
        setApproveButtonText("Select");
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "All Dirs";
            }
        });
        setMultiSelectionEnabled(false);
        setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    }

    @Override
    public void approveSelection() {
        if (getSelectedFile().isDirectory()) super.approveSelection();
    }
}
