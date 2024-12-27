package com.github.puzzle.setup.tabs;

import com.github.puzzle.setup.Main;
import com.github.puzzle.setup.profile.SteppedInstallProfile;
import com.github.puzzle.setup.util.DirChooser;
import com.github.puzzle.setup.util.GUIUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class InstallTab extends JPanel {

    SteppedInstallProfile profile;

    final JLabel title;
    final JLabel logo;
    final JLabel version;

    final JTextField dir;
    final JButton dirSelect;

    final JButton install;

    final JFileChooser fileChooser = new DirChooser();

    private String pathPopup = "path/to/server/dir";

    public InstallTab(
            URL profileUrl,
            ImageIcon icon,
            int iconY
    ) {
        super(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
            }
        });

        try {
            profile = SteppedInstallProfile.fromURL(profileUrl);
        } catch (Exception ignore) {}

        setBackground(Color.GRAY);

        title = new JLabel(profile.display_name);
        logo = new JLabel(icon);
        version = new JLabel(profile.version);

        dir = new JTextField(15) {
            {
                setText(pathPopup);
                setForeground(Color.GRAY);

                addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (getText().equals(pathPopup)) {
                            setText("");
                            setForeground(Color.WHITE);
                        }
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getText().isEmpty()) {
                            setForeground(Color.GRAY);
                            setText(pathPopup);
                        }
                    }
                });

                getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        warn();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        warn();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        warn();
                    }

                    public void warn() {
                        install.setEnabled(new File(getText()).exists());
                    }
                });
            }
        };

        dirSelect = new JButton("Choose Directory") {
            {
                setActionCommand("choose_jar");

                addActionListener((a) -> {
                    if (Objects.equals(a.getActionCommand(), "choose_jar")) {
                        fileChooser.showSaveDialog(null);
                    }
                });

                fileChooser.addActionListener(a -> {
                    if (a.getActionCommand().equals("ApproveSelection")) {
                        dir.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                });
            }
        };

        install = new JButton("Install/Upgrade") {
            {
                setActionCommand("install");
                setEnabled(false);

                addActionListener((a) -> {
                    if (Objects.equals(a.getActionCommand(), "install")) {
                        Map<String, String> constants = new HashMap<>();
                        constants.put("basePath", dir.getText().replaceAll("\\\\", "/"));
                        profile.doSteps(constants);
                    }
                });
            }
        };

        GUIUtil.add(this, logo, title, dir, dirSelect, version, install);

        logo.setBounds(
                Main.window.centerX - (logo.getIcon().getIconWidth() / 2),
                iconY,
                logo.getIcon().getIconWidth(),
                logo.getIcon().getIconHeight()
        );

        GUIUtil.setFont(title, 20);
        GUIUtil.setFont(version, 13);

        int y = 40;

        y -= setCentered(title, y);
        y -= setCentered(version, y);
        y -= setCentered(dir, y);
        y -= setCentered(dirSelect, y, dir.getPreferredSize().width);
        setCentered(install, y, dir.getPreferredSize().width);
    }

    public void setPathPopup(String pathPopup) {
        this.pathPopup = pathPopup;
        dir.setText(pathPopup);
    }

    public InstallTab setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public InstallTab setLogo(ImageIcon icon) {
        logo.setIcon(icon);
        return this;
    }

    public InstallTab setVersion(String ver) {
        version.setText(ver);
        return this;
    }

    protected int setCentered(Component comp, int y, int width) {
        comp.setBounds(Main.window.centerX - (width / 2), (Main.window.centerY - 10) - y, width, comp.getPreferredSize().height);
        return comp.getPreferredSize().height + 2;
    }

    protected int setCentered(Component comp, int y) {
        comp.setBounds(Main.window.centerX - (comp.getPreferredSize().width / 2), (Main.window.centerY - 10) - y, comp.getPreferredSize().width, comp.getPreferredSize().height);
        return comp.getPreferredSize().height + 2;
    }

}
