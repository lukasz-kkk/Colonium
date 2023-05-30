package Utils;

import aurelienribon.tweenengine.TweenAccessor;
import core.GameScreen;
import objects.Unit;

public class AnimationAccessor implements TweenAccessor<Unit> {
    public static final int POSITION_XY = 1;

    @Override
    public int getValues(Unit target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Unit target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY:
                target.setPosition(newValues[0], newValues[1]);
                break;
        }
    }
}
