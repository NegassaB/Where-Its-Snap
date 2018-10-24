package com.negassagisila.whereitssnap;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Negassa Berhanu on 9/16/18.
 */

public class ViewFragment extends Fragment {
    private Cursor mCursor;
    private ImageView mImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //where is the Photo object we want to show?
        int position = getArguments().getInt("Position");

        //Load the appropriate photo from database
        DataManager d = new DataManager(getActivity().getApplicationContext());
        mCursor = d.getPhoto(position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        TextView textView = view.findViewById(R.id.textView);
        Button buttonShowLocation = view.findViewById(R.id.buttonShowLocation);

        //set the text from the title column of the data
        textView.setText(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_TITLE)));

        mImageView = view.findViewById(R.id.imageView);

        //load the image into the TextView via the URI
        mImageView.setImageURI(Uri.parse(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_URI))));

        buttonShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = Double.valueOf(mCursor.getString(
                        mCursor.getColumnIndex(DataManager.TABLE_ROW_LOCATION_LAT)
                ));
                double longitude = Double.valueOf(mCursor.getString(
                        mCursor.getColumnIndex(DataManager.TABLE_ROW_LOCATION_LONG)
                ));

                //create a URI from the latitude and longitude
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);

                //create a Google maps intent
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                //start the maps activity
                getActivity().startActivity(mapsIntent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //make sure we don't run out of memory
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }
}
