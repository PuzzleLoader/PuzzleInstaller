package com.github.puzzle.setup.util;

import com.formdev.flatlaf.extras.FlatSVGUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtil {

    public static ImageIcon fromURLSVG(URL url) {
        return new ImageIcon(FlatSVGUtils.svg2image(url, 1));
    }

    public static ImageIcon fromURLScaledSVG(URL url, int width, int height) {

        return new ImageIcon(FlatSVGUtils.svg2image(url, 1).getScaledInstance(width, height, 0));
    }

    public static ImageIcon fromURL(URL url) {
        return new ImageIcon(url);
    }

    public static ImageIcon fromImage(Image image) {
        return new ImageIcon(image);
    }

    public static ImageIcon fromURLScaled(URL url, int width, int height) {

        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(url).getScaledInstance(width, height, 0));
    }

    public static ImageIcon fromImageScaled(Image image, int width, int height) {
        return new ImageIcon(image.getScaledInstance(width, height, 0));
    }

}
