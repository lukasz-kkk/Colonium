package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import org.lwjgl.opengl.GL20;

public class MenuScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private BitmapFont font;

    public MenuScreen(OrthographicCamera camera){
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2,Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        //font
        font = new BitmapFont(Gdx.files.internal("font20.fnt"),Gdx.files.internal("font20.png"),false);
        font.getData().setScale(1f);
    }

    public void update(){
        world.step(1/60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            Boot.INSTANCE.setScreen(new GameScreen(camera));
        }
    }

    @Override
    public void render(float delta){
        update();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.6f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "temp menu press SPACE to run", Boot.INSTANCE.getScreenHeight() / 2f, Boot.INSTANCE.getScreenHeight() / 2f);
        batch.end();
    }
}
