package core;

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
        //GameScreen.unitHandler.sendUnits(src, dst);
        Client.message = MessageUtility.createAttackJSON(src, dst);
    }
//    public void messageReciver(Province[] provinces)
//    {
//        if (Client.jsonResponse!=null)
//        {
//            String type = Client.jsonResponse.get("type").toString();
//            if(type.equals("attack"))
//            {
//                String srcs = Client.jsonResponse.get("src").toString();
//                int src = Integer.parseInt(srcs);
//                srcs = Client.jsonResponse.get("dsc").toString();
//                int dsc = Integer.parseInt(srcs);
//                provinces[dsc].setValue(provinces[dsc].getValue()+provinces[src].getValue());
//                provinces[src].setValue(0);
//            }
//        }
//    }
    public void sending_troops(Province[] provinces)
    {
        float endX=0;
        float endY=0;
        float provinceX=0,provinceY=0;
        if(Gdx.input.isTouched())
        {
            if(first_province_selected ==0)
            {
                float startX = Gdx.input.getX();
                float startY = Gdx.graphics.getHeight() - Gdx.input.getY();
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
            endX = Gdx.input.getX();
            endY =Gdx.graphics.getHeight() - Gdx.input.getY();
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
}
