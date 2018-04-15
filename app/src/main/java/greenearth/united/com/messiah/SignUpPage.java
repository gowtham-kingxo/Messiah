package greenearth.united.com.messiah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public  class SignUpPage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{


    EditText email, username;
    Spinner profession, bloodGroup;
    EditText password, confirm_password;
    Button submit;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String Entered_email = null;
    String Entered_Password = null;
    String Entered_confirm_Password = null;
    String Entered_profession = "Other";
    String Entered_blood_group = "Blood Group not selected";
    String Entered_username = null;

    FirebaseUser currentUser;

    DatabaseReference mDatabase;





    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //getting mDatabase to point to the root of the firebase database

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        profession = (Spinner) findViewById(R.id.ProfessionSpinner);
        bloodGroup = (Spinner) findViewById(R.id.BloodGroupSpinner);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
        password = findViewById(R.id.password);

        profession = findViewById(R.id.ProfessionSpinner);
        bloodGroup = findViewById(R.id.BloodGroupSpinner);

        ArrayAdapter<CharSequence> profession_adapter = ArrayAdapter.createFromResource(this, R.array.professions, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> bloodgroup_adapter = ArrayAdapter.createFromResource(this, R.array.BloodGroup, R.layout.support_simple_spinner_dropdown_item);

        profession_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bloodgroup_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        profession.setAdapter(profession_adapter);
        bloodGroup.setAdapter(bloodgroup_adapter);


        profession.setOnItemSelectedListener(this);
        bloodGroup.setOnItemSelectedListener(this);

        findViewById(R.id.sign_up_submit).setOnClickListener(this);


        // findViewById(R.id.fbLogin).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();





    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        Toast.makeText(this, "User: "+currentUser, Toast.LENGTH_SHORT).show();

        if(currentUser != null)
           updateUI();
    }

    private void updateUI() {

        Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(SignUpPage.this, ActivitiesFeed.class);
        startActivity(i);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fb:", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fb:", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fb:", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {


             //Toast.makeText(this, "Hii submit clicked", Toast.LENGTH_SHORT).show();

            Entered_email = email.getText().toString();
            Entered_Password = password.getText().toString();

            //Entered_Password = "hihello66";

                Entered_confirm_Password = confirm_password.getText().toString();
//
//        if(Entered_Password != Entered_confirm_Password)
//            Toast.makeText(this, "passwords does not match", Toast.LENGTH_SHORT).show();
//
//        Entered_profession = profession.getText().toString();
//        Entered_age = age.getText().toString();
               Entered_username = username.getText().toString();


            // Toast.makeText(this, "Email: "+Entered_email+" Password: "+Entered_Password, Toast.LENGTH_SHORT).show();

            if (!Entered_email.equals(""))
            {
                if (!Entered_Password.equals("")) {

                    if(Entered_Password != Entered_confirm_Password) {

                        if(Entered_username != null)
                        {

                        mAuth.createUserWithEmailAndPassword(Entered_email, Entered_Password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpPage.this, "user created", Toast.LENGTH_SHORT).show();

                                            //User details stored in Firestore

                                            currentUser = mAuth.getCurrentUser();

                                            String user_ID = currentUser.getUid();
                                            Map<String, String> userMap = new HashMap<>();

                                    long p = 0;
                                        userMap.put("name",Entered_username);
                                        userMap.put("profession",Entered_profession);
                                        userMap.put("bloodGroup",Entered_blood_group);


                                        userMap.put("points", ""+p);


                                            firebaseFirestore.collection("Users").document(user_ID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpPage.this, "The user account successfuly created", Toast.LENGTH_SHORT).show();
                                                        Intent Activities_Feed_Intent = new Intent(SignUpPage.this, ActivitiesFeed.class);
                                                        startActivity(Activities_Feed_Intent);
                                                        finish();

                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(SignUpPage.this, "Firestore ERROR: " + error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                            Intent i = new Intent(SignUpPage.this, AccountSetup.class);
                                            startActivity(i);
                                            finish();

                                        } else {
                                            if (!task.isSuccessful()) {
                                                try {
                                                    throw task.getException();
                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                    Toast.makeText(SignUpPage.this, "Weak Password", Toast.LENGTH_SHORT).show();
                                                } catch (FirebaseAuthInvalidCredentialsException e) {

                                                    Toast.makeText(SignUpPage.this, "Invalid email id", Toast.LENGTH_SHORT).show();

                                                } catch (FirebaseAuthUserCollisionException e) {

                                                    Toast.makeText(SignUpPage.this, "email id already exists", Toast.LENGTH_SHORT).show();

                                                } catch (Exception e) {
                                                    Log.e("General Exception", e.getMessage());
                                                }
                                            }
                                            Log.d("sign up error", "createUserWithEmail:failure", task.getException());
                                            //Toast.makeText(Sign_Up_Activity.this, "Error: unable to create user", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                    }
                    else
                        {
                            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                        Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(this, "Please enter the password!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Please enter the email address!", Toast.LENGTH_SHORT).show();


        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner whichOne = (Spinner) adapterView;

        if(whichOne.getId() == R.id.ProfessionSpinner)
        {

            Entered_profession = (String) adapterView.getItemAtPosition(i);

        }
        else if (whichOne.getId() == R.id.BloodGroupSpinner)
        {
            Entered_blood_group = (String) adapterView.getItemAtPosition(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        Spinner whichOne = (Spinner) adapterView;

        if(whichOne.getId() == R.id.ProfessionSpinner)
        {

            Entered_profession = "Other";

        }
        else if (whichOne.getId() == R.id.BloodGroupSpinner)
        {
            Entered_blood_group = "No blood group selected";
        }
    }
}
