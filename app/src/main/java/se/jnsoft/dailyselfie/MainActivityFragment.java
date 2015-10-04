package se.jnsoft.dailyselfie;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {

    private SelfiesAdapter mAdapter;
    private Selfie mCurrentItem;

    public MainActivityFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SelfiesAdapter(getActivity(), null, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy () {
        mAdapter.freeResources();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCurrentItem = (Selfie)mAdapter.getItem(position);
        if(mCurrentItem != null ) {
            SelfieFragment selfieFragment = new SelfieFragment(mCurrentItem);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, selfieFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            // Force Android to execute the committed FragmentTransaction
            getFragmentManager().executePendingTransactions();
        }
    }

    public void addSelfie(Selfie s) {
        if(mAdapter != null) {
            mAdapter.add(s);
        }
    }

    public void deleteSelfie() {
        if(mAdapter != null) {
            mAdapter.delete(mCurrentItem);
        }
    }

    public void clearAll() {
        if(mAdapter != null) {
            mAdapter.clearAll();
        }
    }
}
