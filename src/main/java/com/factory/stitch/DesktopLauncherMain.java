package com.factory.stitch;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

public final class DesktopLauncherMain {

    private static ConfigurableApplicationContext context;

    private DesktopLauncherMain() {
    }

    public static void launchDesktop(String[] args) {
        String[] serverArgs = Arrays.stream(args)
                .filter(arg -> !"--desktop".equalsIgnoreCase(arg))
                .toArray(String[]::new);

        context = new SpringApplicationBuilder(Main.class).run(serverArgs);
        DesktopWebViewLauncher.setApplicationContext(context);
        Application.launch(DesktopWebViewLauncher.class, serverArgs);
    }

    static ConfigurableApplicationContext getContext() {
        return context;
    }
}
