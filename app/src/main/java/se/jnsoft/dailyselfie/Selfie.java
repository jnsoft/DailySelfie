package se.jnsoft.dailyselfie;

import android.graphics.Bitmap;

/**
 * Created by johann on 2015-10-04.
 */
public class Selfie {

    private int mId;
    private String mName;
    private String mPicPath;
    private Bitmap mPic;

    public Selfie(String name, String path) { mName = name; mPicPath = path; }
    public Selfie(int id, String name, String path) { mId = id; mName = name; mPicPath = path; }

    public int getId(){ return mId; }
    public String getPath(){ return mPicPath; }
    public Bitmap getBitmap(){ return mPic; }

    public void setBitmap(Bitmap pic) {
        mPic = pic;
    }

    @Override
    public String toString() {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //return dateFormat.format(mCreatedDate);
        return mName;
    }


}
