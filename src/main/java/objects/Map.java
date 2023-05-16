package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import core.GameScreen;

public class Map {
    GameScreen gameScreen;
    SpriteBatch batch;

    int numberOfProvinces;
    private Province[] provinces;

    public Map(int numberOfProvinces, GameScreen gameScreen, SpriteBatch batch){
        this.numberOfProvinces = numberOfProvinces;
        this.gameScreen = gameScreen;
        this.batch = batch;
        provincesInit();
    }

    private void provincesInit(){
        provinces = new Province[numberOfProvinces];
        for(int i = 0; i < numberOfProvinces; i++){
            provinces[i] = new Province(MathUtils.random(0, 100), MathUtils.random(0, 100), gameScreen);
        }
    }

    public void provincesRender(){
        for(int i = 0; i < numberOfProvinces; i++){
            provinces[i].render(batch);
        }
    }
}
