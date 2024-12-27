package com.github.puzzle.setup.profile;

import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SteppedInstallProfile {

    public String name;
    public String display_name;
    public String version;

    Map<String, String> constants = new HashMap<>();
    Map<String, JsonArray> blocks = new HashMap<>();

    JsonArray steps;
    MavenProvider mavenProvider = new MavenProvider();
    private JsonObject o;

    public static SteppedInstallProfile fromURL(URL url) throws IOException {
        InputStream stream = url.openStream();
        String s = new String(stream.readAllBytes());
        stream.close();
        return fromString(s);
    }

    public static SteppedInstallProfile fromString(String s) {
        JsonObject o = JsonObject.readHjson(s).asObject();
        SteppedInstallProfile profile = new SteppedInstallProfile();
        JsonObject constants = o.get("constants").asObject();
        for (String k : constants.names()) profile.constants.put(k, constants.get(k).asString());
        profile.steps = o.get("steps").asArray();
        if (o.get("blocks") != null) {
            for (String n : o.get("blocks").asObject().names()) {
                profile.blocks.put(n, o.get("blocks").asObject().get(n).asArray());
            }
        }

        profile.display_name = profile.format(o.getString("display_name", "Generic Profile"));
        profile.version = profile.format(o.getString("version", "idk"));
        profile.name = profile.format(o.getString("name", "generic_profile"));
        profile.o = o;

        return profile;
    }

    public void doSteps(Map<String, String> constants) {
        System.out.println("-- Running Profile with Name: \"" + name + "\" --");
        for (JsonValue v : steps) Step.execute(this, v.asObject(), constants);
        refreshConstants();
    }

    public String getConstant(String s) {
        return constants.get(s);
    }

    public void refreshConstants() {
        this.constants.clear();

        JsonObject constants = o.get("constants").asObject();
        for (String k : constants.names()) this.constants.put(k, constants.get(k).asString());
    }

    public static String format2(String s, Map<String, String> constants) {
        String n = s;
        for (String k : constants.keySet()) {
            n = n.replaceAll("#" + k, constants.get(k));
        }
        return n;
    }

    public String format(String s) {
        String n = s;
        for (String k : constants.keySet()) {
            n = n.replaceAll("\\@" + k, constants.get(k));
        }
        return n;
    }
}
