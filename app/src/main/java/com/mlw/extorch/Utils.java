package com.mlw.extorch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Utils {

    private Utils(){}

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String compress_string(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }/*w  w w. ja  va 2s.c om*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    public static List<String> read_dir_files(String dir_path){
        File folder = new File(dir_path);
        File[] listOfFiles = folder.listFiles();
        List<String> file_names = new ArrayList<String>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
//                System.out.println("File " + listOfFiles[i].getName());
                file_names.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return file_names;
    }

    public static String b64_url_encoding(String b64) {
        if (b64 == null) {
            return "";
        }
        String encoded_b64 = b64.replace("+", "-");
        encoded_b64 = encoded_b64.replace("/", "_");
        encoded_b64 = encoded_b64.replaceAll(System.lineSeparator(),"");
        return encoded_b64;
    }

}
