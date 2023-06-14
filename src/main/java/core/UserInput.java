package core;

import Utils.SoundManager;
import com.badlogic.gdx.Gdx;
import objects.Map;
import objects.Province;
import java.lang.Math;

public class UserInput
{
    int first_provinceID =0;
    int second_provinceID =0;
    int first_temp=-1;
    int second_temp=-2;
    int first_province_selected =0;
    int second_province_selected =0;
    float startprovinceX=0,startprovinceY=0;
    public void send_move(int src, int dst)
    {
        Client.message = MessageUtility.createAttackJSON(src, dst);
        Boot.sm.moveplayer();
    }
    public void sending_troops(Province[] provinces)
    {
        float endX=0;
        float endY=0;
        float provinceX=0,provinceY=0;
        if(Gdx.input.isTouched())
        {
            if(first_province_selected ==0)
            {
                float startX = getMouseX();
                float startY = getMouseY();
                for(first_provinceID=0; first_provinceID <Map.numberOfProvinces; first_provinceID++)
                {
                    startprovinceX = provinces[first_provinceID].getXposition();
                    startprovinceY = provinces[first_provinceID].getYposition();

                    if(Math.sqrt(Math.pow(startX-(startprovinceX),2)+Math.pow((startY-startprovinceY),2))<=20)
                    {
                        first_temp=first_provinceID;
                        first_province_selected =1;
                        break;
                    }
                }
                if(first_province_selected !=1) first_temp =-1;
            }
        }
        else if(first_province_selected ==1 && !Gdx.input.isTouched())
        {
            endX = getMouseX();
            endY = getMouseY();
            for(second_provinceID=0; second_provinceID <Map.numberOfProvinces; second_provinceID++)
            {
                provinceX = provinces[second_provinceID].getXposition();
                provinceY = provinces[second_provinceID].getYposition();

                if(Math.sqrt(Math.pow(endX-(provinceX),2)+Math.pow((endY-provinceY),2))<=20)
                {
                    second_temp = second_provinceID;
                    second_province_selected =1;
                    break;
                }
            }
            if(second_province_selected !=1) second_temp =-2;
            first_province_selected =0;
        }

        if(second_province_selected ==1 && first_temp != second_temp)
        {
            send_move(first_provinceID, second_provinceID);
            second_province_selected =0;
            first_temp =-1;
            second_temp =-2;
        }
        else if(first_temp == second_temp)
        {
            first_temp =-1;
            second_temp =-2;
            second_province_selected =0;
            first_province_selected =0;
        }
    }
    public boolean isProvinceSelected(){
        return first_province_selected == 1;
    }

    public float getStartprovinceX() {
        return startprovinceX;
    }

    public float getStartprovinceY() {
        return startprovinceY;
    }

    public int getFirst_provinceID() {
        return first_provinceID;
    }

    public static int getMouseX(){
        int mouseX = Gdx.input.getX();
        float windowWidth = Gdx.graphics.getWidth();
        float originalWidth = 1920;
        mouseX = (int)((mouseX / windowWidth) * originalWidth);
        return mouseX;
    }

    public static int getMouseY(){
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        float windowHeight = Gdx.graphics.getHeight();
        float originalHeight = 1020;

        mouseY = (int)((mouseY / windowHeight) * originalHeight);
        return mouseY;
    }
}
