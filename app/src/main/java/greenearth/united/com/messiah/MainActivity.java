package greenearth.united.com.messiah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText username;
    EditText password;
    Button submit;
    Button fbLogin;
    FirebaseAuth mAuth;
    String Entered_Username = null;
    String Entered_Password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username  = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.fbLogin).setOnClickListener(this);






        mAuth = FirebaseAuth.getInstance();
    }




    @Override
    public void onClick(View view) {

        

    }
}
