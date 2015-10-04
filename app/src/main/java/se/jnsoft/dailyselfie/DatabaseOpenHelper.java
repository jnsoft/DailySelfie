package se.jnsoft.dailyselfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by johann on 2015-10-04.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    final private static String DB_NAME = "selfies_db";
    final private static Integer VERSION = 1;
    public final static String TABLE_NAME = "selfies";
    public final static String ID_COL = "_id"; // must be _id or _ID for CursorAdapter to work
    public final static String NAME_COL = "name";
    public final static String PIC_PATH_COL = "picPath";

    public final static String[] columns = { ID_COL, NAME_COL, PIC_PATH_COL };

    final private static String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NAME_COL + " TEXT NOT NULL, "
                    + PIC_PATH_COL + " TEXT NOT NULL)";

    final private Context mContext;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_CMD); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    private void deleteDatabase() { mContext.deleteDatabase(DB_NAME); }
}
