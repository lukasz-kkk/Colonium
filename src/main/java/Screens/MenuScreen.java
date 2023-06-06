package Screens;

import UI.Button;
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

    private final Texture backgroundTexture;
    private final Texture logoTexture;

    Button buttonStart;
    Button buttonQuit;

    public MenuScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonStart = new Button("Start game", 660, 550, 600, 100, batch, font);
        this.buttonQuit = new Button("Quit game", 660, 700, 600, 100, batch, font);
    }

    public void update() {
        buttonStart.update();
        buttonQuit.update();

        inputHandle();

        world.step(1 / 60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);
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

    public void inputHandle() {
        if (buttonStart.isClicked()) {
            Boot.INSTANCE.setScreen(Boot.lobbyUsername);
        }
        if (buttonQuit.isClicked()) {
            Gdx.app.exit();
        }
    }

    public void buttonsRender() {
        buttonStart.render();
        buttonQuit.render();
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());
        // logo
        batch.draw(logoTexture, Boot.INSTANCE.getScreenWidth() / 2f - 360, Boot.INSTANCE.getScreenHeight() / 2f + 300);
    }
}
