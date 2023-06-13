package Screens;

import UI.Button;
import Utils.SoundManager;
import Utils.TextTyper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import core.Client;
import core.Launcher;
import core.MessageUtility;
import org.lwjgl.opengl.GL20;
import core.Boot;

public class LobbyUsername extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture blackTexture;
    private SoundManager sm = new SoundManager();
    private String username;

    TextTyper textTyper;

    Button buttonContinue;
    Button buttonReturn;

    public LobbyUsername(OrthographicCamera camera)  {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();
        this.textTyper = new TextTyper();

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();

        username = "";
        if(Launcher.test) {
            username = "Test_player_" + Launcher.randomName;
            System.out.println(username);
        }
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonContinue = new Button("Continue", 660, 700, 600, 100, batch, font);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, font);
    }

    private void userContinue() {
        Client.clientName = username;
        Client.message = MessageUtility.setNameJSON(Client.clientName);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Client.getLobbiesInfo = 1;
        Client.message = MessageUtility.createLobbiesRequest();

        Boot.INSTANCE.setScreen(Boot.lobbyMainScreen);
    }

    public void update(){
        buttonContinue.update();
        buttonReturn.update();

        batch.setProjectionMatrix(camera.combined);

        enterUsername();
        inputHandle();
    }

    public void inputHandle() {
        if (buttonContinue.isClicked()) {
            sm.clickplayer();
            userContinue();
        }
        if (buttonReturn.isClicked()) {
            sm.clickplayer();
            Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
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

    private void enterUsername() {
        username = textTyper.enterText(username);
    }

    private void usernameFieldRender() {
        font.draw(batch, "Enter username", Boot.INSTANCE.getScreenWidth() / 2f - 270, Boot.INSTANCE.getScreenHeight() / 2f + 180);
        int yPos = Boot.INSTANCE.getScreenHeight() - 500;
        batch.draw(blackTexture, 650, yPos, 600, 100);
        font.draw(batch, username, 700, yPos + 70);
    }

    public void buttonsRender() {
        buttonContinue.render();
        buttonReturn.render();
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());

        // logo
        batch.draw(logoTexture, Boot.INSTANCE.getScreenWidth() / 2f - 360, Boot.INSTANCE.getScreenHeight() / 2f + 300);
    }

}
