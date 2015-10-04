package se.jnsoft.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by johann on 2015-10-04.
 */
public class Helpers {

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    public static File getNewImageFile(boolean privateStore) throws IOException {
        File image = File.createTempFile(getPhotoFileName("Selfie"), ".jpg", getPhotoStorageDir(privateStore));
        return image;
    }

    public static File getPhotoStorageDir(boolean privateStore){
        if(privateStore) // use private for smaller application provate data sets
            return Environment.getExternalStorageDirectory();
        else // use exernal for larger non-private data sets
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static String getPhotoFileName(String tag){
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        if(tag != "")
            return tag + "_" + timeStamp + "_";
        else
            return  timeStamp;
    }

    public static String getPhotoName(String path){
        String[] parts = path.split("_");
        return  parts[1].substring(0,4) + "-" +
                parts[1].substring(4,6) + "-" +
                parts[1].substring(6,8) + " " +
                parts[2].substring(0,2) + ":" +
                parts[2].substring(2,4) + ":" +
                parts[2].substring(4,6);
    }

    @NonNull
    public static Bitmap getBitmapFromFile(File f){
        if(f.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
            return bitmap;
        }
        return null;
    }

    public static Bitmap getScaledBitmap(String path, int width, int height){

        // get dims of bitmap:
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor;

        if(width > 0 && height > 0) {
            scaleFactor = Math.min(photoW/width, photoH/height);
        }
        else {
            scaleFactor = 1;
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        // bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return bitmap;
    }

    // wrapper for deleteAllFiles(File f)
    public static void deleteAllSelfieFiles(boolean privateStore){
        File f = getPhotoStorageDir(privateStore);
        deleteAllFiles(f);
    }

    private static void deleteAllFiles(File f){
        if(f.isDirectory()){
            for (File f2 : f.listFiles())
                deleteAllFiles(f2);
        }
        else
            f.delete();
    }
}
