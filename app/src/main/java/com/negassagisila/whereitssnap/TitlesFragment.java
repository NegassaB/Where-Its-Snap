package com.negassagisila.whereitssnap;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Negassa Berhanu on 9/2/18.
 */

public class TitlesFragment extends ListFragment {
    private Cursor mCursor;
    private ActivityComs mActivityComs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the tag to search for
        String tag = getArguments().getString("Tag");

        //get an instance of DataManager
        DataManager d = new DataManager(getActivity().getApplicationContext());

        if (tag == "_NO_TAG") {
            //get all the titles from the database
            mCursor = d.getTitles();
        } else {
            //get all the titles with a specific related tag
            mCursor = d.getTitlesWithTag(tag);
        }
        //create a new adapter
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mCursor, new String[] {DataManager.TABLE_ROW_TITLE},
                new int[] {android.R.id.text1}, 0);

        //attach the adapter to the ListView
        setListAdapter(cursorAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //move the cursor to the clicked item in the list
        mCursor.moveToPosition(position);

        //what is the database _id of this item?
        int dBID = mCursor.getInt(mCursor.getColumnIndex(DataManager.TABLE_ROW_ID));

        //use the interface to send the selected _id
        mActivityComs.onTitlesListItemSelected(dBID);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityComs = (ActivityComs) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityComs = null;
    }
}