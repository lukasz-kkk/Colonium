package Screens;

import UI.Button;
import Utils.SoundManager;
import Utils.TextTyper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import core.Client;
import core.MessageUtility;
import org.lwjgl.opengl.GL20;
import core.Boot;

import static Utils.Definitions.SCREEN_HEIGHT;

public class LobbyCreate extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;
    TextTyper textTyper;

    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture blackTexture;
    private final Texture whiteTexture;
    private String warning;

    int tick;

    private static String lobbyName;

    Button buttonContinue;
    Button buttonReturn;

    public LobbyCreate(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();
        this.textTyper = new TextTyper();
        this.warning = "";

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");
        this.logoTexture = new Texture("UI_elements/logo.png");
        this.tick = 0;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        this.whiteTexture = new Texture("white.png");
        pixmap.dispose();

        lobbyName = "";
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas62px.fnt"), Gdx.files.internal("fonts/Bebas62px.png"), false);
        BitmapFont fontReturn = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonContinue = new Button("Continue ", 660, 700, 600, 100, batch, font);
        this.buttonContinue.setAdditionalYOffset(5);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, fontReturn);
    }

    private void createContinue() throws InterruptedException {
        if(lobbynameValidate() != 0) return;

        Client.message = MessageUtility.createLobbyJSON(lobbyName);
        try {
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println(e);
        }
        if(Client.errorOccured == 1 && Client.errorMessage.equals("failed to create lobby"))
        {
            warning = "failed to create lobby";
            lobbyName = "";
            Client.errorOccured = 0;
        }else{
            System.out.println(Client.errorOccured);
            System.out.println(Client.errorMessage);
            Client.currentLobby = lobbyName;
            Thread.sleep(100);
            refreshLobbiesList();
            Boot.INSTANCE.setScreen(Boot.lobby);
        }
    }

    public void update() throws InterruptedException {
        tick++;
        buttonContinue.update();
        buttonReturn.update();

        batch.setProjectionMatrix(camera.combined);

        enterUsername();
        inputHandle();
    }

    public void inputHandle() throws InterruptedException {
        if (buttonContinue.isClicked()) {
            Boot.sm.clickplayer();
            createContinue();
        }
        if (buttonReturn.isClicked()) {
            Boot.sm.clickplayer();
            Boot.INSTANCE.setScreen(Boot.menuScreen);
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

        font.setColor(Color.WHITE);
        backgroundRender();
        buttonsRender();
        lobbyNameFieldRender();
        cursorRender();
        warningRender();

        batch.end();
    }

    private int lobbynameValidate(){
        if(lobbyName.length() == 0){
            warning = "Lobby name cannot be empty";
            return -1;
        }
        return 0;
    }
    private void warningRender(){
        if(warning.equals("")) return;
        font.setColor(Color.RED);
        font.draw(batch, warning, Boot.INSTANCE.getScreenWidth() / 2f - 300, Boot.INSTANCE.getScreenHeight() / 4f + 230);
    }

    private void enterUsername(){
        lobbyName = textTyper.enterText(lobbyName);
    }

    private void lobbyNameFieldRender(){
        font.draw(batch, "Enter lobby name", Boot.INSTANCE.getScreenWidth() / 2f - 270, Boot.INSTANCE.getScreenHeight() / 2f + 180);
        int yPos = Boot.INSTANCE.getScreenHeight() - 500;
        batch.draw(blackTexture, 650, yPos, 600, 100);
        font.draw(batch, lobbyName, 700, yPos + 75);
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

    public void clearLobbyName(){
        lobbyName = "";
    }
    public void refreshLobbiesList() {
        Client.getLobbiesInfo = 1;
        Client.message = MessageUtility.createLobbiesRequest();
    }

    private void cursorRender(){
        if(tick < 30) return;
        if(tick > 60) tick = 0;
        int yPos = SCREEN_HEIGHT - 480;
        int xOffset = 0;
        for (char c : lobbyName.toCharArray()) {
            xOffset += font.getData().getGlyph(c).width * 0.95;
        }

        batch.draw(whiteTexture, 703 + xOffset, yPos, 5, 60);
    }

}
