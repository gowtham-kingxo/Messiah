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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetup extends AppCompatActivity {

    private CircleImageView setUpIamge;
    private Uri mainImageURI = null;

    private TextView Acc_Settings_Name;
    private Button Acc_Settings_Btn;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    private ProgressBar Acc_Settings_ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Acc_Settings_Name = findViewById(R.id.Acc_Settings_Name);
        Acc_Settings_Btn = findViewById(R.id.AccountSettingButton);

        Acc_Settings_ProgressBar = findViewById(R.id.Acc_Settings_ProgressBar);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        setUpIamge = findViewById(R.id.setupImage);

        Acc_Settings_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_name = Acc_Settings_Name.getText().toString();

                if(!TextUtils.isEmpty(user_name) && mainImageURI != null)
                {
                    //Upload Image in firebase

                    String user_ID = firebaseAuth.getCurrentUser().getUid();

                    Acc_Settings_ProgressBar.setVisibility(View.VISIBLE);

                    StorageReference image_path = storageReference.child("Profile_Images").child(user_ID+".jpg");

                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {

                            if(task.isSuccessful())
                            {
                                Uri download_uri = task.getResult().getDownloadUrl();
                                Toast.makeText(AccountSetup.this, "The image is uploaded", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String error = task.getException().getMessage();
                                Toast.makeText(AccountSetup.this, "Error :"+ error, Toast.LENGTH_SHORT).show();
                            }


                        Acc_Settings_ProgressBar.setVisibility(View.INVISIBLE);

                        }
                    });
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
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
