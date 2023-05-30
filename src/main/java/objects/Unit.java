package objects;

import Utils.AnimationAccessor;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Unit {
    ShapeRenderer shapeRenderer;
    private TweenManager tweenManager;

    int ID;

    float Xpos, Ypos;
    final float targetX;
    final float targetY;
    float SPEED = 1f;
    public static int RADIUS = 5;

    public Unit(int sourceX, int sourceY, int destinationX, int destinationY) {
        Xpos = sourceX;
        Ypos = sourceY;
        shapeRenderer = new ShapeRenderer();
        tweenManager = new TweenManager();

        targetX = destinationX;
        targetY = destinationY;

        float distance = (float) Math.sqrt(Math.pow(targetX - Xpos, 2) + Math.pow(targetY - Ypos, 2));
        float duration = distance / SPEED;

        Tween.registerAccessor(Unit.class, new AnimationAccessor());
        Tween.to(this, AnimationAccessor.POSITION_XY, duration)
                .target(targetX, targetY)
                .ease(TweenEquations.easeNone)
                .start(tweenManager);
    }

    public void render() {
        tweenManager.update(1f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.circle(Xpos, Ypos, RADIUS);

        shapeRenderer.end();
    }

    public void setColor(int owner){
        if (owner == 0)
            shapeRenderer.setColor(new Color(Color.WHITE));
        if (owner == 1)
            shapeRenderer.setColor(new Color(Color.BLUE));
        if (owner == 2)
            shapeRenderer.setColor(new Color(Color.RED));
        if (owner == 3)
            shapeRenderer.setColor(new Color(Color.GREEN));
        if (owner == 4)
            shapeRenderer.setColor(new Color(Color.YELLOW));
    }

    public void dispose() {
        shapeRenderer.dispose();
        tweenManager.killTarget(this);
        shapeRenderer = null;
        tweenManager = null;
    }

    public float getX() {
        return Xpos;
    }

    public float getY() {
        return Ypos;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setPosition(float x, float y) {
        Xpos = x;
        Ypos = y;
    }
}
