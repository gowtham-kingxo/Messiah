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

public  class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText username;
    EditText password;
    Button submit;
    Button fbLogin;
    FirebaseAuth mAuth;
    String Entered_Username = null;
    String Entered_Password = null;



    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

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

        Intent i = new Intent(MainActivity.this, HomeScreen.class);
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
                            Toast.makeText(MainActivity.this, "Authentication failed.",
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

            Entered_Username = username.getText().toString();
            Entered_Password = password.getText().toString();

            // Toast.makeText(this, "Email: "+Entered_Username+" Password: "+Entered_Password, Toast.LENGTH_SHORT).show();

            if (!Entered_Username.equals("")) {
                if (!Entered_Password.equals("")) {
                    mAuth.createUserWithEmailAndPassword(Entered_Username, Entered_Password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(MainActivity.this, "user created", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(MainActivity.this, HomeScreen.class);
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
                                                Toast.makeText(MainActivity.this, "Weak Password", Toast.LENGTH_SHORT).show();
                                            } catch (FirebaseAuthInvalidCredentialsException e) {

                                                Toast.makeText(MainActivity.this, "Invalid email id or password", Toast.LENGTH_SHORT).show();

                                            } catch (FirebaseAuthUserCollisionException e) {

                                                Toast.makeText(MainActivity.this, "email id already exists", Toast.LENGTH_SHORT).show();

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
