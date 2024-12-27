package com.github.puzzle.setup.gui;

import com.github.puzzle.setup.Main;
import com.github.puzzle.setup.util.IconUtil;

import javax.swing.*;
import java.util.Objects;

public class Resources {

    public static final ImageIcon EXIT_ICON = IconUtil.fromURLSVG(Objects.requireNonNull(Main.class.getResource("/assets/icons/exit.svg")));
    public static final ImageIcon MINIMIZE_ICON = IconUtil.fromURLSVG(Objects.requireNonNull(Main.class.getResource("/assets/icons/minimize.svg")));
    public static final ImageIcon MAXIMIZE_ICON = IconUtil.fromURLSVG(Objects.requireNonNull(Main.class.getResource("/assets/icons/maximize.svg")));

}
