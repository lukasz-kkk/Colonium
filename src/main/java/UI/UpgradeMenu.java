package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import objects.Map;
import objects.Province;

public class UpgradeMenu {
    private final Texture menuTexture;
    private final SpriteBatch batch;
    Province targetProvince;
    private int show;

    public UpgradeMenu() {
        this.menuTexture = new Texture("UI_elements/upgrade_menu.png");
        this.batch = new SpriteBatch();
        targetProvince = null;
        show = 0;
    }

    public void show() {
        if (show == 0) return;
        batch.begin();

        batch.draw(menuTexture, targetProvince.getXposition() - 6, targetProvince.getYposition() - 6, 299, 242);

        batch.end();
    }

    public void update() {
        processInput();
    }

    public void processInput() {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            show = 0;
            targetProvince = null;
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            for (int i = 0; i < Map.numberOfProvinces; i++) {
                float provinceX = Map.provinces[i].getXposition();
                float provinceY = Map.provinces[i].getYposition();
                float distance = (float) Math.sqrt(Math.pow(mouseX - provinceX, 2) + Math.pow(mouseY - provinceY, 2));
                if (distance <= 20) {
                    targetProvince = Map.provinces[i];
                    show = 1;
                }
            }
        }
    }

}
