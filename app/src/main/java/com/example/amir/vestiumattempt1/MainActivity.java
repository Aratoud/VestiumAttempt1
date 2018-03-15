package com.example.amir.vestiumattempt1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // this is the final tag for intent
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView;

    // this is the path of the taken image
    String mCurrentPhotoPath;
    // this is the uri of the taken image
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializes properties
       imageView = (ImageView) findViewById(R.id.imageView);
       mCurrentPhotoPath = "";

    }

    // calls dispatchTakePictureIntent() which is from android's website
    public void onClick(View view) {
        dispatchTakePictureIntent();

    }

    // this one is called automatically after activity results
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       imageView.setImageURI( photoURI);
    }

/*
    private String getPictureName() {
        SimpleDateFormat abc = new SimpleDateFormat("yyyyMMd_HHmmss");
        String timeStamp = abc.format(new Date());
        return "OurImage" + timeStamp + ".jpg";
    }
*/


    // this one creates a unique name to our newly taken image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // this one uses an intent to take a pic and store it in the private directory in
    // apps path using the unique name made by createImageFile method
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
