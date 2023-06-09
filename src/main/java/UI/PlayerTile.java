package UI;

import Utils.Definitions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class PlayerTile {
    private final ShapeRenderer shapeRenderer;
    private final List<String> players;
    private final BitmapFont font;
    private final SpriteBatch batch;

    public PlayerTile(List<String> players){
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.players = players;
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas34px.fnt"), Gdx.files.internal("fonts/Bebas34px.png"), false);

        font.getData().setScale(1);
    }


    public void render(){

        for(int i = 0; i < players.size(); i++){
            playerRender(i);
        }

    }

    private void playerRender(int playerID){
        int basicY = Gdx.graphics.getHeight() / 4;
        int height = 50;
        int width = 250;

        int offsetY = 80 * playerID;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, basicY + offsetY - 5, width + 5, height + 10);

        setColor(playerID);
        shapeRenderer.rect(0, basicY + offsetY, width, height);

        shapeRenderer.end();
        nameRender(playerID, 0, (basicY + offsetY));
    }

    private void nameRender(int playerID, int x, int y){
        batch.begin();

        font.draw(batch, players.get(playerID), x + 5, y + 40);


        batch.end();
    }

    private void setColor(int playerID){
        shapeRenderer.setColor(Definitions.colors[playerID + 1]);
    }

}
