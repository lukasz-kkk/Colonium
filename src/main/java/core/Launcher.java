package core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import java.io.IOException;

public class Launcher {
    public static Client client;

    public static Client getClient(){
        return client;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //Connection
        client = new Client();
        client.start();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Colonium");

        //config.setWindowedMode(1920, 1080);
    config.setMaximized(true);
        config.setInitialBackgroundColor(new Color(0.5f, 0.5f, 0.6f, 0));
        new Lwjgl3Application(new Boot(), config);
    }
}
