package core;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        //Connection
        if(args.length > 0) {
            test = true;
            randomName = args[0];
        }

        client = new Client();
        client.start();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 16);
        config.setIdleFPS(60);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setTitle("Colonium");

        config.setMaximized(true);

        config.setInitialBackgroundColor(new Color(0.5f, 0.5f, 0.6f, 0));
        new Lwjgl3Application(new Boot(), config);
    }
}
