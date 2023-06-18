package Screens;

import UI.Button;
import Utils.TextTyper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
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

import java.util.WeakHashMap;

import static Utils.Definitions.SCREEN_HEIGHT;

public class LobbyUsername extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture blackTexture;
    private final Texture whiteTexture;
    private String warning;

    private String username;

    TextTyper textTyper;

    int tick;

    Button buttonContinue;
    Button buttonReturn;

    public LobbyUsername(OrthographicCamera camera)  {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();
        this.textTyper = new TextTyper();
        this.warning = "";
        this.tick = 0;

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");
        this.whiteTexture = new Texture("white.png");

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
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas62px.fnt"), Gdx.files.internal("fonts/Bebas62px.png"), false);
        BitmapFont fontReturn = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonContinue = new Button("Continue ", 660, 700, 600, 100, batch, font);
        this.buttonContinue.setAdditionalYOffset(5);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, fontReturn);
    }

    public void update(){
        tick++;
        buttonContinue.update();
        buttonReturn.update();

        batch.setProjectionMatrix(camera.combined);

        enterUsername();
        inputHandle();
    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.setColor(Color.WHITE);
        backgroundRender();
        buttonsRender();
        usernameFieldRender();
        cursorRender();
        warningRender();

        batch.end();
    }

    public void inputHandle() {
        if (buttonContinue.isClicked()) {
            Boot.sm.clickplayer();
            userContinue();
        }
        if (buttonReturn.isClicked()) {
            Boot.sm.clickplayer();
            Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
    }

    private void userContinue() {
        if(usernameValidate() != 0) return;
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

    private int usernameValidate(){
        if(username.length() == 0){
            warning = "Username cannot be empty";
            return -1;
        }

        return 0;
    }

    private void warningRender(){
        if(warning.equals("")) return;
        font.setColor(Color.RED);
        font.draw(batch, warning, Boot.INSTANCE.getScreenWidth() / 2f - 280, Boot.INSTANCE.getScreenHeight() / 4f + 230);
    }

    private void enterUsername() {
        username = textTyper.enterText(username);
    }

    private void usernameFieldRender() {
        font.draw(batch, "Enter username", Boot.INSTANCE.getScreenWidth() / 2f - 270, Boot.INSTANCE.getScreenHeight() / 2f + 180);
        int yPos = SCREEN_HEIGHT - 500;
        batch.draw(blackTexture, 660, yPos, 600, 100);
        font.draw(batch, username, 700, yPos + 80);
    }

    private void buttonsRender() {
        buttonContinue.render();
        buttonReturn.render();
    }

    private void cursorRender(){
        if(tick < 30) return;
        if(tick > 60) tick = 0;
        int yPos = SCREEN_HEIGHT - 480;
        int xOffset = 0;
        for (char c : username.toCharArray()) {
            xOffset += font.getData().getGlyph(c).width * 0.95;
        }


        batch.draw(whiteTexture, 703 + xOffset, yPos, 5, 60);
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());

        // logo
        batch.draw(logoTexture, Boot.INSTANCE.getScreenWidth() / 2f - 360, Boot.INSTANCE.getScreenHeight() / 2f + 300);
    }

}
