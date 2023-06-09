package Screens;

import UI.Button;
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
import core.MessageUtility;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL20;
import core.Boot;

import static Utils.Definitions.*;

public class LobbyMainScreen extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Texture backgroundTexture;
    private final Texture blackTexture;

    int tick;
    int lobbySelected;

    public static String lobbyName;

    String username;

    Button buttonCreate;
    Button buttonReturn;
    Button buttonJoin;
    Button buttonRefresh;

    public LobbyMainScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();

        this.backgroundTexture = new Texture("UI_elements/menu_background.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();

        this.tick = 0;
        this.lobbySelected = -1;
        this.username = "";
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);

        this.buttonCreate = new Button("Create lobby", 660, 140, 600, 100, batch, font);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, font);
        this.buttonRefresh = new Button("R", 650, 950, 100, 100, batch, font);
        this.buttonJoin = new Button("join", 990, 950, 300, 100, batch, font);
    }

    private void createLobby() {
        Boot.lobbyCreate.clearLobbyName();
        Boot.INSTANCE.setScreen(Boot.lobbyCreate);
    }

    private void joinLobby() throws InterruptedException {
        if(lobbySelected == -1) return;
        int select = 4 - lobbySelected;

        int spaceIndex;
        try {
            spaceIndex = Client.lobbies.get(select).indexOf(" ");
        } catch (NullPointerException | IndexOutOfBoundsException e){
            System.out.println(ANSI_RED + "Failed to find lobby" + ANSI_RESET);
            return;
        }

        lobbyName = Client.lobbies.get(select).substring(0, spaceIndex);
        LobbyCreate.lobbyName = lobbyName;
        Client.message = MessageUtility.joinLobbyJSON(lobbyName);
        Thread.sleep(200);
        Client.currentLobby = lobbyName;

        refreshLobbiesList();
        Boot.INSTANCE.setScreen(Boot.lobby);
    }

    public void update() throws InterruptedException {
        buttonCreate.update();
        buttonReturn.update();
        buttonJoin.update();
        buttonRefresh.update();

        //if (tick++ >= 300)
        //    refreshLobbiesList();

        batch.setProjectionMatrix(camera.combined);

        inputHandle();
    }

    public void inputHandle() throws InterruptedException {
        if (buttonCreate.isClicked()) {
            createLobby();
        }
        if (buttonReturn.isClicked()) {
            Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
        if (buttonRefresh.isClicked()) {
            refreshLobbiesList();
        }
        if (buttonJoin.isClicked()) {
            joinLobby();
        }
        selectLobby();
    }

    public void refreshLobbiesList() {
        Client.getLobbiesInfo = 1;
        Client.message = MessageUtility.createLobbiesRequest();
        tick = 0;
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
        lobbiesRender();
        selectionFrameRender();

        batch.end();
    }

    private void playerInfoRender() {
        font.draw(batch, "Username: " + username, 10, Gdx.graphics.getHeight() / 10.0f);
    }

    private void lobbiesRender() {
        float xOffset = Boot.INSTANCE.getScreenWidth() / 3.5f;
        float yOffset = Boot.INSTANCE.getScreenHeight() - 790;
        batch.draw(blackTexture, xOffset, yOffset, 800, 585);
        font.draw(batch, "Lobby name", xOffset + 20, yOffset + 565);
        font.draw(batch, "Players", xOffset + 570, yOffset + 565);

        if (Client.lobbies != null) {
            int ind = 0;
            for (String l : Client.lobbies) {
                font.draw(batch, l, xOffset + 20, Boot.INSTANCE.getScreenHeight() - 315 - 100 * ind);
                ind++;
            }
        }
    }

    public void selectionFrameRender() {
        float xOffset = Boot.INSTANCE.getScreenWidth() / 3.5f;
        float yOffset = Boot.INSTANCE.getScreenHeight() - 790;

        if (lobbySelected != -1) {
            yOffset += 100 * lobbySelected;
        }

        batch.draw(blackTexture, xOffset, yOffset, 800, 100);
    }

    public void selectLobby() {
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) return;

        float xOffset = Boot.INSTANCE.getScreenWidth() / 3.5f;
        float yOffset = Boot.INSTANCE.getScreenHeight() - 300;
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();

        for (int i = 0; i < 5; i++) {
            int lobbyOffset = 100 * i;
            if (mouseX >= xOffset && mouseX <= xOffset + 800 && mouseY >= yOffset - lobbyOffset && mouseY <= yOffset - lobbyOffset + 100) {
                lobbySelected = i;
                break;
            }
        }
    }

    public void buttonsRender() {
        buttonCreate.render();
        buttonReturn.render();
        buttonJoin.render();
        buttonRefresh.render();
    }

    public void backgroundRender() {
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
