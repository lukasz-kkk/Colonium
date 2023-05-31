package Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import objects.Map;
import objects.Province;
import objects.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UnitHandler {
    ShapeRenderer shapeRenderer;
    List<Unit> unitList = new ArrayList<>();
    int numberOfUnits;

    public UnitHandler() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(0,0,0,1);
        numberOfUnits = 0;
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Unit unit : unitList) {
            unit.render();
        }
        shapeRenderer.end();
    }

    public void update() {
        Unit toDelete = null;
        for (Iterator<Unit> iterator = unitList.iterator(); iterator.hasNext();) {
            Unit unit = iterator.next();
            if (unit.getX() == unit.getTargetX() && unit.getY() == unit.getTargetY()) {
                unit.dispose();
                iterator.remove();
            }
        }
    }

    public void sendUnit(int source, int destination) {
        Unit toAdd = new Unit(Map.provinces[source].owner, (int)Map.provinces[source].getXposition(),(int) Map.provinces[source].getYposition(),
                (int)Map.provinces[destination].getXposition(),(int) Map.provinces[destination].getYposition(), shapeRenderer);
        unitList.add(toAdd);
        numberOfUnits++;
    }

    public void sendUnits(int source, int destination){
        Timer.schedule(new Timer.Task() {
            int counter = 0;
            @Override
            public void run() {
                sendUnit(source, destination);
                counter++;
                if (counter >= 3) {
                    this.cancel();
                }
            }
        }, 0, 0.3f);
    }

}
