package com.negassagisila.whereitssnap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Negassa Berhanu on 9/2/18.
 */

public class TagsFragment extends ListFragment {
    private ActivityComs mActivityComs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataManager d = new DataManager(getActivity().getApplicationContext());
        Cursor c = d.getTags();

        //create a new adapter
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, c,
                new String[] {DataManager.TABLE_ROW_TAG},
                new int[] {android.R.id.text1}, 0);

        //attach the Cursor to the adapter
        setListAdapter(cursorAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //what tag has just been clicked
        Cursor c = ((SimpleCursorAdapter)l.getAdapter()).getCursor();
        c.moveToPosition(position);
        String clickedTag = c.getString(1);
        //1 is the position of the string
        Log.i("ClickedTag = ", " " + clickedTag);

        mActivityComs.onTagsListItemSelected(clickedTag);
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
