package greenearth.united.com.messiah;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class Post_Volunteership extends AppCompatActivity {


    private Toolbar newPostToolBar;

    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;
    private Uri postImageUri = null;
    private ProgressBar newpostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id = "";

    private Bitmap compressedImageFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post__v);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolBar = findViewById(R.id.new_post_tool_bar);
        setSupportActionBar(newPostToolBar);
        getSupportActionBar().setTitle("Post Volunteer Activity");

        //for back button enabled for Post_Volunteership
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostImage = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostBtn = findViewById(R.id.new_post_btn);
        newpostProgress = findViewById(R.id.new_post_progress);
        //to upload image from mobile
        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1,1)
                        .start(Post_Volunteership.this);

            }
        });

        //to post the volunteership - backend
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                    final String desc = newPostDesc.getText().toString();

                    if(!TextUtils.isEmpty(desc) && postImageUri != null)
                    {
                        newpostProgress.setVisibility(View.VISIBLE);

                        final String randomName = UUID.randomUUID().toString();
                        StorageReference filePath = storageReference.child("post_images").child(randomName+".jpg");

                        filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                            {
                                final String downloadUri = task.getResult().getDownloadUrl().toString();

                                if(task.isSuccessful())
                                {
                                    // Image Compression

                                    File newImageFile = new File(postImageUri.getPath());

                                    try {
                                        compressedImageFile   = new Compressor(Post_Volunteership.this)
                                                .setMaxHeight(200)
                                                .setMaxWidth(200)
                                                .setQuality(10)
                                                .compressToBitmap(newImageFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData =baos.toByteArray();


                                    UploadTask uploadTask = storageReference.child("post_images/thumbs").child(randomName+".jpg").putBytes(thumbData);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                    {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            String downloadthumbUrl = taskSnapshot.getDownloadUrl().toString();

                                            Map<String, Object> postMap = new HashMap<>();
                                            postMap.put("image_url", downloadUri);
                                            postMap.put("thumb",downloadthumbUrl);
                                            postMap.put("desc",desc);
                                            postMap.put("user_id", current_user_id);
                                            postMap.put("timestamp", FieldValue.serverTimestamp());

                                            firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(Post_Volunteership.this, "Post was added", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(Post_Volunteership.this, ActivitiesFeed.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(Post_Volunteership.this, "Firestore ERROR: "+error, Toast.LENGTH_SHORT).show();
                                                    }

                                                    newpostProgress.setVisibility(View.INVISIBLE);

                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {

                                        }
                                    });




                                }
                                else
                                {
                                        newpostProgress.setVisibility(View.INVISIBLE);
                                }

                            }
                        });

                    }
            }
        });

    }


    //after cropping the image, we get the image as result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                postImageUri = result.getUri();

                newPostImage.setImageURI(postImageUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }


}
