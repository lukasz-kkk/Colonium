package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager
{
    private long lastMoveSound = -1;
    private long lastClickdSound = -1;
    private long lastErrorSound = -1;
    private long lasUpgradeSound = -1;
    public Sound movesound = Gdx.audio.newSound(Gdx.files.internal("sounds/movesound.mp3"));
    public Sound loopsound = Gdx.audio.newSound(Gdx.files.internal("sounds/loop.mp3"));
    public Sound clicksound = Gdx.audio.newSound(Gdx.files.internal("sounds/clickv2.wav"));
    public Sound errorsound = Gdx.audio.newSound(Gdx.files.internal("sounds/error.wav"));
    public Sound upgradesound = Gdx.audio.newSound(Gdx.files.internal("sounds/upgrade.wav"));

    public void moveplayer()
    {
        if(lastMoveSound==-1)
        {
            lastMoveSound = movesound.play(0.1f);
        }
        else
        {
            movesound.stop(lastMoveSound);
            lastMoveSound = movesound.play(0.1f);
        }
    }
    public void upgradeplayer()
    {
        if(lasUpgradeSound==-1)
        {
            lasUpgradeSound = upgradesound.play(0.1f);
        }
        else
        {
            upgradesound.stop(lasUpgradeSound);
            lasUpgradeSound = upgradesound.play(0.1f);
        }
    }
    public void clickplayer()
    {
        if(lastClickdSound==-1)
        {
            lastClickdSound = clicksound.play(0.1f);
        }
        else
        {
            clicksound.stop(lastClickdSound);
            lastClickdSound = clicksound.play(0.1f);
        }
    }
    public void errorplayer()
    {
        if(lastErrorSound==-1)
        {
            lastErrorSound = errorsound.play(0.1f);
        }
        else
        {
            errorsound.stop(lastErrorSound);
            lastErrorSound = errorsound.play(0.1f);
        }
    }
    public void loopplayer()
    {
        long loop = loopsound.play(0.05f);
        loopsound.setLooping(loop,true);
    }
}
