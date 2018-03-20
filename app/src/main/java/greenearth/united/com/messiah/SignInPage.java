package greenearth.united.com.messiah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPage extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    String Entered_email = "";
    String Entered_password = "";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        email = (EditText) findViewById(R.id.email);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.goToSignUpPage).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

        int v = view.getId();


        if (v == R.id.signIn)
        {


            Entered_email = email.getText().toString();
            Entered_password = password.getText().toString();

            if (!Entered_email.equals("")) {
                if (!Entered_password.equals("")) {
                    mAuth.signInWithEmailAndPassword(Entered_email, Entered_password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Sign-In LOG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //Intent - move from sign in page to newsfeed page

                                        Intent i = new Intent(SignInPage.this, ActivitiesFeed.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Sign-In LOG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignInPage.this, "Authentication failed."+ task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                        try
                                        {
                                            task.getException();
                                        } catch (Exception e) {
                                            Log.e("General Exception", e.getMessage());
                                            Toast.makeText(SignInPage.this, "Error occurred. Please check your email and passowrd", Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    // ...
                                }
                            });
                } else
                    Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Please enter your email id", Toast.LENGTH_SHORT).show();

        }

        if(v == R.id.goToSignUpPage)
        {

            Intent i2 = new Intent(SignInPage.this, SignUpPage.class);
            startActivity(i2);
            finish();
        }
    }
}
