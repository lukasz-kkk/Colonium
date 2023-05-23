package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import core.Boot;
import core.GameScreen;

public class Province {
    private Body body;
    public int ID;
    public int owner; // PLAYER_ID 0 - neutral
    private final float xPosition, yPosition;
    private GameScreen gameScreen;
    private Texture texture;
    public static int RADIUS = 20;
    private BitmapFont font;
    private int value = 0;
    private int counter = 0;
    private final int timeout = MathUtils.random(60, 160);
    private final int unitsCap = MathUtils.random(30, 60);
    ShapeRenderer shapeRenderer;

    public Province(float xPosition, float yPosition, int ID, GameScreen gameScreen) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.gameScreen = gameScreen;
        this.ID = ID;
        this.owner = 0;
        font = new BitmapFont(Gdx.files.internal("font20.fnt"), Gdx.files.internal("font20.png"), false);
        this.texture = new Texture("test_blob.png");
        font.setColor(Color.BLACK);
        font.getData().setScale(.4f);
        shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 1); // BLACK
        shapeRenderer.circle(xPosition, yPosition, RADIUS + 4); // +val == border size

        setColor();
        shapeRenderer.circle(xPosition, yPosition, RADIUS);

        shapeRenderer.end();

        batch.begin();
        font.draw(batch, String.valueOf(value), xPosition - 5, yPosition + 5);
        batch.end();
    }

    public void setColor() {
        if (owner == 0)
            shapeRenderer.setColor(new Color(Color.GRAY));
        if (owner == 1)
            shapeRenderer.setColor(new Color(Color.BLUE));
        if (owner == 2)
            shapeRenderer.setColor(new Color(Color.RED));
        if (owner == 3)
            shapeRenderer.setColor(new Color(Color.GREEN));
        if (owner == 4)
            shapeRenderer.setColor(new Color(Color.YELLOW));
    }

    public void update() {
        if (counter % timeout == 0 && value != unitsCap) value++;
        counter++;
    }
}
