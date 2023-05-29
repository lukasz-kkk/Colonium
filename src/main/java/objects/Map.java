package objects;

import UI.Arrow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import core.Boot;
import core.GameScreen;
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
    private final Texture backgroundTexture;
    public static int numberOfProvinces;
    private Province[] provinces;

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
        this.backgroundTexture = new Texture("prov1_borders.png");

        provincesInit();
        polygonRendererInit();
        testInitValues();
    }

    private void provincesInit() {
        provinces = new Province[numberOfProvinces];
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i] = new Province(blobCoordinates[i][0] + 50, Gdx.graphics.getHeight() - blobCoordinates[i][1] + 50, i, gameScreen);
        }
    }

    UserInput user = new UserInput();
    public void provincesRender() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].render(batch);
        }
        if(user.isProvinceSelected()){
            arrow.activate((int)user.getStartprovinceX(), (int)user.getStartprovinceY(), Gdx.input.getX(), 1020 - Gdx.input.getY(), provinces[user.getFirst_provinceID()].owner);
            provinces[user.getFirst_provinceID()].render(batch);
        }

    }

    public void update() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].update();
        }
       user.sneding_troops(provinces);
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
        if (provinces[polygonID].owner == 0)
            polySprite[polygonID].setColor(new Color(0x8e8e8eff)); // GRAY
        if (provinces[polygonID].owner == 1)
            polySprite[polygonID].setColor(new Color(Color.SKY)); // BLUE
        if (provinces[polygonID].owner == 2)
            polySprite[polygonID].setColor(new Color(Color.SALMON)); // RED
        if (provinces[polygonID].owner == 3)
            polySprite[polygonID].setColor(new Color(Color.OLIVE)); // GREEN
        if (provinces[polygonID].owner == 4)
            polySprite[polygonID].setColor(new Color(Color.GOLD)); // YELLOW

        polySprite[polygonID].draw(polyBatch);
    }

    public void testInitValues() {
        provinces[0].owner = 1;
        provinces[1].owner = 1;
        provinces[2].owner = 1;

        provinces[8].owner = 2;
        provinces[7].owner = 2;
        provinces[15].owner = 2;

        provinces[17].owner = 3;

        provinces[22].owner = 4;
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
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/map_1.json"));
        JSONObject jsonObject = (JSONObject) obj;
        pngPath = (String) jsonObject.get("prov1_borders.png");
        JSONArray provinces = (JSONArray) jsonObject.get("provinces");
        Iterator<?> iterator = provinces.iterator();


        vertices = new float[provinces.size()][];
        blobCoordinates = new int[provinces.size()][];

        int ind = 0;
        while (iterator.hasNext()) {
            JSONObject province = (JSONObject) iterator.next();
            JSONArray coords = (JSONArray) province.get("coords");
            JSONArray blobCoords = (JSONArray) province.get("blobCoords");
            System.out.println(blobCoords);
            float[] coordsArray = new float[coords.size()];
            int[] blobCoordsArray = new int[blobCoords.size()];
            for (int i = 0; i < coords.size(); i++)
                coordsArray[i] = Float.parseFloat(coords.get(i).toString());
            for (int i = 0; i < blobCoords.size(); i++)
                blobCoordsArray[i] = Integer.parseInt(blobCoords.get(i).toString());
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
