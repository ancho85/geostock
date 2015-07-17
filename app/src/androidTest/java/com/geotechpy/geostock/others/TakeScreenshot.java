package com.geotechpy.geostock.others;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.os.Environment;

public class TakeScreenshot{

    public static void takeScreenshot(String name, Activity activity)
    {

        // In Testdroid Cloud, taken screenshots are always stored
        // under /test-screenshots/ folder and this ensures those screenshots
        // be shown under Test Results
        String path=
                Environment.getExternalStorageDirectory().getAbsolutePath()+"/test-screenshots/"+name;

        View scrView=activity.getWindow().getDecorView().getRootView();
        scrView.setDrawingCacheEnabled(true);
        Bitmap bitmap=Bitmap.createBitmap(scrView.getDrawingCache());
        scrView.setDrawingCacheEnabled(false);

        OutputStream out=null;
        File imageFile=new File(path);

        try{
            out=new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
        }catch(FileNotFoundException e){
            // exception
        }catch(IOException e){
            // exception
        }finally{

            try{
                if(out!=null){
                    out.close();
                }

            }catch(Exception exc){
            }

        }
    }

}
