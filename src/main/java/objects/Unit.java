package objects;

import Utils.AnimationAccessor;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import core.Client;

public class Unit {
    private TweenManager tweenManager;
    ShapeRenderer shapeRenderer;
    int ID;
    String owner;
    float Xpos, Ypos;
    final float targetX;
    final float targetY;
    float SPEED = 1f;
    public static int RADIUS = 5;

    public Unit(String owner, int sourceX, int sourceY, int destinationX, int destinationY, ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        this.owner = owner;

        Xpos = sourceX;
        Ypos = sourceY;

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
        setColor();
        shapeRenderer.circle(Xpos, Ypos, RADIUS);
    }

    public void setColor(){
        if (owner == null) return;
        shapeRenderer.setColor(Client.playersColors.get(owner));
    }

    public void dispose() {
        tweenManager.killTarget(this);
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
