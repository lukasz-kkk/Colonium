package Screens;

import UI.PlayerTile;
import UI.UpgradeMenu;
import Utils.UnitHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    // game objects
    private Map map;
    public static UnitHandler unitHandler;
    private UpgradeMenu upgradeMenu;
    private PlayerTile playerTile;

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        unitHandler = new UnitHandler();

        this.map = new Map(27, this, batch);
        this.upgradeMenu = new UpgradeMenu();
        this.playerTile = new PlayerTile(Client.players);
    }

    public void update() {
        world.step(1 / 60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        map.update();
        unitHandler.update();
        upgradeMenu.update();
    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.render();

        unitHandler.render();

        map.provincesRender();

        upgradeMenu.show();

        playerTile.render();
    }
}
