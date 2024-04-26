package com.example.project_3.utils;

import java.nio.file.Files;
import java.nio.file.Path;

public class Settings {
    public static final String APP_FILE = "settings/app.properties";
    public static final String DB_FILE = "settings/db.properties";
    public static final String EMAIL_FILE = "settings/email.properties";
    public static final String PERSISTENCE_FILE = "settings/persistence.xml";

    public static void initialize() {
        try {
            Path path = Resource.getPathFromResource("settings", false);
            Files.createDirectories(path);
        } catch (Exception e) {
            Log.error(Settings.class, e.toString());
            return;
        }
        Resource.copyResource(APP_FILE, APP_FILE);
        Resource.copyResource(DB_FILE, DB_FILE);
        Resource.copyResource(EMAIL_FILE, EMAIL_FILE);
        Resource.copyResource(PERSISTENCE_FILE, PERSISTENCE_FILE);
    }
}
