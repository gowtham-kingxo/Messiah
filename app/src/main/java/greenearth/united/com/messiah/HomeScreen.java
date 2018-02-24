package greenearth.united.com.messiah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {


    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        findViewById(R.id.logout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(HomeScreen.this, SignInPage.class);
        startActivity(i);
        finish();
    }
}
