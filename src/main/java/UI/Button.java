package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import core.Boot;

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
        font.draw(batch, text, xPos + width / 2f - fontOffset, yPos + height - 30);
    }

    public void mouseHandle() {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
            buttonTexture = buttonTouchTexture;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                clicked = true;
        }
    }

    public boolean isClicked(){
        return clicked;
    }
}
