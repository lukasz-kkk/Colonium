package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class TextTyper {

    public String enterText(String text){
        String newText = text;
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            int keyPressed = -1;
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    keyPressed = i;
                    break;
                }
            }
            if (keyPressed != -1) {
                if (keyPressed == 67) {
                    if (text.length() > 0)
                        newText = newText.substring(0, text.length() - 1); // Backspace
                }
                else if(keyPressed == Input.Keys.SPACE){
                    newText += " ";
                }
                else if((keyPressed >= 29 && keyPressed <= 54) || (keyPressed >= 7 && keyPressed <= 16)) {
                    newText += Input.Keys.toString(keyPressed);
                }
            }
        }
        return newText;
    }
}
