package com.github.puzzle.setup.tabs;

import com.github.puzzle.setup.Main;
import com.github.puzzle.setup.util.IconUtil;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;

public class ParadoxInstallTab extends InstallTab {

    static URL icon_url = Objects.requireNonNull(Main.class.getResource("/assets/icons/paradox_logo.png"));
    static final ImageIcon LOGO = IconUtil.fromURLScaled(icon_url, 166, 166);

    public ParadoxInstallTab() {
        super(
                Objects.requireNonNull(Main.class.getResource("/assets/paradox_install_profile.json")),
                LOGO,
                65
        );
    }
}
