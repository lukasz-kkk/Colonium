package core;

import Screens.LobbyCreate;
import Screens.LobbyMainScreen;
import Screens.LobbyUsername;
import Screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Boot extends Game {
    public Game game;
    public static Boot INSTANCE;
    private int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    public static MenuScreen menuScreen;
    public static LobbyMainScreen lobbyMainScreen;
    public static LobbyUsername lobbyUsername;
    public static LobbyCreate lobbyCreate;
    public Boot() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);

        menuScreen = new MenuScreen(orthographicCamera);
        lobbyMainScreen = new LobbyMainScreen(orthographicCamera);
        lobbyUsername = new LobbyUsername(orthographicCamera);
        lobbyCreate = new LobbyCreate(orthographicCamera);

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
