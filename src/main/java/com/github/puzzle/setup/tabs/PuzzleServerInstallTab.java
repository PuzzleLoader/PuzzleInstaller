package com.github.puzzle.setup.tabs;

import com.github.puzzle.setup.Main;
import com.github.puzzle.setup.util.IconUtil;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;

public class PuzzleServerInstallTab extends InstallTab {

    static URL icon_url = Objects.requireNonNull(Main.class.getResource("/assets/icons/puzzle_logo.png"));
    static final ImageIcon LOGO = IconUtil.fromURLScaled(icon_url, 128, 128);

    public PuzzleServerInstallTab() {
        super(
                Objects.requireNonNull(Main.class.getResource("/assets/puzzle_server_install_profile.json")),
                LOGO,
                90
        );
    }
}
