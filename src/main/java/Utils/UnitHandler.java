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
    public static List<Unit> unitList = new ArrayList<>();
    int numberOfUnits;

    public UnitHandler() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(0, 0, 0, 1);
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
        for (Iterator<Unit> iterator = unitList.iterator(); iterator.hasNext(); ) {
            Unit unit = iterator.next();
            if (unit.getX() == unit.getTargetX() && unit.getY() == unit.getTargetY()) {
                unit.dispose();
                iterator.remove();
            }
        }
    }

    public void sendUnit(int sourceX, int sourceY, int destinationX, int destinationY, int sourceID) {
        Unit toAdd = new Unit(Map.provinces[sourceID].owner, sourceX, sourceY, destinationX, destinationY, shapeRenderer);
        unitList.add(toAdd);
        numberOfUnits++;
    }

    public void sendUnits(int source, int destination) {
        Timer.schedule(new Timer.Task() {
            final float sourceX = Map.provinces[source].getXposition();
            final float sourceY = Map.provinces[source].getYposition();
            final float destinationX = Map.provinces[destination].getXposition();
            final float destinationY = Map.provinces[destination].getYposition();
            final float[][] positions = calculate(source, destination);
            int unitsToSend = Map.provinces[source].getValue();
            @Override
            public void run() {
                int singleSend = Math.min(unitsToSend, 3);
                unitsToSend -= singleSend;

                for (int i = 0; i < singleSend; i++) {
                    sendUnit((int) sourceX + (int) positions[i][0], (int) sourceY + (int) positions[i][1],
                            (int) destinationX + (int) positions[i][0], (int) destinationY + (int) positions[i][1], source);
                }

                if (unitsToSend <= 0) {
                    this.cancel();
                }
            }
        }, 0, 0.25f);
    }

    private float[][] calculate(int source, int destination) {
        float[][] positions = {{0, 0}, {0, 0}, {0, 0}};
        float localProvinceX = Map.provinces[destination].getXposition() - Map.provinces[source].getXposition();
        float localProvinceY = Map.provinces[destination].getYposition() - Map.provinces[source].getYposition();
        float OFFSET = 12;
        float perpSlope = -(localProvinceX / localProvinceY);

        float y_2 = (float) Math.pow(perpSlope, 2);
        float x_2 = y_2 + 1;
        float x = (float) Math.sqrt(Math.pow(OFFSET, 2) / x_2);

        positions[0][0] = 0;
        positions[0][1] = 0;

        positions[1][0] = x;
        positions[1][1] = x * perpSlope;

        positions[2][0] = -x;
        positions[2][1] = -x * perpSlope;

        return positions;
    }

}
