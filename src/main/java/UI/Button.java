package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import core.Boot;
import core.UserInput;

public class Button {
    private Texture buttonTexture;
    private final Texture buttonTouchTexture;
    private final Texture buttonIdleTexture;
    SpriteBatch batch;
    BitmapFont font;
    private final String text;
    boolean clicked;
    private final int width;
    private final int height;
    private final int xPos;
    private final int yPos;

    private int additionalXOffset = 0;
    private int additionalYOffset = 0;

    public Button(String text, int xPos, int yPos, int width, int height, SpriteBatch batch, BitmapFont font){
        this.batch = batch;
        this.font = font;
        this.buttonIdleTexture = new Texture("UI_elements/button_idle.png");
        this.buttonTouchTexture = new Texture("UI_elements/button_touch.png");
        this.text = text;
        this.xPos = xPos;
        this.yPos = Boot.INSTANCE.getScreenHeight() - yPos;
        this.width = width;
        this.height = height;
        clicked = false;
    }

    public void update(){
        clicked = false;
        buttonTexture = buttonIdleTexture;
        mouseHandle();
    }

    public void render(){
        batch.draw(buttonTexture, xPos, yPos, width, height);
        int fontOffset = 10 * text.length();

        float xPosFont = xPos + (width / 2f) - fontOffset + additionalXOffset;
        float yPosFont = yPos + height - 30 + additionalYOffset;
        font.draw(batch, text, xPosFont, yPosFont);
    }

    public void mouseHandle() {
        int mouseX = UserInput.getMouseX();
        int mouseY = UserInput.getMouseY();

        if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
            buttonTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                clicked = true;
        }
    }

    public boolean isClicked(){
        return clicked;
    }

    public void setAdditionalXOffset(int value) {
        additionalXOffset = value;
    }

    public void setAdditionalYOffset(int value) {
        additionalYOffset = value;
    }

}
