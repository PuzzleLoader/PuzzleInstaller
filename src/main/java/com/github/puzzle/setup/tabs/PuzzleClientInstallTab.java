package com.github.puzzle.setup.tabs;

import com.github.puzzle.setup.Main;
import com.github.puzzle.setup.util.IconUtil;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;

public class PuzzleClientInstallTab extends InstallTab {

    static URL icon_url = Objects.requireNonNull(Main.class.getResource("/assets/icons/puzzle_logo.png"));
    static final ImageIcon LOGO = IconUtil.fromURLScaled(icon_url, 128, 128);

    public PuzzleClientInstallTab() {
        super(
                Objects.requireNonNull(Main.class.getResource("/assets/puzzle_client_install_profile.json")),
                LOGO,
                90
        );

        setPathPopup("path/to/client/dir");
    }
}
