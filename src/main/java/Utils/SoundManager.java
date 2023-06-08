package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager
{
    public Sound movesound = Gdx.audio.newSound(Gdx.files.internal("sounds/movesound.mp3"));
    public Sound loopsound = Gdx.audio.newSound(Gdx.files.internal("sounds/loop.mp3"));

}
