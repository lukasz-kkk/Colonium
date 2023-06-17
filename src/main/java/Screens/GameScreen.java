package Screens;

import UI.ArmyStrip;
import UI.Button;
import UI.PlayerTile;
import UI.UpgradeMenu;
import Utils.SoundManager;
import Utils.UnitHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import core.Boot;
import core.Client;
import objects.Map;
import objects.Province;
import org.lwjgl.opengl.GL20;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private final BitmapFont font;

    // game objects
    private Button buttonQuit;
    private Map map_1;

    private Map map_selected;

    public static UnitHandler unitHandler;
    private UpgradeMenu upgradeMenu;
    private PlayerTile playerTile;
    private ArmyStrip armyStrip;

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        //this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2f, Boot.INSTANCE.getScreenHeight() / 2f, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas26px.fnt"), Gdx.files.internal("fonts/Bebas26px.png"), false);
        font.getData().setScale(1f);

        unitHandler = new UnitHandler();
        this.upgradeMenu = new UpgradeMenu();

        this.buttonQuit = new Button("X", 1800, 100, 50, 50, batch, font);
        this.buttonQuit.setAdditionalYOffset(20);
        this.buttonQuit.setAdditionalXOffset(3);

        initMaps();

        map_selected = map_1;
    }

    public void initMaps(){
        this.map_1 = new Map(27, this, batch);
    }

    public void update() {
        world.step(1 / 60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Boot.sm.clickplayer();
            Gdx.app.exit();
        }
        map_selected.update();
        unitHandler.update();
        upgradeMenu.update();
        buttonQuit.update();
        if(buttonQuit.isClicked()) {
            Boot.sm.clickplayer();
            Gdx.app.exit();
            System.exit(0);
        }
    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map_selected.render();

        unitHandler.render();

        map_selected.provincesRender();

        upgradeMenu.show();

        playerTile.render();
        armyStrip.render();

        batch.begin();
        buttonQuit.render();
        batch.end();
    }

    public void setPlayerTile() {
        this.playerTile = new PlayerTile(Client.players);
        this.armyStrip = new ArmyStrip(Client.players);
    }
}
