package core;

import Screens.*;
import Utils.SoundManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lwjgl.system.CallbackI;

public class Boot extends Game {
    public Game game;
    public static SoundManager sm;
    public static Boot INSTANCE;
    private int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    public static MenuScreen menuScreen;
    public static LobbyMainScreen lobbyMainScreen;
    public static LobbyUsername lobbyUsername;
    public static LobbyCreate lobbyCreate;
    public static Lobby lobby;
    public Boot() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
        sm = new SoundManager();
        menuScreen = new MenuScreen(orthographicCamera);
        lobbyMainScreen = new LobbyMainScreen(orthographicCamera);
        lobbyUsername = new LobbyUsername(orthographicCamera);
        lobbyCreate = new LobbyCreate(orthographicCamera);
        lobby = new Lobby(orthographicCamera);

        //setScreen(new GameScreen(orthographicCamera));
        setScreen(new MenuScreen(orthographicCamera));
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void resize(int width, int height) {
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
    }
}
