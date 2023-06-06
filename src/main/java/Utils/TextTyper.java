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
                        text = text.substring(0, text.length() - 1); // Backspace
                } else {
                    text += (char) (keyPressed + 36);
                }
            }
        }
        newText = text;
        return newText;
    }
}
