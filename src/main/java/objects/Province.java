package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import core.Boot;
import core.GameScreen;

import static java.lang.Math.abs;

public class Province {
    private Body body;
    private final float xPosition, yPosition;
    private GameScreen gameScreen;
    private Texture texture;
    public static int WIDTH = 70;
    public static int HEIGHT = 70;
    private BitmapFont font;

    private int value=0;
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int counter=0;
    private final int timeout=MathUtils.random(60,160);
    private final int unitsCap=MathUtils.random(30,60);

    //if(Gdx.input.isTouched()&&Gdx.input.getX()==X&&(Gdx.graphics.getHeight() - Gdx.input.getY())==Y)
    public Province(float xPosition, float yPosition, GameScreen gameScreen){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.gameScreen = gameScreen;
        //font = new BitmapFont();
        font = new BitmapFont(Gdx.files.internal("font20.fnt"),Gdx.files.internal("font20.png"),false);
        this.texture = new Texture("test_blob.png");
        font.setColor(Color.RED);
        font.getData().setScale(.5f);

    }

    public float getXposition()
    {
        return xPosition;
    }

    public float getYposition()
    {
        return yPosition;
    }

    public void render(SpriteBatch batch) {



       
        //batch.draw(texture, xPosition, yPosition);
        batch.draw(texture, xPosition-50,  yPosition, WIDTH, HEIGHT);
        //font.draw(batch, String.valueOf(value),
        //        (xPosition / 100 * Boot.INSTANCE.getScreenWidth())+25,
        //        (yPosition / 100 * Boot.INSTANCE.getScreenHeight())+25);
        font.draw(batch, String.valueOf(value), xPosition, yPosition +50);

    }
    public void update(){
        if (counter%timeout==0&&value<=unitsCap)value++;
        counter++;
    }
}
