package greenearth.united.com.messiah;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetup extends AppCompatActivity {

    private CircleImageView setUpIamge;
    private Uri mainImageURI = null;

    String user_ID = "";

    private TextView Acc_Settings_Name;
    private Button Acc_Settings_Btn;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    private ProgressBar Acc_Settings_ProgressBar;

    private FirebaseFirestore firebaseFirestore;
    private boolean isChanged = false;

    private  TextView points_TV;
    private TextView profession_TV;
    private  TextView bloodGroup_TV;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);



        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Acc_Settings_Name = findViewById(R.id.username);
        Acc_Settings_Btn = findViewById(R.id.AccountSettingButton);

        Acc_Settings_ProgressBar = findViewById(R.id.Acc_Settings_ProgressBar);

        //Corrections part 6 27:00
        Acc_Settings_ProgressBar.setVisibility(View.VISIBLE);
        Acc_Settings_Btn.setEnabled(false);

        points_TV = findViewById(R.id.points_TV);

        profession_TV = findViewById(R.id.AccSettingsProfession_TV);

        bloodGroup_TV = findViewById(R.id.AccSettingsBlood_TV);


        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        setUpIamge = findViewById(R.id.setupImage);

        //Retrieving user name and Image from Firestore

        user_ID = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(user_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                       String name = task.getResult().getString("name");
                       String image = task.getResult().getString("image");

                       String points = task.getResult().getString("points");

                       points_TV.setText(points);

                       String profession = task.getResult().getString("profession");
                       profession_TV.setText(profession);

                       String bloodGroup = task.getResult().getString("bloodGroup");
                       bloodGroup_TV.setText(bloodGroup);

                       // Toast.makeText(AccountSetup.this, image, Toast.LENGTH_SHORT).show();

                        if(image != null) {
                            mainImageURI = Uri.parse(image);
                        }

                        RequestOptions placeholderrequest = new RequestOptions();
                        placeholderrequest.placeholder(R.drawable.defaultprofilepic);

                       Acc_Settings_Name.setText(name);

                        Glide.with(AccountSetup.this).setDefaultRequestOptions(placeholderrequest).load(image).into(setUpIamge);

                    }
                    else
                    {
                        Toast.makeText(AccountSetup.this, "Data doesn't Exists", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSetup.this, "Firestore Retrieval ERROR: "+error, Toast.LENGTH_SHORT).show();
                }

                Acc_Settings_ProgressBar.setVisibility(View.INVISIBLE);
                Acc_Settings_Btn.setEnabled(true);

            }
        });

        Acc_Settings_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_name = Acc_Settings_Name.getText().toString();

                if (!TextUtils.isEmpty(user_name) && mainImageURI != null)
                {
                    Acc_Settings_ProgressBar.setVisibility(View.VISIBLE);

                    if(isChanged)
                    {


                            //Upload Image in firebase

                            user_ID = firebaseAuth.getCurrentUser().getUid();



                            StorageReference image_path = storageReference.child("Profile_Images").child(user_ID + ".jpg");

                            image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        storeFirestore(task, user_name);

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(AccountSetup.this, "Image Error :" + error, Toast.LENGTH_SHORT).show();

                                        Acc_Settings_ProgressBar.setVisibility(View.INVISIBLE);
                                    }


                                }
                            });
                        }
                        else
                        {
                            storeFirestore(null, user_name);
                        }
                }



            }
        });

        setUpIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //If version is equal to and Marchmellow, it requires permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(AccountSetup.this, Manifest.permission.READ_EXTERNAL_STORAGE )
                            != PackageManager.PERMISSION_GRANTED )
                    {
                        Toast.makeText(AccountSetup.this, "permssion denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(AccountSetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
                        Toast.makeText(AccountSetup.this, "You already have the permssion", Toast.LENGTH_SHORT).show();
                        BringImagePicker();
                    }

                }
                else
                {
                    //for versions lower than Marchmellow

                    BringImagePicker();
                }

            }
        });
    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task, String user_name)
    {
        Uri download_uri;

        if(task != null)
        {
            download_uri = task.getResult().getDownloadUrl();
        }
        else
        {
            download_uri  = mainImageURI;
        }



        Map<String, String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("image",download_uri.toString());

        //FIRESTORE UPLOAD

        firebaseFirestore.collection("Users").document(user_ID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(AccountSetup.this, "The user settings are updated", Toast.LENGTH_SHORT).show();
                    Intent Activities_Feed_Intent = new Intent(AccountSetup.this, ActivitiesFeed.class);
                    startActivity(Activities_Feed_Intent);
                    finish();

                }
                else
                {
                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSetup.this, "Firestore ERROR: "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Toast.makeText(AccountSetup.this, "The image is uploaded", Toast.LENGTH_SHORT).show();
        Acc_Settings_ProgressBar.setVisibility(View.INVISIBLE);
    }

    private void BringImagePicker()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(AccountSetup.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                 mainImageURI = result.getUri();
                 setUpIamge.setImageURI(mainImageURI);

                 isChanged = true;
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
