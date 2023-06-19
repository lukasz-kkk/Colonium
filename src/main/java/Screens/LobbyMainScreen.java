package Screens;

import UI.Button;
import Utils.SoundManager;
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
import core.UserInput;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL20;
import core.Boot;
import org.lwjgl.system.CallbackI;

import static Utils.Definitions.*;

public class LobbyMainScreen extends ScreenAdapter {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture backgroundTexture;
    private final Texture blackTexture;

    int tick;
    int lobbySelected;

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
        //font
        font = new BitmapFont(Gdx.files.internal("fonts/font20.fnt"), Gdx.files.internal("fonts/font20.png"), false);
        font.getData().setScale(1f);


        this.buttonCreate = new Button("Create lobby", 660, 140, 600, 100, batch, font);
        this.buttonReturn = new Button("<", 40, 140, 100, 100, batch, font);
        this.buttonRefresh = new Button("Refresh ", 610, 950, 300, 100, batch, font);
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

        String lobbyName = Client.lobbies.get(select).substring(0, spaceIndex);
        Client.currentLobby = lobbyName;
        Client.message = MessageUtility.joinLobbyJSON(lobbyName);
        Thread.sleep(200);

        refreshLobbiesList();
        Boot.INSTANCE.setScreen(Boot.lobby);
    }

    public void update() throws InterruptedException {
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        batch.setProjectionMatrix(camera.combined);

        buttonCreate.update();
        buttonReturn.update();
        buttonJoin.update();
        buttonRefresh.update();

        //if (tick++ >= 300)
        //    refreshLobbiesList();


        inputHandle();
    }

    public void inputHandle() throws InterruptedException {
        if (buttonCreate.isClicked()) {
            Boot.sm.clickplayer();
            createLobby();
        }
        if (buttonReturn.isClicked()) {
            Boot.sm.clickplayer();
            Boot.INSTANCE.setScreen(Boot.menuScreen);
        }
        if (buttonRefresh.isClicked()) {
            Boot.sm.clickplayer();
            refreshLobbiesList();
        }
        if (buttonJoin.isClicked()) {
            Boot.sm.clickplayer();
            joinLobby();
        }
        selectLobby();
    }

    public void refreshLobbiesList() {
        Client.getLobbiesInfo = 1;
        Client.lobbies.clear();
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
        font.draw(batch, "Username: " + Client.clientName, 10, Gdx.graphics.getHeight() / 10.0f);
    }

    private void lobbiesRender() {
        float xOffset = SCREEN_WIDTH / 3.5f;
        float yOffset = SCREEN_HEIGHT - 790;
        batch.draw(blackTexture, xOffset, yOffset, 800, 585);
        batch.draw(blackTexture, xOffset, yOffset + 500, 800, 85);

        font.draw(batch, "                  ===Lobbies list===", xOffset + 20, yOffset + 565);


        if (Client.lobbies != null) {
            int ind = 0;
            for (String l : Client.lobbies) {
                font.draw(batch, l, xOffset + 20, Boot.INSTANCE.getScreenHeight() - 315 - 100 * ind);
                ind++;
            }
        }
    }

    public void selectionFrameRender() {
        if(lobbySelected == -1) return;
        float xOffset = SCREEN_WIDTH / 3.5f;
        float yOffset = SCREEN_HEIGHT - 790;
        yOffset += 100 * lobbySelected;

        batch.draw(blackTexture, xOffset, yOffset, 800, 100);
    }

    public void selectLobby() {
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) return;

        float xOffset = SCREEN_WIDTH / 3.5f;
        float yOffset = SCREEN_HEIGHT - 300;
        int mouseX = UserInput.getMouseX();
        int mouseY = SCREEN_HEIGHT - UserInput.getMouseY() + 40;
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

}
