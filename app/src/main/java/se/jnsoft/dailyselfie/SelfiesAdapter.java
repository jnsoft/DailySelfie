package se.jnsoft.dailyselfie;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johann on 2015-10-04.
 */
public class SelfiesAdapter extends CursorAdapter {

    private static LayoutInflater inflater = null;
    private final List<Selfie> mItems = new ArrayList<Selfie>();
    private final Context mContext;
    private SelfiesDatabase mDB;


    public SelfiesAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mDB = new SelfiesDatabase(mContext);
        mDB.open();
        this.swapCursor(mDB.getAll()); // reload
    }

    public void add(Selfie selfie) {
        mDB.insert(selfie.toString(), selfie.getPath());
        this.swapCursor(mDB.getAll());
    }

    public void delete(Selfie selfie) {
        mDB.delete(selfie.getId());
        this.swapCursor(mDB.getAll());
    }

    public void clearAll() {
        Helpers.deleteAllSelfieFiles(MainActivity.USE_PRIVATE_STORE); // clear file storage
        mDB.deleteAll(); // clear DB
        this.swapCursor(mDB.getAll());
    }

    public void freeResources() { mDB.close(); }

    @Override
    public int getCount() {	return mItems.size(); }

    @Override
    public Object getItem(int position) { return mItems.get(position); }

    // Get the ID for the item, in this case it's just the position
    @Override
    public long getItemId(int position) { return position; }

    // Creates a View for the Selfie at specified position using the ViewHolder pattern
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View newView = convertView;
//        ViewHolder holder;
//        final Selfie item = mItems.get(position);
//
//        if(convertView == null) {
//            holder = new ViewHolder();
//            newView = inflater.inflate(R.layout.selfie, parent, false);
//            holder.pic = (ImageView) newView.findViewById(R.id.selfie_pic_image_view);
//            holder.name = (TextView) newView.findViewById(R.id.selfie_name_text_view);
//            newView.setTag(holder);
//        }
//        else { holder = (ViewHolder) newView.getTag(); }
//
//        holder.pic.setImageBitmap(item.getBitmap());
//        holder.name.setText(item.toString());
//
//        return newView;
//    }

    private Selfie getSelfieFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.ID_COL));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.NAME_COL));
        String picturePath = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PIC_PATH_COL));
        Selfie selfie = new Selfie(id, name, picturePath);
        return selfie;
    }

    static class ViewHolder {
        ImageView pic;
        TextView name;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        mItems.clear();
        if(newCursor != null && newCursor.moveToFirst()) {
            do {
                mItems.add(getSelfieFromCursor(newCursor));
            } while (newCursor.moveToNext());
        }
        return super.swapCursor(newCursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.NAME_COL)));
        int h = (int)mContext.getResources().getDimension(R.dimen.selfie_pic_height);
        int w = (int)mContext.getResources().getDimension(R.dimen.selfie_pic_width);
        holder.pic.setImageBitmap(Helpers.getScaledBitmap(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PIC_PATH_COL)), w, h));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View newView = inflater.inflate(R.layout.selfie, parent, false);
        holder.name = (TextView)newView.findViewById(R.id.selfie_name_text_view);
        holder.pic = (ImageView)newView.findViewById(R.id.selfie_pic_image_view);
        newView.setTag(holder);
        return newView;
    }
}