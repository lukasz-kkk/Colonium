package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import core.Boot;
import core.GameScreen;

public class Map {
    private final Texture texture;
    GameScreen gameScreen;
    SpriteBatch batch;

    int numberOfProvinces;
    private Province[] provinces;
    int[][] coordinates = {{355,293}, {700,160}, {1067,219}, {1395,323}, {330,505}, {573,487}, {757,357}, {1111,455}, {471,675}, {673,761}, {888,611}, {837,787}, {1005,903}, {1073,767}, {1279,901}, {1365,666}};

    public Map(int numberOfProvinces, GameScreen gameScreen, SpriteBatch batch){
        this.numberOfProvinces = numberOfProvinces;
        this.gameScreen = gameScreen;
        this.batch = batch;
        this.texture = new Texture("prov.png");
        provincesInit();
    }

    private void provincesInit(){

        provinces = new Province[numberOfProvinces];
        for(int i = 0; i < numberOfProvinces; i++){
            provinces[i] = new Province(coordinates[i][0]+50, Gdx.graphics.getHeight() - coordinates[i][1]+50, gameScreen);
            System.out.println(coordinates[i][0]+ " " + coordinates[i][1]);
        }

    }

    public void provincesRender(){

        batch.draw(texture,0,0);
        for(int i = 0; i < numberOfProvinces; i++){
            provinces[i].render(batch);
        }
    }
    public void update(){
        for(int i = 0; i < numberOfProvinces; i++){
            provinces[i].update();
        }
    }
}
