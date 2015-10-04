package se.jnsoft.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by johann on 2015-10-04.
 */
public class SelfiesDatabase {

    private final Context mContext;
    private SQLiteDatabase mDB;
    private DatabaseOpenHelper mDBHelper;

    public SelfiesDatabase(Context context) {
        mContext = context;
        mDBHelper = new DatabaseOpenHelper(mContext);
    }

    public void open() throws SQLException {
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() { mDBHelper.close(); }

    public long insert(String name, String path) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(mDBHelper.NAME_COL, name);
        initialValues.put(mDBHelper.PIC_PATH_COL, path);
        return mDB.insert(mDBHelper.TABLE_NAME, null, initialValues);
    }

    public boolean update(long id, String name, String path) {
        ContentValues args = new ContentValues();
        args.put(mDBHelper.NAME_COL, name);
        args.put(mDBHelper.PIC_PATH_COL, path);
        return mDB.update(mDBHelper.TABLE_NAME, args, mDBHelper.ID_COL + "=" + id, null) > 0;
    }

    public Cursor get(long id) throws SQLException {
        // distinct, table, selection, where, gruop by, having, orderby, limit
        Cursor mCursor = mDB.query(true, mDBHelper.TABLE_NAME, mDBHelper.columns, mDBHelper.ID_COL + "=" + id, null, null, null, null, null);

        if(mCursor != null)
            mCursor.moveToFirst();

        return mCursor;
    }

    public Cursor getAll() {
        String orderBy =  mDBHelper.ID_COL + " DESC";
        return mDB.query(mDBHelper.TABLE_NAME, mDBHelper.columns, null, null, null, null, orderBy);
    }

    public boolean delete(long id) { return mDB.delete(mDBHelper.TABLE_NAME, mDBHelper.ID_COL + "=" + id, null) > 0; }

    public boolean deleteAll() { return mDB.delete(mDBHelper.TABLE_NAME, null, null) > 0; }




}
