package UI;

import Utils.Definitions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import objects.Map;
import Utils.UnitHandler;
import objects.Province;

import java.util.List;

import static Utils.Definitions.SCREEN_HEIGHT;

public class ArmyStrip {
    private final ShapeRenderer shapeRenderer;
    private final List<String> players;
    private final SpriteBatch batch;

    public ArmyStrip(List<String> players){
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.players = players;
        this.players.add("unowned");
    }


    public void render(){
        if(players == null) return;
        playerRender();
    }

    private void playerRender(){

        int lastWidth = 0;
        int renderArmy = 0;
        int totalArmy = 0;
        float w = 0;
        int width =0;

        for(int i = 0; i < Map.numberOfProvinces; i++)
        {
            totalArmy += Map.provinces[i].getValue();

        }

        if(!UnitHandler.unitList.isEmpty())
        {
            totalArmy += UnitHandler.unitList.size();
        }


        for (int i=0;i<players.size();i++) {
            for (int j = 0; j < Map.numberOfProvinces; j++) {
                if (Map.provinces[j].owner.equals(players.get(i))) {
                    renderArmy +=Map.provinces[j].getValue();
                }
            }
            if(!UnitHandler.unitList.isEmpty())
            {
                for(int j =0;j<UnitHandler.unitList.size();j++)
                {
                    if(UnitHandler.unitList.get(j).getOwner().equals(players.get(i))) renderArmy++;
                }
            }
            w = (1800*renderArmy)/totalArmy;
            width = Math.round(w);
            renderArmy=0;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            setColor(i);
            shapeRenderer.rect(lastWidth,920, width, 50);
            lastWidth += width;
            shapeRenderer.end();
        }
    }

    private void setColor(int playerID){
        if(playerID == players.size()-1) shapeRenderer.setColor(Definitions.colors[0]);
        else shapeRenderer.setColor(Definitions.colors[playerID+1]);
    }

}
