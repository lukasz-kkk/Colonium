package objects;

import UI.Arrow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import core.Boot;
import Screens.GameScreen;
import core.Client;
import core.UserInput;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Iterator;

public class Map {
    private final Texture texture;
    GameScreen gameScreen;
    SpriteBatch batch;
    PolygonSprite[] polySprite;
    PolygonSpriteBatch polyBatch;
    ShapeRenderer shapeRenderer;

    private final Texture backgroundTexture;
    public static int numberOfProvinces;
    public static Province[] provinces;

    String pngPath;
    float[][] vertices;
    int[][] blobCoordinates;
    Arrow arrow = new Arrow();

    public Map(int numberOfProvinces, GameScreen gameScreen, SpriteBatch batch) {
        Map.numberOfProvinces = numberOfProvinces;
        this.gameScreen = gameScreen;
        this.batch = batch;
        try {
            jsonRead();
        }catch (Exception e){
            System.out.println(e);
        }
        this.texture = new Texture("white.png");
        this.backgroundTexture = new Texture("maps/prov1_borders.png");
        shapeRenderer = new ShapeRenderer();

        provincesInit();
        polygonRendererInit();
    }

    private void provincesInit() {
        provinces = new Province[numberOfProvinces];
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i] = new Province(blobCoordinates[i][0] + 50, Gdx.graphics.getHeight() - blobCoordinates[i][1] + 50, i, gameScreen, shapeRenderer);
        }

    }

    UserInput user = new UserInput();
    public void provincesRender() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].render(batch);
        }
        if(user.isProvinceSelected()){
            arrow.activate((int)user.getStartprovinceX(), (int)user.getStartprovinceY(), Gdx.input.getX(), Boot.INSTANCE.getScreenHeight() - Gdx.input.getY(), provinces[user.getFirst_provinceID()].owner);
            provinces[user.getFirst_provinceID()].render(batch);
        }

    }

    public void update() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].update();
        }
       user.sending_troops(provinces);
    }


    public void render() {
        polyBatch.begin();
        for (int i = 0; i < numberOfProvinces; i++) {
            polygonRender(i);
        }
        polyBatch.end();

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());
        batch.end();

    }

    public void polygonRender(int polygonID) {
        if(provinces[polygonID].owner == null || Client.playersColors == null) return;
        System.out.println(provinces[polygonID].owner);
        polySprite[polygonID].setColor((Client.playersColors.get(provinces[polygonID].owner)));
        polySprite[polygonID].draw(polyBatch);
    }

    public void polygonRendererInit() {
        polyBatch = new PolygonSpriteBatch();
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 1, 1);
        pix.fill();
        TextureRegion textureRegion = new TextureRegion(texture);
        EarClippingTriangulator triangulator = new EarClippingTriangulator();

        ShortArray[] trianfleIndicies = new ShortArray[numberOfProvinces];
        PolygonRegion[] polyReg = new PolygonRegion[numberOfProvinces];
        polySprite = new PolygonSprite[numberOfProvinces];

        for (int i = 0; i < numberOfProvinces; i++) {
            trianfleIndicies[i] = triangulator.computeTriangles(vertices[i]);
            polyReg[i] = new PolygonRegion(textureRegion, vertices[i], trianfleIndicies[i].toArray());
            polySprite[i] = new PolygonSprite(polyReg[i]);
        }
    }


    private void jsonRead() throws Exception{
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/maps/map_1.json"));
        JSONObject jsonObject = (JSONObject) obj;
        pngPath = (String) jsonObject.get("maps/prov1_borders.png");
        JSONArray provinces = (JSONArray) jsonObject.get("provinces");
        Iterator<?> iterator = provinces.iterator();


        vertices = new float[provinces.size()][];
        blobCoordinates = new int[provinces.size()][];

        int ind = 0;
        while (iterator.hasNext()) {
            JSONObject province = (JSONObject) iterator.next();
            JSONArray coords = (JSONArray) province.get("coords");
            JSONArray blobCoords = (JSONArray) province.get("blobCoords");
            float[] coordsArray = new float[coords.size()];
            int[] blobCoordsArray = new int[blobCoords.size()];
            for (int i = 0; i < coords.size(); i++){
                coordsArray[i] = Float.parseFloat(coords.get(i).toString());
                if(i % 2 == 1) {
                    coordsArray[i] -= (1018 - Boot.INSTANCE.getScreenHeight()) * 0.55;
                }
            }

            for (int i = 0; i < blobCoords.size(); i++) {
                blobCoordsArray[i] = Integer.parseInt(blobCoords.get(i).toString());
                if(i % 2 == 1) {
                    blobCoordsArray[i] -= (1018 - Boot.INSTANCE.getScreenHeight()) * 0.55;
                }
            }
            vertices[ind] = coordsArray;
            blobCoordinates[ind] = blobCoordsArray;
            ind++;
        }
    }
}

/*
POLYGON INFO COLLECTOR

if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.print(Gdx.input.getX() + "," + (1080 - Gdx.input.getY() - 64) + ",");
        }

PROVINCES CORDS COLLECTOR
if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.print("{" + (Gdx.input.getX() - 50 ) + "," + (Gdx.input.getY() + 50) + "},");
        }
 */
