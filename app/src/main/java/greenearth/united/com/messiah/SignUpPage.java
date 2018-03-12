package greenearth.united.com.messiah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

public  class SignUpPage extends AppCompatActivity implements View.OnClickListener {


    EditText email, username, profession, age;
    EditText password, confirm_password;
    Button submit;
    Button fbLogin;
    FirebaseAuth mAuth;
    String Entered_email = null;
    String Entered_Password = null;
    String Entered_confirm_Password = null;
    String Entered_profession = null;
    String Entered_age = null;
    String Entered_username = null;

    DatabaseReference mDatabase;





    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //getting mDatabase to point to the root of the firebase database

        mDatabase = FirebaseDatabase.getInstance().getReference();

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        profession = (EditText) findViewById(R.id.profession);
        age = (EditText) findViewById(R.id.age);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);

        findViewById(R.id.submit).setOnClickListener(this);


        // findViewById(R.id.fbLogin).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("fb:", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("fb:", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {

            }

        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Toast.makeText(this, "User: "+currentUser, Toast.LENGTH_SHORT).show();

        if(currentUser != null)
           updateUI();
    }

    private void updateUI() {

        Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(SignUpPage.this, HomeScreen.class);
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

        Entered_confirm_Password = confirm_password.getText().toString();

        if(Entered_Password != Entered_confirm_Password)
            Toast.makeText(this, "passwords does not match", Toast.LENGTH_SHORT).show();

        Entered_profession = profession.getText().toString();
        Entered_age = age.getText().toString();
        Entered_username = username.getText().toString();


            // Toast.makeText(this, "Email: "+Entered_email+" Password: "+Entered_Password, Toast.LENGTH_SHORT).show();

            if (!Entered_email.equals("")) {
                if (!Entered_Password.equals("")) {
                    mAuth.createUserWithEmailAndPassword(Entered_email, Entered_Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpPage.this, "user created", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(SignUpPage.this, HomeScreen.class);
                                        startActivity(i);
                                        finish();

                                    }
                                    else
                                        {
                                        if (!task.isSuccessful())
                                        {
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
                } else
                    Toast.makeText(this, "Please enter the password!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Please enter the email address!", Toast.LENGTH_SHORT).show();


        }

}
