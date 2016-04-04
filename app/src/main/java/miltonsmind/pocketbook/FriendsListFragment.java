package miltonsmind.pocketbook;

/**
 * Created by brendan on 22/02/2016.
 */


import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

// Fragments NOTES
// Multiple fragments can relate to activities
// Activate and de-activate at any time
// No navigation take place all happen on one screen

// Fragment class used to display friends on screen in a format to use details and have access to edit and delete
public class FriendsListFragment extends android.support.v4.app.ListFragment implements LoaderManager.LoaderCallbacks<List<Friend>> {

    private static final String LOG_TAG = FriendsListFragment.class.getSimpleName();
    private FriendsCustomAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Friend> mFriends;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // Update fragments in class
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mContentResolver = getActivity().getContentResolver();
        mAdapter = new FriendsCustomAdapter(getActivity(), getChildFragmentManager());
        setEmptyText("No Contacts");
        setListAdapter(mAdapter);
        setListShown(false); // Wait till data is returned from loader

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Friend>> onCreateLoader(int id, Bundle args) {

       mContentResolver = getActivity().getContentResolver();
        return new FriendsListLoader(getActivity(), FriendsContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Friend>> loader, List<Friend> friends) {

        mAdapter.setData(friends);
        mFriends = friends;

        if(isResumed()){

            setListShown(true);

        }else {

            setListShownNoAnimation(true);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<Friend>> loader) {

        mAdapter.setData(null);

    }
}
