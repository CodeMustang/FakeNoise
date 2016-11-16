package com.mssoft.fakenoise.Utilities;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Marius on 9/3/2016.
 */
public abstract class Utils {

    @SuppressWarnings("deprecation")
    public static int[] getScreenWidthAndHeight(Context context){

        int widthHeight[] = new int[2];
        int measuredWidth = 0;
        int measuredHeight = 0;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){

            Point size = new Point();
            windowManager.getDefaultDisplay().getSize(size);

            measuredWidth = size.x;
            measuredHeight = size.y;

            widthHeight[0] = measuredWidth;
            widthHeight[1] = measuredHeight;

        }

        else{
            Display d = windowManager.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();

            widthHeight[0] = measuredWidth;
            widthHeight[1] = measuredHeight;
        }

        return widthHeight;
    }

    public static String getCleanPhoneNumber(String string){
        StringBuilder character = new StringBuilder()
                     ,builder   = new StringBuilder();

        for(int i = 0 ; i < string.length();i++){
            character.append(string.charAt(i));

                if(character.toString().matches("[^-() ]")) {
                    builder.append(character);
                }
            character.delete(0,1);
            }

        return builder.toString();
    }

}
