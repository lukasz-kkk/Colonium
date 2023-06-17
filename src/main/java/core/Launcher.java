package core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import java.io.IOException;
import java.util.Arrays;

public class Launcher {
    public static Client client;
    public static Client getClient(){
        return client;
    }

    public static boolean test = false;
    public static String randomName;
    public static Lwjgl3ApplicationConfiguration config;

    public static void main(String[] args) throws IOException, InterruptedException {
        //Connection
        if(args.length > 0) {
            test = true;
            randomName = args[0];
        }


        client = new Client();
        client.start();
        config = new Lwjgl3ApplicationConfiguration();
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 16);
        config.setIdleFPS(60);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setTitle("Colonium");

        //config.setMaximized(true);
        config.setWindowedMode(1920, 1020);
        config.setWindowPosition(0, 30);

        config.setInitialBackgroundColor(new Color(0.3f, 0.3f, 0.3f, 0));
        Boot boot = new Boot();
        //config.setMaximized(true);
        new Lwjgl3Application(boot, config);
    }
}
