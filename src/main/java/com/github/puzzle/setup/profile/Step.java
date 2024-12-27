package com.github.puzzle.setup.profile;

import com.github.puzzle.setup.Main;
import io.sigpipe.jbsdiff.ui.FileUI;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public abstract class Step {
    public static void doSteps(SteppedInstallProfile profile, JsonArray steps, Map<String, String> constants) {
        for (JsonValue v : steps) Step.execute(profile, v.asObject(), constants);
    }

    static void delete(File f) {
        if (f.isDirectory()) {
            for (File f1 : Objects.requireNonNull(f.listFiles()))
                delete(f1);
        } else {
            f.delete();
        }
    }

    public static void execute(SteppedInstallProfile profile, JsonObject object, Map<String, String> constants) {
        if (object.get("name") != null) System.out.println("--> step: " + object.get("name").asString());
        try {
            switch (object.get("type").asString()) {
                case "maven-repos" -> {
                    profile.mavenProvider = new MavenProvider();
                    profile.mavenProvider.read(profile, object);
                }
                case "maven-install" -> {
                    JsonValue f = object.get("dep");
                    String outDir = SteppedInstallProfile.format2(profile.format(object.get("out").asString()), constants);

                    try {
                        new File(outDir).mkdirs();
                    } catch (Exception ignore) {}

                    if (f.isArray()) {
                        for (JsonValue v : f.asArray()) {
                            System.out.println("Attempting artifact \"" + SteppedInstallProfile.format2(profile.format(v.asString()), constants) + "\"");
                            URL url = profile.mavenProvider.getURL(SteppedInstallProfile.format2(profile.format(v.asString()), constants));
                            if (url == null) {
                                System.out.println("Could not find artifact \"" + SteppedInstallProfile.format2(profile.format(v.asString()), constants) + "\"");
                                continue;
                            }
                            String name = new File(url.getFile()).getName();
                            File file = new File(outDir.endsWith("/") ? outDir + name : outDir + "/" + name);
                            try {
                                file.getParentFile().mkdirs();
                            } catch (Exception ignore) {}
                            System.out.println(file);
                            
                            InputStream s = url.openStream();
                            byte[] bytes = s.readAllBytes();
                            s.close();
                            FileOutputStream stream = new FileOutputStream(file);
                            stream.write(bytes);
                            stream.close();
                        }
                    } else {
                        URL url = profile.mavenProvider.getURL(SteppedInstallProfile.format2(profile.format(f.asString()), constants));
                        if (url == null) {
                            System.out.println("Could not find artifact \"" + SteppedInstallProfile.format2(profile.format(f.asString()), constants) + "\"");
                            return;
                        }
                        String name = new File(url.getFile()).getName();
                        File file = new File(outDir.endsWith("/") ? outDir + name : outDir + "/" + name);
                        try {
                            file.getParentFile().mkdirs();
                        } catch (Exception ignore) {}

                        InputStream s = url.openStream();
                        byte[] bytes = s.readAllBytes();
                        s.close();
                        FileOutputStream stream = new FileOutputStream(file);
                        stream.write(bytes);
                        stream.close();
                    }
                }
                case "format-string" -> {
                    File file = new File(SteppedInstallProfile.format2(profile.format(object.get("file").asString()), constants));

                    FileInputStream inputStream = new FileInputStream(file);
                    String s = new String(inputStream.readAllBytes());
                    inputStream.close();

                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(SteppedInstallProfile.format2(profile.format(s), constants).getBytes(StandardCharsets.UTF_8));
                    stream.close();
                }
                case "replace-all" -> {
                    File file = new File(SteppedInstallProfile.format2(profile.format(object.get("file").asString()), constants));

                    FileInputStream inputStream = new FileInputStream(file);
                    String s = new String(inputStream.readAllBytes());
                    inputStream.close();

                    FileOutputStream stream = new FileOutputStream(file);

                    stream.write(s.replaceAll(object.get("find").asString(), SteppedInstallProfile.format2(profile.format(object.get("new").asString()), constants)).getBytes(StandardCharsets.UTF_8));
                    stream.close();
                }
                case "download" -> {
                    URL url = new URL(profile.format(object.get("url").asString()));
                    File path = new File(SteppedInstallProfile.format2(profile.format(object.get("path").asString()), constants));

                    try {
                        path.getParentFile().mkdirs();
                    } catch (Exception ignore) {}
                    InputStream s = url.openStream();
                    byte[] bytes = s.readAllBytes();
                    s.close();
                    FileOutputStream stream = new FileOutputStream(path);
                    stream.write(bytes);
                    stream.close();
                }
                case "extract-internal" -> {
                    URL from = Main.class.getResource(SteppedInstallProfile.format2(profile.format(object.get("from").asString()), constants));
                    File to = new File(SteppedInstallProfile.format2(profile.format(object.get("to").asString()), constants));

                    try {
                        to.getParentFile().mkdirs();
                    } catch (Exception ignore) {}

                    InputStream s = from.openStream();
                    byte[] bytes = s.readAllBytes();
                    s.close();
                    FileOutputStream stream = new FileOutputStream(to);
                    stream.write(bytes);
                    stream.close();
                }
                case "delete" -> {
                    JsonValue f = object.get("file");
                    if (f.isArray()) {
                        for (JsonValue v : f.asArray()) {
                            File file = new File(SteppedInstallProfile.format2(profile.format(v.asString()), constants));
                            if (file.exists()) delete(file);
                        }
                    } else {
                        File file = new File(SteppedInstallProfile.format2(profile.format(f.asString()), constants));
                        if (file.exists()) delete(file);
                    }
                }
                case "patch" -> {
                    File in = new File(SteppedInstallProfile.format2(profile.format(object.get("in").asString()), constants));
                    File out = new File(SteppedInstallProfile.format2(profile.format(object.get("out").asString()), constants));
                    File patch = new File(SteppedInstallProfile.format2(profile.format(object.get("patch").asString()), constants));
                    System.setProperty("jbsdiff.compressor", object.get("compressor").asString());

                    FileUI.patch(in, out, patch);
                }
                case "diff" -> {
                    File in = new File(SteppedInstallProfile.format2(profile.format(object.get("in").asString()), constants));
                    File out = new File(SteppedInstallProfile.format2(profile.format(object.get("out").asString()), constants));
                    File patch = new File(SteppedInstallProfile.format2(profile.format(object.get("patch").asString()), constants));

                    FileUI.diff(in, out, patch, object.get("compression").asString());
                }
                case "block-execute" -> {
                    String name = SteppedInstallProfile.format2(profile.format(object.get("block").asString()), constants);
                    if (profile.blocks.get(name) == null) System.out.println("Block \"" + name + "\" does not exist!!!");
                    Step.doSteps(profile, profile.blocks.get(name), constants);
                }
                case "block-execute-conditional" -> {
                    String cond_a = SteppedInstallProfile.format2(profile.format(object.get("a").asString()), constants);
                    String cond_b = SteppedInstallProfile.format2(profile.format(object.get("b").asString()), constants);

                    String name;

                    if (Objects.equals(cond_a, cond_b))
                        name = SteppedInstallProfile.format2(profile.format(object.get("block_a").asString()), constants);
                    else {
                        if (object.get("block_b") == null) return;
                        name = SteppedInstallProfile.format2(profile.format(object.get("block_b").asString()), constants);
                    }
                    System.out.println(cond_a + " " + cond_b + " " + cond_a.equals(cond_b) + " " + name);


                    if (profile.blocks.get(name) == null) {
                        System.out.println("Block \"" + name + "\" does not exist!!!");
                        return;
                    }
                    Step.doSteps(profile, profile.blocks.get(name), constants);
                }
                case "file-exist-conditional" -> {
                    File file = new File(SteppedInstallProfile.format2(profile.format(object.get("a").asString()), constants));

                    boolean cond = file.exists();

                    String name;

                    if (object.get("b") != null) {
                        File file2 = new File(SteppedInstallProfile.format2(profile.format(object.get("b").asString()), constants));
                        cond = file2.exists() || file.exists();
                    }

                    if (cond)
                        name = SteppedInstallProfile.format2(profile.format(object.get("block_a").asString()), constants);
                    else {
                        if (object.get("block_b") == null) return;
                        name = SteppedInstallProfile.format2(profile.format(object.get("block_b").asString()), constants);
                    }


                    if (profile.blocks.get(name) == null) {
                        System.out.println("Block \"" + name + "\" does not exist!!!");
                        return;
                    }
                    Step.doSteps(profile, profile.blocks.get(name), constants);
                }
                case "create-popup" -> {
                    String contents = SteppedInstallProfile.format2(profile.format(object.get("message").asString()), constants);

                    JOptionPane.showMessageDialog(Main.window.frame, contents);
                }
                case "set-const" -> {
                    String name = SteppedInstallProfile.format2(profile.format(object.get("const").asString()), constants);
                    String value = SteppedInstallProfile.format2(profile.format(object.get("value").asString()), constants);

                    profile.constants.put(name, value);
                }
                case "write-file" -> {
                    String name = SteppedInstallProfile.format2(profile.format(object.get("fname").asString()), constants);
                    String value = SteppedInstallProfile.format2(profile.format(object.get("value").asString()), constants);

                    File f = new File(name);
                    try {
                        f.getParentFile().mkdirs();
                    } catch (Exception ignore) {}

                    FileOutputStream stream = new FileOutputStream(f);
                    stream.write(value.getBytes(StandardCharsets.UTF_8));
                    stream.close();
                }
                default -> throw new RuntimeException("Unknown step type: " + object.get("type").asString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
