package com.example.project_3;

import com.example.project_3.utils.Resource;
import com.example.project_3.utils.Settings;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MhplProject3Application {
    public static void main(String[] args) {
        initialize();
        var application = new SpringApplicationBuilder()
                .sources(MhplProject3Application.class)
                .headless(true)
                .properties(Resource.loadProperties(Settings.APP_FILE, false));
        application.run(args);
    }

    public static void initialize() {
        Settings.initialize();
    }
}
