package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import core.Boot;
import core.GameScreen;

public class Province {
    private Body body;
    private final float xPosition, yPosition;
    private GameScreen gameScreen;
    private final Texture texture;
    public static int WIDTH = 70;
    public static int HEIGHT = 70;

    public Province(float xPosition, float yPosition, GameScreen gameScreen){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.gameScreen = gameScreen;
        this.texture = new Texture("test_blob.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,
                xPosition / 100 * Boot.INSTANCE.getScreenWidth(),
                yPosition / 100 * Boot.INSTANCE.getScreenHeight(),
                WIDTH, HEIGHT);
    }
}
