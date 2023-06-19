package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import core.Client;
import core.MessageUtility;
import core.UserInput;
import objects.Map;
import objects.Province;

public class UpgradeMenu {
    private final Texture menuTexture;
    private final SpriteBatch batch;
    Province targetProvince;
    private int show;
    private final BitmapFont font;
    private final BitmapFont goldFont;

    int manPrice;
    int capPrice;
    int incPrice;

    public UpgradeMenu() {
        this.menuTexture = new Texture("UI_elements/upgrade_menu.png");
        this.batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/Bebas22px.fnt"), Gdx.files.internal("fonts/Bebas22px.png"), false);
        font.getData().setScale(1f);
        goldFont = new BitmapFont(Gdx.files.internal("fonts/Bebas16px.fnt"), Gdx.files.internal("fonts/Bebas16px.png"), false);
        goldFont.getData().setScale(1f);
        targetProvince = null;
        show = 0;
    }

    public void show() {
        if (show == 0) return;
        batch.begin();

        batch.draw(menuTexture, targetProvince.getXposition() - 6, targetProvince.getYposition() - 6, 299, 242);

        font.draw(batch, String.valueOf(targetProvince.getManLvl()), targetProvince.getXposition() + 66, targetProvince.getYposition() + 175);
        font.draw(batch, String.valueOf(targetProvince.getCapLvl()), targetProvince.getXposition() + 64, targetProvince.getYposition() + 110);
        font.draw(batch, String.valueOf(targetProvince.getIncLvl()), targetProvince.getXposition() + 66, targetProvince.getYposition() + 55);

        priceUpdate();

        setColor(manPrice);
        goldFont.draw(batch, String.valueOf(manPrice), targetProvince.getXposition() + 213, targetProvince.getYposition() + 168);
        setColor(capPrice);
        goldFont.draw(batch, String.valueOf(capPrice), targetProvince.getXposition() + 213, targetProvince.getYposition() + 108);
        setColor(incPrice);
        goldFont.draw(batch, String.valueOf(incPrice), targetProvince.getXposition() + 213, targetProvince.getYposition() + 48);

        batch.end();
    }

    private void setColor(int value) {
        if (Client.gold <= value)
            goldFont.setColor(Color.SALMON);
        else {
            goldFont.setColor(Color.WHITE);
        }
    }

    public void update() {
        processInput();
    }

    public void processInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            float mouseX = UserInput.getMouseX();
            float mouseY = UserInput.getMouseY();
            for (int i = 0; i < Map.numberOfProvinces; i++) {
                float provinceX = Map.provinces[i].getXposition();
                float provinceY = Map.provinces[i].getYposition();
                float distance = (float) Math.sqrt(Math.pow(mouseX - provinceX, 2) + Math.pow(mouseY - provinceY, 2));
                if (distance <= 20 && Map.provinces[i].owner.equals(Client.clientName)) {
                    targetProvince = Map.provinces[i];
                    show = 1;
                }
            }
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            if (targetProvince == null) return;
            int mouseX = UserInput.getMouseX();
            int mouseY = UserInput.getMouseY();

            float update_x = targetProvince.getXposition() + 190;

            float update_1y = targetProvince.getYposition() + 140;
            float update_2y = targetProvince.getYposition() + 80;
            float update_3y = targetProvince.getYposition() + 20;


            if (mouseY >= update_1y && mouseY <= update_1y + 30 && mouseX >= update_x && mouseX <= update_x + 60) {
                System.out.println("PRODUCE SPEED");
                if (Client.gold >= manPrice) {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "man", manPrice);
                    targetProvince.manLvl++;
                    priceUpdate();
                }

            } else if (mouseY >= update_2y && mouseY <= update_2y + 30 && mouseX >= update_x && mouseX <= update_x + 60) {
                System.out.println("CAPACITY");
                if (Client.gold >= capPrice) {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "cap", capPrice);
                    targetProvince.capLvl += 10;
                    priceUpdate();
                }

            } else if (mouseY >= update_3y && mouseY <= update_3y + 30 && mouseX >= update_x && mouseX <= update_x + 60) {
                System.out.println("INCOME");
                if (Client.gold >= incPrice) {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "inc", incPrice);
                    targetProvince.incLvl++;
                    priceUpdate();
                }

            } else {
                show = 0;
                targetProvince = null;
            }

        }
    }

    private void priceUpdate(){
        manPrice = 100 * targetProvince.manLvl;
        capPrice = 10 * targetProvince.capLvl;
        incPrice = 100 * targetProvince.incLvl;
    }

}