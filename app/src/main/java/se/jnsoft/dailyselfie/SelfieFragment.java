package se.jnsoft.dailyselfie;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by johann on 2015-10-04.
 */
public class SelfieFragment extends Fragment {
    private Selfie mItem;

    public SelfieFragment() {}
    public SelfieFragment(Selfie s) { mItem = s; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Display options menu
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.selfie_pic, container, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.selfie_pic_image_view);
        imageView.setImageBitmap(Helpers.getScaledBitmap(mItem.getPath(), 0, 0));

        // Set title bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mItem.toString());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Remove items of the SelfiesFragment
        menu.clear();

        // Inflate selfie details mnu
        inflater.inflate(R.menu.menu_selfie, menu);
    }


}
