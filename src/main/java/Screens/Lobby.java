package Screens;

import UI.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.opengl.GL20;
import core.Boot;

import static Screens.LobbyUsername.username;
import static Screens.LobbyCreate.lobbyName;

public class Lobby extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;

    int tick;

    Button buttonCreate;
    Button buttonReturn;
    Button buttonStart;
    Button buttonRefresh;


    public Lobby(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");


        this.tick = 0;
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonCreate = new Button("Create lobby", 660, 140, 600, 100, batch, font);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, font);
        this.buttonRefresh = new Button("R", 650, 950, 100, 100, batch, font);
        this.buttonStart = new Button("Start", 990, 950, 300, 100, batch, font);
    }

    private void start(){
        Boot.INSTANCE.setScreen(new GameScreen(camera));
    }


    public void update() throws InterruptedException {
        buttonCreate.update();
        buttonReturn.update();
        buttonStart.update();
        buttonRefresh.update();

        batch.setProjectionMatrix(camera.combined);

        inputHandle();
    }

    public void inputHandle() throws InterruptedException {
        if (buttonReturn.isClicked()) {
            Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
        if (buttonStart.isClicked()) {
            start();
        }
        if(buttonReturn.isClicked()){
            Boot.INSTANCE.setScreen(Boot.lobbyMainScreen);
        }
    }


    @Override
    public void render(float delta) {
        try {
            update();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        backgroundRender();
        buttonsRender();
        playerInfoRender();

        batch.end();
    }

    private void playerInfoRender() {
        font.draw(batch, "Username: " + username, 10, Gdx.graphics.getHeight() / 10.0f);
    }

    public void buttonsRender() {
        buttonStart.render();
        buttonRefresh.render();
        buttonReturn.render();
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());
    }

}
