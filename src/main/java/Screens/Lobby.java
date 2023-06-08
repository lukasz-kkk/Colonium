package Screens;

import UI.Button;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import core.Client;
import core.MessageUtility;
import objects.Province;
import org.lwjgl.opengl.GL20;
import core.Boot;

import java.util.ArrayList;
import java.util.List;

import static Screens.LobbyUsername.username;
import static Screens.LobbyCreate.lobbyName;

public class Lobby extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;
    private final Texture blackTexture;

    int tick;

    static Color[] colors = {
            Color.GRAY,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.ORANGE,
            Color.PURPLE,
            Color.PINK,
            Color.OLIVE,
            Color.WHITE,
            Color.BROWN,
            Color.RED,
            Color.CYAN,
            Color.MAGENTA,
            Color.LIME,
            Color.GOLD,
            Color.SALMON
    };
    public static List<Color> basicColors = new ArrayList<>(List.of(colors));

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

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();

        this.buttonCreate = new Button("Create lobby", 660, 140, 600, 100, batch, font);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, font);
        this.buttonRefresh = new Button("R", 650, 950, 100, 100, batch, font);
        this.buttonStart = new Button("Start", 990, 950, 300, 100, batch, font);
    }

    private void start() throws InterruptedException {
        Client.message = MessageUtility.startMapJSON(Client.currentLobby);
        GameScreen gameScreen = new GameScreen(camera);
        Thread.sleep(500);
        assignColors();
        Boot.INSTANCE.setScreen(gameScreen);
    }


    public void update() throws InterruptedException {
        if (tick++ >= 60)
            refreshLobbiesList();

        buttonCreate.update();
        buttonReturn.update();
        buttonStart.update();
        buttonRefresh.update();

        batch.setProjectionMatrix(camera.combined);
        if(Client.startMapFlag == 1){
            start();
        }
        inputHandle();
    }

    private void assignColors(){
        Client.playersColors.put("unowned", basicColors.get(0));
        for(String player : Client.players) {
            Client.playersColors.put(player, basicColors.get(Client.players.indexOf(player) + 1));
        }
    }

    public void inputHandle() throws InterruptedException {
        if (buttonStart.isClicked()) {
            start();
        }
        if(buttonReturn.isClicked()){
            Boot.INSTANCE.setScreen(Boot.lobbyMainScreen);
        }
        if(buttonRefresh.isClicked()){
            refreshLobbiesList();
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
        playersInLobbyRender();

        batch.end();
    }

    private void playersInLobbyRender(){
        float xOffset = Boot.INSTANCE.getScreenWidth() / 3.5f;
        float yOffset = Boot.INSTANCE.getScreenHeight() - 790;

        batch.draw(blackTexture, xOffset, yOffset, 800, 585);
        font.draw(batch, "Players in: " + LobbyMainScreen.lobbyName, xOffset + 20, yOffset + 565);

        if (Client.players != null) {
            int ind = 0;
            for (String l : Client.players) {
                font.draw(batch, l, xOffset + 20, Boot.INSTANCE.getScreenHeight() - 315 - 100 * ind);
                ind++;
            }
        }
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

    public void refreshLobbiesList() {
        Client.getLobbiesInfo = 1;
        Client.message = MessageUtility.createLobbiesRequest();
        tick = 0;
    }
}
