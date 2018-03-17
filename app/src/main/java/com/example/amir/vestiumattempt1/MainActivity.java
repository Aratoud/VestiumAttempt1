package com.example.amir.vestiumattempt1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // CONSTANTS
    // this is the final tag for intent
    static final int REQUEST_TAKE_PHOTO = 1;

    // VARIABLES
    ImageView imageView;
    String todaysDate;

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
        todaysDate = new SimpleDateFormat("yyyyMMdd").format( new Date());
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
        // variables
        String timeStamp;
        String imageFileName;
        String albumName;
        Date date;
        File storageDir;
        File image;


        // Create an image file name
        date = new Date();
        albumName = new SimpleDateFormat("yyyyMMdd").format( date);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( date);
        imageFileName = "JPEG_" + timeStamp + "_";

        // albumName is just a name which has todays date.. I want to make a subfoler and add files there
        // for example if today is 170318 is gonna be a folder for images taken today and 180318 for tomorrow

        // this code just adds them to /storage/emulated/0/Android/data/com.example.amir.vestiumattempt1/files/Pictures/
        // folder in the apps directory, which is available only to this app
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // thse two codes didn't work
        //storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + albumName);
        //System.out.println( storageDir.getAbsolutePath());

        // rest uses the storageDir directory and makes a unique File object called image
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // store the current path of the image file
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // this one uses an intent to take a pic and store it in the private directory in
    // apps path using the unique name made by createImageFile method
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // if External storage is available
            if ( isExternalStorageWritable()) {

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
            } else {
                Toast.makeText(getApplicationContext(), "External storage is not writeable",
                        Toast.LENGTH_SHORT).show();
                System.out.println("External storage is not writeable");
            }
        } else {
            Toast.makeText(getApplicationContext(), "The camera activity of your phone is not"
                    + " avaiable!", Toast.LENGTH_SHORT).show();
            System.out.println("The camera activity of your phone is not");
        }
    }

    /**
     * checks if external storage is available for the app to use
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        // Environment.MEDIA_MOUNTED is a String equal to "MEDIA_MOUNTED"
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
