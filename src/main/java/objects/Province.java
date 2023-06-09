package objects;

import Screens.Lobby;
import Screens.LobbyCreate;
import Screens.LobbyUsername;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import Screens.GameScreen;
import core.Client;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Province {
    private Body body;
    public int ID;
    public String owner;
    private final float xPosition, yPosition;
    private GameScreen gameScreen;
    public static int RADIUS = 22;
    private final BitmapFont font;
    private int value=0;
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    private int counter = 0;
    private final int timeout = MathUtils.random(60, 160);
    private final int unitsCap = MathUtils.random(30, 60);
    ShapeRenderer shapeRenderer;

    public Province(float xPosition, float yPosition, int ID, GameScreen gameScreen, ShapeRenderer shapeRenderer) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.gameScreen = gameScreen;
        this.shapeRenderer = shapeRenderer;
        this.ID = ID;
        this.value = 20;
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas26px.fnt"), Gdx.files.internal("fonts/Bebas26px.png"), false);

        font.getData().setScale(1);
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 1); // BLACK
        shapeRenderer.circle(xPosition, yPosition, RADIUS + 4); // +val == border size

        setColor();
        shapeRenderer.circle(xPosition, yPosition, RADIUS);

        shapeRenderer.end();

        int numOfDigits = (int) Math.floor(Math.log10(value)) + 1;
        if(value == 0) numOfDigits = 1;
        int fontOffset = numOfDigits * 6;

        batch.begin();
        font.draw(batch, String.valueOf(value), xPosition - fontOffset, yPosition + 12);
        font.draw(batch, "Lobby name: " + LobbyCreate.lobbyName, 10, 1000);
        font.draw(batch, "User name: " + LobbyUsername.username, 10, 900);
        batch.end();
    }

    public void setColor() {
        if(owner == null) return;
        shapeRenderer.setColor(Client.playersColors.get(owner));

    }
  
    public void update(){
//        if (counter%timeout==0&&value<=unitsCap)value++;
//        counter++;
    }
}
