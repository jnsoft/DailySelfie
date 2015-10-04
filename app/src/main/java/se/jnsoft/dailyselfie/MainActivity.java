package se.jnsoft.dailyselfie;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int TWO_MINS = 1000 * 60 * 2;

    private String mCurrentPhotoPath;
    private File mCurrentPic;
    private MainActivityFragment mSelfieList;

    public static final boolean USE_PRIVATE_STORE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelfieList = new MainActivityFragment();
        if(savedInstanceState == null)
            getFragmentManager().beginTransaction().add(R.id.fragment, mSelfieList).commit();
        setAlarm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_takePhoto:
                takeSelfie();
                return true;
            case R.id.menu_delete:
                deleteSelfie();
                return true;
            case R.id.menu_clearAll:
                clearAll();
                return true;
            case R.id.menu_selfie_delete:
                deleteSelfie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeSelfie() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                mCurrentPic = Helpers.getNewImageFile(USE_PRIVATE_STORE);
                mCurrentPhotoPath = mCurrentPic.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPic));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelfie() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_delete));
        builder.setPositiveButton(getString(R.string.dialog_yes), dialogDeleteSelfieClickListener);
        builder.setNegativeButton(getString(R.string.dialog_no), dialogDeleteSelfieClickListener).show();
    }

    private void clearAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_clear_all));
        builder.setPositiveButton(getString(R.string.dialog_yes), dialogClearAllClickListener);
        builder.setNegativeButton(getString(R.string.dialog_no), dialogClearAllClickListener).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // galleryAddPic(mCurrentPhotoPath);
            String name = Helpers.getPhotoName(mCurrentPhotoPath);
            Selfie newSelfie = new Selfie(name, mCurrentPhotoPath);
            newSelfie.setBitmap(Helpers.getBitmapFromFile(mCurrentPic));
            mSelfieList.addSelfie(newSelfie);
        }
    }

    @Override
    public void onBackPressed() { // handle back press from opened selfie
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    DialogInterface.OnClickListener dialogDeleteSelfieClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    mSelfieList.deleteSelfie();
                    getFragmentManager().popBackStack();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogClearAllClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    mSelfieList.clearAll();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setAlarm() {

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // set a repeating alarm
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = TWO_MINS;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }



}
