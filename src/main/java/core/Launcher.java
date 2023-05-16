package core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

public class Launcher {
    public static void main(String[] args){
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Colonium");

        config.setWindowedMode(1280, 720);

        config.setInitialBackgroundColor(new Color(0.5f, 0.5f, 0.6f, 0));

        new Lwjgl3Application(new Boot(), config);
    }
}
