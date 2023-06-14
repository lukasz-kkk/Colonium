package core;

import Screens.*;
import Utils.SoundManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lwjgl.system.CallbackI;

public class Boot extends Game {
    public Game game;
    public static SoundManager sm;
    public static Boot INSTANCE;
    private int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    public static float scaleX = 0;
    public static float scaleY = 0;

    public static MenuScreen menuScreen;
    public static LobbyMainScreen lobbyMainScreen;
    public static LobbyUsername lobbyUsername;
    public static LobbyCreate lobbyCreate;
    public static Lobby lobby;
    public static GameScreen gameScreen;
    public Boot() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        this.screenHeight = 1020;
        this.screenWidth = 1920;
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
        sm = new SoundManager();
        menuScreen = new MenuScreen(orthographicCamera);
        lobbyMainScreen = new LobbyMainScreen(orthographicCamera);
        lobbyUsername = new LobbyUsername(orthographicCamera);
        lobbyCreate = new LobbyCreate(orthographicCamera);
        lobby = new Lobby(orthographicCamera);
        gameScreen = new GameScreen(orthographicCamera);
        //setScreen(new GameScreen(orthographicCamera));
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();

        //window.maximizeWindow();
       //window.restoreWindow();
        window.setPosition(0, 30);

        setScreen(menuScreen);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void resize(int width, int height) {
        //scaleX = (float) width / screenWidth;
        //scaleY = (float) height / screenHeight;

        //this.screenHeight = Gdx.graphics.getHeight();
        //this.screenWidth = Gdx.graphics.getWidth();

        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
    }
}
