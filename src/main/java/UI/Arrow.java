package UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import core.Client;

import java.util.Arrays;
import java.lang.Math;

public class Arrow {
    PolygonSpriteBatch polyBatch;
    private Texture pixTexture;
    private final Texture texture;
    PolygonSprite polySprite;
    ShortArray triangleIndicies;
    PolygonRegion polyReg;
    EarClippingTriangulator triangulator;
    TextureRegion textureRegion;
    Pixmap pix;

    String provinceOwner;
    float provinceX, provinceY;
    float localProvinceX, localProvinceY;
    float mouseX, mouseY;
    float localMouseX, localMouseY;
    float slope, perpSlope;

    float offset = 10;
    float[] vertices = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public Arrow() {
        this.texture = new Texture("white_transparency.png");
        polygonRendererInit();
    }

    public void activate(int provinceX, int provinceY, int mouseX, int mouseY, String provinceOwner) {
        if (provinceX == mouseX && provinceY == mouseY) return;
        if (Math.abs(provinceX - mouseX) <= 10 && Math.abs(provinceY - mouseY) <= 10) return;
        this.provinceX = provinceX;
        this.provinceY = provinceY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.provinceOwner = provinceOwner;

        calculate();
        updatePolygonVertices(vertices);
        render();
    }

    void calculate() {
        // creating local coordinates system
        localMouseX = mouseX - provinceX;
        localMouseY = mouseY - provinceY;
        localProvinceX = 0;
        localProvinceY = 0;

        vertices[8] = mouseX;
        vertices[9] = mouseY;
        if (localMouseX == 0) localMouseX = 1;
        if (localMouseY == 0) localMouseY = 1;
        // the slope of the line passing through the province and mouse coordinates
        slope = localMouseY / localMouseX;
        // the slope of the line perpendicular to the line through the province and mouse coordinates
        perpSlope = -(localMouseX / localMouseY);
        offset = 5 + 5 * Math.abs(slope);
        if (mouseX - provinceX == 0) {
            mouseX = mouseX - 1;
        }
        if (mouseY - provinceY == 0) {
            mouseY = mouseY - 1;
        }
        vertices[0] = (0 - offset) / (slope - perpSlope);
        vertices[1] = vertices[0] * perpSlope;

        vertices[2] = (0 + offset) / (slope - perpSlope);
        vertices[3] = vertices[2] * perpSlope;

        // offset
        vertices[0] += provinceX;
        vertices[1] += provinceY;

        vertices[2] += provinceX;
        vertices[3] += provinceY;

        ////
        vertices[4] = (0 + offset) / (slope - perpSlope);
        vertices[5] = vertices[4] * perpSlope;


        ////
        vertices[6] = (0 + offset * 2.5f) / (slope - perpSlope);
        vertices[7] = vertices[6] * perpSlope;


        ////
        vertices[10] = (0 - offset * 2.5f) / (slope - perpSlope);
        vertices[11] = vertices[10] * perpSlope;


        ////
        vertices[12] = (0 - offset) / (slope - perpSlope);
        vertices[13] = vertices[12] * perpSlope;

        int distance = (int) (Math.abs(localMouseX) + Math.abs(localMouseY));
        for (int i = 4; i < 14; i += 2) {
            if (i == 8) continue; // Mouse
            vertices[i] += mouseX - localMouseX * (0.15 / (distance / 180.0));
            vertices[i + 1] += mouseY - localMouseY * (0.15 / (distance / 180.0));
        }
    }

    void render() {
        setColor();
        polyBatch.begin();

        polySprite.draw(polyBatch);

        polyBatch.end();
    }

    void setColor(){
        if (provinceOwner == null) return;
        Color color = Client.playersColors.get(provinceOwner);
        if(color == null) return;
        polySprite.setColor(color);
    }
    public void polygonRendererInit() {
        polyBatch = new PolygonSpriteBatch();
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 1, 1, 0.5f);
        pix.fill();
        pixTexture = new Texture(pix);
        textureRegion = new TextureRegion(texture);
        triangulator = new EarClippingTriangulator();

        triangleIndicies = triangulator.computeTriangles(vertices);
        polyReg = new PolygonRegion(textureRegion, vertices, triangleIndicies.toArray());
        polySprite = new PolygonSprite(polyReg);
    }

    public void updatePolygonVertices(float[] newVertices) {
        vertices = newVertices;

        triangleIndicies = triangulator.computeTriangles(vertices);


        polyReg = new PolygonRegion(textureRegion, vertices, triangleIndicies.toArray());
        polySprite = new PolygonSprite(polyReg);
        polySprite.setColor(new Color(Color.YELLOW));
    }

    public void dispose() {
        if (polyBatch != null) {
            polyBatch.dispose();
            polyBatch = null;
        }

        if (pix != null) {
            pix.dispose();
            pix = null;
        }

        if (pixTexture != null) {
            pixTexture.dispose();
            pixTexture = null;
        }

        if (textureRegion != null) {
            Texture texture = textureRegion.getTexture();
            if (texture != null) {
                texture.dispose();
            }
            textureRegion = null;
        }

        if (triangulator != null) {
            triangulator = null;
        }

        if (polyReg != null) {
            Texture texture = polyReg.getRegion().getTexture();
            if (texture != null) {
                texture.dispose();
            }
            polyReg = null;
        }
        polySprite = null;
    }

}

