package com.negassagisila.whereitssnap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Negassa Berhanu on 9/2/18.
 */

public class CaptureFragment extends Fragment implements LocationListener{
    private final static int CAMERA_REQUEST = 123;

    @NonNull
    private ImageView mImageView;

    //the file path for the photo
    String mCurrentPhotoPath;

    //where the captured image is stored
    private Uri mImageUri = Uri.EMPTY;

    //a reference to our database
    private DataManager mDataManager;

    //for the location
    private Location mLocation = new Location("");
    private LocationManager mLocationManager;
    private String mProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataManager = new DataManager(getActivity().getApplicationContext());

        //initialize mLocationManager
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_capture, container, false);

        mImageView = view.findViewById(R.id.imageView);
        Button btnCapture = view.findViewById(R.id.btnCapture);
        Button btnSave = view.findViewById(R.id.btnSave);

        final EditText mEditTextTitle = view.findViewById(R.id.editTextTitle);
        final EditText mEditTextTag1 = view.findViewById(R.id.editTextTag1);
        final EditText mEditTextTag2 = view.findViewById(R.id.editTextTag2);
        final EditText mEditTextTag3 = view.findViewById(R.id.editTextTag3);

        //listen for clicks on the capture button
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                } catch (IOException iex) {
                    //error occurred while creating the File
                    Log.e("error", "error creating file");
                }
                //continue only if the File was successfully created
                if (photoFile != null) {
                    mImageUri = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        //listen for clicks on the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageUri != null) {
                    if (!mImageUri.equals(Uri.EMPTY)) {
                        //we have a photo to save
                        Photo photo = new Photo();
                        photo.setTitle(mEditTextTitle.getText().toString());
                        photo.setStorageLocation(mImageUri);

                        //set the current GPS location
                        photo.setGpsLocation(mLocation);

                        //what is in the tags
                        String tag1 = mEditTextTag1.getText().toString();
                        String tag2 = mEditTextTag2.getText().toString();
                        String tag3 = mEditTextTag3.getText().toString();

                        //assign the strings to the Photo object
                        photo.setTag1(tag1);
                        photo.setTag2(tag2);
                        photo.setTag3(tag3);

                        //set the new object to our DataManager
                        mDataManager.addPhoto(photo);
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();

                        mEditTextTitle.getText().clear();
                        mEditTextTag1.getText().clear();
                        mEditTextTag2.getText().clear();
                        mEditTextTag3.getText().clear();
                        mImageView.setImageURI(Uri.parse(Uri.EMPTY.toString()));
                    } else {
                        //no image
                        Toast.makeText(getActivity(), "No image to save", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //uri not initialized
                    Log.e("Error ", "URI is null");
                }
            }
        });

        return view;
    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "__";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, //filename
                ".jpg", //extension
                storageDir //folder
                );

        //save for use with ACTION_VIEW intent
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                mImageView.setImageURI(Uri.parse(mImageUri.toString()));
            } catch (Exception e) {
                Log.e("Error", "Uri not set");
            }
        } else {
            mImageUri = Uri.EMPTY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //make sure we don't run out of memory
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }

    @Override
    public void onLocationChanged(Location location) {
        //update the location if it changed
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //start updates when the app starts/resumes
    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        mLocationManager.requestLocationUpdates(mProvider, 500, 1, this);
    }

    //pause the location manager when the app is paused/stopped
    @Override
    public void onPause() {
        super.onPause();

        mLocationManager.removeUpdates(this);
    }
}
