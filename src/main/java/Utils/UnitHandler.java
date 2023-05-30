package Utils;

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
        if(toDelete != null)
            unitList.remove(toDelete);
    }

    public void sendUnits(int source, int destination) {
        unitList.add(new Unit((int)Map.provinces[source].getXposition(),(int) Map.provinces[source].getYposition(),
                (int)Map.provinces[destination].getXposition(),(int) Map.provinces[destination].getYposition()));
        numberOfUnits++;
    }

}
