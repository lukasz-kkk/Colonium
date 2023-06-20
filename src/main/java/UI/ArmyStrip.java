package UI;

import Utils.Definitions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import core.Boot;
import objects.Map;
import Utils.UnitHandler;
import objects.Province;

import java.util.List;

import static Utils.Definitions.SCREEN_HEIGHT;

public class ArmyStrip {
    private final ShapeRenderer shapeRenderer;
    private final List<String> players;
    private final SpriteBatch batch;
    public static String winner;

    public ArmyStrip(List<String> players) {
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.players = players;
        this.players.add("unowned");
    }


    public void render() {
        if (players == null) return;
        playerRender();
    }

    private void playerRender() {

        int lastWidth = 490;
        int renderArmy = 0;
        int totalArmy = 0;
        int losePlayerCounter = 1;
        float w = 0;
        int width = 0;

        for (int i = 0; i < Map.numberOfProvinces; i++) {
            totalArmy += Map.provinces[i].getValue();

        }

        if (!UnitHandler.unitList.isEmpty()) {
            totalArmy += UnitHandler.unitList.size();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(lastWidth - 5, 920 - 5, 1010, 60);
        shapeRenderer.end();

        for (int i = 0; i < players.size(); i++) {
            int np=0;
            for (int j = 0; j < Map.numberOfProvinces; j++) {
                if (Map.provinces[j].owner == null) return;
                if (Map.provinces[j].owner.equals(players.get(i))) {
                    renderArmy += Map.provinces[j].getValue();
                }
            }
            if (!UnitHandler.unitList.isEmpty()) {
                for (int j = 0; j < UnitHandler.unitList.size(); j++) {
                    if (UnitHandler.unitList.get(j).getOwner().equals(players.get(i))) renderArmy++;
                }
            }
            if (renderArmy == 0 && !players.get(i).equals("unowned")) {
                for (int j = 0; j < Map.numberOfProvinces; j++) {
                    if (Map.provinces[j].owner.equals(players.get(i))) np++;
                }
                if(np==0) losePlayerCounter++;
            }
            w = (1000 * renderArmy) / totalArmy;
            width = Math.round(w);
            renderArmy = 0;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            setColor(i);
            shapeRenderer.rect(lastWidth, 920, width, 50);
            lastWidth += width;
            shapeRenderer.end();
        }

        if (losePlayerCounter == players.size() - 1) {
            for (int i = 0; i < players.size() - 1; i++) {
                renderArmy = 0;
                for (int j = 0; j < Map.numberOfProvinces; j++) {
                    if (Map.provinces[j].owner.equals(players.get(i))) {
                        renderArmy += Map.provinces[j].getValue();
                    }
                }
                if (renderArmy > 0) {
                    winner = players.get(i);
                    Boot.INSTANCE.setScreen(Boot.endScreen);
                }
            }
        }
    }

    private void setColor(int playerID) {
        if (playerID == players.size() - 1) shapeRenderer.setColor(Definitions.colors[0]);
        else shapeRenderer.setColor(Definitions.colors[playerID + 1]);
    }

}
