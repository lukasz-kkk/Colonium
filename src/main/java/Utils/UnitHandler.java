package Utils;

import com.badlogic.gdx.utils.Timer;
import objects.Map;
import objects.Province;
import objects.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitHandler {
    List<Unit> unitList = new ArrayList<>();
    int numberOfUnits;

    public UnitHandler() {
        numberOfUnits = 0;
    }

    public void render() {
        for (Unit unit : unitList) {
            unit.render();
        }
    }

    public void update() {
        Unit toDelete = null;
        for (Unit unit : unitList) {
            if(unit.getX() == unit.getTargetX() && unit.getY() == unit.getTargetY()){
                toDelete = unit;
            }
        }
        if(toDelete != null) {
            toDelete.dispose();
            unitList.remove(toDelete);
        }
    }

    public void sendUnit(int source, int destination) {
        Unit toAdd = new Unit((int)Map.provinces[source].getXposition(),(int) Map.provinces[source].getYposition(),
                (int)Map.provinces[destination].getXposition(),(int) Map.provinces[destination].getYposition());
        toAdd.setColor(Map.provinces[source].owner);
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
