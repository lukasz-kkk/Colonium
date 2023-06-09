package Screens;

import UI.Button;
import Utils.SoundManager;
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
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

import static Utils.Definitions.*;

public class Lobby extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;
    private final Texture blackTexture;

    int tick;


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
        this.buttonRefresh = new Button("Refresh ", 610, 950, 300, 100, batch, font);
        this.buttonStart = new Button("Start", 990, 950, 300, 100, batch, font);
    }

    private void start() throws InterruptedException {
        if (Client.players == null) return;
        Client.message = MessageUtility.startMapJSON(Client.currentLobby);
        Thread.sleep(500);
        assignColors();
        Boot.gameScreen.setPlayerTile();
        Boot.INSTANCE.setScreen(Boot.gameScreen);
    }


    public void update() throws InterruptedException {
        if (tick++ >= 60)
            refreshLobbiesList();

        buttonCreate.update();
        buttonReturn.update();
        buttonStart.update();
        buttonRefresh.update();

        batch.setProjectionMatrix(camera.combined);
        if (Client.startMapFlag == 1) {
            start();
        }
        inputHandle();
    }

    private void assignColors() {
        Client.playersColors.put("unowned", basicColors.get(0));
        for (String player : Client.players) {
            Client.playersColors.put(player, basicColors.get(Client.players.indexOf(player) + 1));
        }
    }

    public void inputHandle() throws InterruptedException {
        if (buttonStart.isClicked()) {
            Boot.sm.clickplayer();
            start();
        }
        if (buttonReturn.isClicked()) {
            Boot.sm.clickplayer();
            Client.message = MessageUtility.leaveLobbyJSON();
            Boot.INSTANCE.setScreen(Boot.lobbyMainScreen);
        }
        if (buttonRefresh.isClicked()) {
            Boot.sm.clickplayer();
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

    private void playersInLobbyRender() {
        float xOffset = SCREEN_WIDTH / 3.5f;
        float yOffset = SCREEN_HEIGHT - 800;

        batch.draw(blackTexture, xOffset, yOffset, 820, 700);
        font.draw(batch, "Players in: " + Client.currentLobby, xOffset + 20, yOffset + 680);


        if (Client.players != null) {
            int indY = 0;
            int indX = 0;
            for (String l : Client.players) {
                font.draw(batch, l, xOffset + 20 + indX, yOffset + 580 - 70 * indY);
                indY++;
                if(indY >= 8) {
                    indX = 400;
                    indY = 0;
                }
            }
        }
    }

    private void playerInfoRender() {
        font.draw(batch, "Username: " + Client.clientName, 10, Gdx.graphics.getHeight() / 10.0f);
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
