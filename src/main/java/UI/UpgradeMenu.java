package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
                if(Client.gold >= 100L * targetProvince.manLvl)
                {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "man", 100 * targetProvince.manLvl);
                    targetProvince.manLvl++;
                }

            } else if (mouseY >= update_2y && mouseY <= update_2y + 30 && mouseX >= update_x && mouseX <= update_x + 60) {
                System.out.println("CAPACITY");
                if(Client.gold >= 100L * targetProvince.capLvl) {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "cap", 10 * targetProvince.capLvl);
                    targetProvince.capLvl += 10;
                }

            } else if (mouseY >= update_3y && mouseY <= update_3y + 30 && mouseX >= update_x && mouseX <= update_x + 60) {
                System.out.println("INCOME");
                if(Client.gold >= 100L * targetProvince.incLvl) {
                    Client.message = MessageUtility.createUpgradeRequest(Client.currentLobby, targetProvince.ID, "inc", 100 * targetProvince.incLvl);
                    targetProvince.incLvl++;
                }

            } else {
                show = 0;
                targetProvince = null;
            }
        }
    }

}