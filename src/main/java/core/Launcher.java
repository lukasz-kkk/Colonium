package core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

public class Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 16);
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Colonium");

        config.setMaximized(true);

        config.setInitialBackgroundColor(new Color(0.5f, 0.5f, 0.6f, 0));

        new Lwjgl3Application(new Boot(), config);
    }
}
