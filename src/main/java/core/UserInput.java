package core;

import com.badlogic.gdx.Gdx;
import objects.Map;
import objects.Province;

public class UserInput
{
    int i=0;
    int j=0;
    int check=0;
    int check2=0;
    public void sneding_troops(Province[] provinces)
    {
        float endX=0;
        float endY=0;
        int mistakeX = (Province.WIDTH);
        int mistakeY = (Province.HEIGHT);
        float provinceX=0,provinceY=0;
        float startprovinceX=0,startprovinceY=0;
        if(Gdx.input.isTouched())
        {
            if(check==0)
            {
                float startX = Gdx.input.getX();
                float startY = Gdx.graphics.getHeight() - Gdx.input.getY();
                for(;i<Map.numberOfProvinces;i++)
                {
                    startprovinceX = provinces[i].getXposition();
                    startprovinceY = provinces[i].getYposition();

                    if((startX>=(startprovinceX-mistakeX) && startX<=(startprovinceX+mistakeX)) && (startY>=(startprovinceY-mistakeY) && startY<=(startprovinceY+mistakeY)))
                    {
                        check=1;
                        break;
                    }
                }
                if(check!=1) i=0;
            }
        }
        else if(check==1 && !Gdx.input.isTouched())
        {
            endX = Gdx.input.getX();
            endY =Gdx.graphics.getHeight() - Gdx.input.getY();
            for(;j<Map.numberOfProvinces;j++)
            {
                provinceX = provinces[j].getXposition();
                provinceY = provinces[j].getYposition();

                if((endX>=(provinceX-mistakeX) && endX<=(provinceX+mistakeX)) && (endY>=(provinceY-mistakeY) && endY<=(provinceY+mistakeY)))
                {
                    check2=1;
                    break;
                }
            }
            if(check2!=1) j=0;
            check=0;
        }

        if(check2==1 && i != j)
        {
            provinces[j].setValue(provinces[j].getValue()+provinces[i].getValue());
            provinces[i].setValue(0);
            check2=0;
            i=0;
            j=0;
        }
        else if(i==j)
        {
            i=0;
            j=0;
            check2=0;
            check=0;
        }

    }
}
