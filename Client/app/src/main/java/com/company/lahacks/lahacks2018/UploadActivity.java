package com.company.lahacks.lahacks2018;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private Uri imageUri = null;
    private ImageView imageView;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = findViewById(R.id.iv_userimage);
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void chooseImage(View view) {
        openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public void uploadImage(View view) {
        if (imageUri == null) {
            Toast.makeText(UploadActivity.this, "Cannot upload without selecting image.",
                    Toast.LENGTH_SHORT).show();
            return; //no image so return immediately
        }

        Toast.makeText(UploadActivity.this, "Please wait for upload...",
                Toast.LENGTH_SHORT).show();

        //stores as 'photos/<filename>.jpg'
        final StorageReference photoRef = mStorageRef.child("photos")
                .child(imageUri.getLastPathSegment());

        photoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Upload succeeded

                // Get the public download URL
                //Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                Toast.makeText(UploadActivity.this, "Photo successfully uploaded.",
                        Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Photo upload failed. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        })
        ;

        //after uploading the image set imageURI back to null and clear imageview
        imageUri = null;
        imageView.setImageResource(android.R.color.transparent);
    }


}
