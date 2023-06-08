package Screens;

import Utils.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import core.Boot;
import core.Client;
import core.Launcher;
import org.lwjgl.opengl.GL20;

public class MenuScreen extends ScreenAdapter {

    Client client = Launcher.getClient();
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private final BitmapFont font;

    private Texture buttonStartTexture;
    private Texture buttonQuitTexture;

    private final Texture buttonTouchTexture;
    private final Texture buttonIdleTexture;
    private final Texture backgroundTexture;
    private final Texture logoTexture;

    private final int BUTTON_WIDTH = 600;
    private final int BUTTON_HEIGHT = 100;

    SoundManager sm = new SoundManager();

    public MenuScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        this.buttonIdleTexture = new Texture("UI_elements/button_idle.png");
        this.buttonTouchTexture = new Texture("UI_elements/button_touch.png");
        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);
    }

    public void update() {
        buttonStartTexture = buttonIdleTexture;
        buttonQuitTexture = buttonIdleTexture;

        world.step(1 / 60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        mouseHandle();

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        backgroundRender();
        buttonsRender();

        batch.end();
    }

    public void buttonsRender() {
        batch.draw(buttonStartTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f, BUTTON_WIDTH, BUTTON_HEIGHT);
        batch.draw(buttonQuitTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f - 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        font.draw(batch, "Start game", Boot.INSTANCE.getScreenWidth() / 2f - 110, Boot.INSTANCE.getScreenHeight() / 2f + 70);
        font.draw(batch, "Quit game", Boot.INSTANCE.getScreenWidth() / 2f - 90, Boot.INSTANCE.getScreenHeight() / 2f - 80);
    }

    public void mouseHandle() {
        if (Gdx.input.getX() >= Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f && Gdx.input.getX() <= Boot.INSTANCE.getScreenWidth() / 2f + BUTTON_WIDTH / 2f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f) {
            buttonStartTexture = buttonTouchTexture;
            // start game
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            {
                Boot.INSTANCE.setScreen(Boot.lobbyScreen);
                sm.clickplayer();
            }

        }

        if (Gdx.input.getX() >= Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f && Gdx.input.getX() <= Boot.INSTANCE.getScreenWidth() / 2f + BUTTON_WIDTH / 2f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT + 150 && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f + 150) {
            buttonQuitTexture = buttonTouchTexture;
            // quit game
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            {
                Gdx.app.exit();
                sm.clickplayer();
            }

        }
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());

        // logo
        batch.draw(logoTexture, Boot.INSTANCE.getScreenWidth() / 2f - 350, Boot.INSTANCE.getScreenHeight() / 2f + 300);
    }

}
