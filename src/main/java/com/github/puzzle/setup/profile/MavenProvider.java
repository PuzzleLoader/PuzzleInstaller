package com.github.puzzle.setup.profile;

import org.hjson.JsonArray;
import org.hjson.JsonObject;

import java.net.URL;

public class MavenProvider {
        String[] repos;

        SteppedInstallProfile profile;

        public void read(SteppedInstallProfile profile, JsonObject obj) {
            this.profile = profile;

            JsonArray array = obj.get("repos").asArray();
            repos = new String[array.size()];
            for (int i = 0; i < array.size(); i++) repos[i] = profile.format(array.get(i).asString());
        }

        public URL getURL(String mavenDep) {
            for (String base : repos) {
                try {
                    URL u = new URL(base + toURL(profile.format(mavenDep)));
                    if (u.getContent() != null) return u;
                } catch (Exception e) {
                    System.out.println(e.fillInStackTrace());
                    continue;
                }
            }
            return null;
        }

        private String toURL(String s) {
            StringBuilder builder = new StringBuilder();

            String[] parts = s.split(":");
            String extra = "";
            if (parts.length == 4) extra = "-" + parts[3];

            builder.append(parts[0].replaceAll("\\.", "/"))
                    .append("/")
                    .append(parts[1])
                    .append("/")
                    .append(parts[2])
                    .append("/")
                    .append(parts[1] + "-" + parts[2] + extra + ".jar");

            return builder.toString();
        }
    }