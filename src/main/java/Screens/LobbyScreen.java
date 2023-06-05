package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import org.lwjgl.opengl.GL20;
import core.Boot;

public class LobbyScreen extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private Texture buttonCreateLobbyTexture;
    private Texture buttonJoinLobbyTexture;
    private Texture buttonDirectPlayTexture;
    private Texture buttonReturnTexture;

    private final Texture buttonTouchTexture;
    private final Texture buttonIdleTexture;
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture blackTexture;

    private final int BUTTON_WIDTH = 600;
    private final int BUTTON_HEIGHT = 100;

    String username;

    public LobbyScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.batch = new SpriteBatch();

        this.buttonIdleTexture = new Texture("UI_elements/button_idle.png");
        this.buttonTouchTexture = new Texture("UI_elements/button_touch.png");
        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();

        this.username = "";
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);
    }

    private void createLobby(){
        System.out.println("createLobby()");
    }

    private void joinLobby(){
        System.out.println("joinLobby()");
    }

    public void update() {
        buttonCreateLobbyTexture = buttonIdleTexture;
        buttonJoinLobbyTexture = buttonIdleTexture;
        buttonDirectPlayTexture = buttonIdleTexture;
        buttonReturnTexture = buttonIdleTexture;

        batch.setProjectionMatrix(camera.combined);

        enterUsername();
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
        usernameFieldRender();

        batch.end();
    }

    private void enterUsername(){
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            int keyPressed = -1;
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    keyPressed = i;
                    break;
                }
            }
            if (keyPressed != -1) {
                if(keyPressed == 67) {
                    if(username.length() > 0)
                        username = username.substring(0, username.length() - 1); // Backspace
                } else {
                    username += (char)(keyPressed + 36);
                }
            }
        }
    }

    private void usernameFieldRender(){
        batch.draw(blackTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f + 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        font.draw(batch, username, Boot.INSTANCE.getScreenWidth() / 2f - 240, Boot.INSTANCE.getScreenHeight() / 2f + 220);
    }

    public void buttonsRender() {
        batch.draw(buttonCreateLobbyTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f, BUTTON_WIDTH, BUTTON_HEIGHT);
        batch.draw(buttonJoinLobbyTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f - 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        batch.draw(buttonDirectPlayTexture, Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f, Boot.INSTANCE.getScreenHeight() / 2f - 300, BUTTON_WIDTH, BUTTON_HEIGHT);
        batch.draw(buttonReturnTexture, 70, Boot.INSTANCE.getScreenHeight() / 2f + 350, BUTTON_WIDTH / 6.0f, BUTTON_HEIGHT);

        font.draw(batch, "Create lobby", Boot.INSTANCE.getScreenWidth() / 2f - 120, Boot.INSTANCE.getScreenHeight() / 2f + 70);
        font.draw(batch, "Join lobby", Boot.INSTANCE.getScreenWidth() / 2f - 90, Boot.INSTANCE.getScreenHeight() / 2f - 80);
        font.draw(batch, "Direct play (for testing)", Boot.INSTANCE.getScreenWidth() / 2f - 240, Boot.INSTANCE.getScreenHeight() / 2f - 230);
        font.draw(batch, "<", Boot.INSTANCE.getScreenWidth() / 2f - 850, Boot.INSTANCE.getScreenHeight() / 2f + 420);
    }

    public void mouseHandle() {
        if (Gdx.input.getX() >= Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f && Gdx.input.getX() <= Boot.INSTANCE.getScreenWidth() / 2f + BUTTON_WIDTH / 2f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f) {
            buttonCreateLobbyTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                createLobby();
        }
        if (Gdx.input.getX() >= Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f && Gdx.input.getX() <= Boot.INSTANCE.getScreenWidth() / 2f + BUTTON_WIDTH / 2f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT + 150 && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f + 150) {
            buttonJoinLobbyTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                joinLobby();
        }
        if (Gdx.input.getX() >= Boot.INSTANCE.getScreenWidth() / 2f - BUTTON_WIDTH / 2f && Gdx.input.getX() <= Boot.INSTANCE.getScreenWidth() / 2f + BUTTON_WIDTH / 2f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT + 300 && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f + 300) {
            buttonDirectPlayTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                Boot.INSTANCE.setScreen(new GameScreen(camera));
        }
        if (Gdx.input.getX() >= 70 && Gdx.input.getX() <= 70 + BUTTON_WIDTH / 6f
                && Gdx.input.getY() >= Boot.INSTANCE.getScreenHeight() / 2f - BUTTON_HEIGHT - 350 && Gdx.input.getY() <= Boot.INSTANCE.getScreenHeight() / 2f - 350) {
            buttonReturnTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());

        // logo
        batch.draw(logoTexture, Boot.INSTANCE.getScreenWidth() / 2f - 350, Boot.INSTANCE.getScreenHeight() / 2f + 300);
    }

}
