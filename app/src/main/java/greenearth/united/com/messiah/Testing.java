package greenearth.united.com.messiah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class Testing extends AppCompatActivity {


    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        //mDatabase = FirebaseDatabase.getInstance().getReference();

       // mDatabase.child("Name").setValue("hii");
       // mDatabase.child("Value").setValue("hello");
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
//
//        mDatabase.child("Name").setValue("User2");
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User/User2");
//
//        mDatabase.child("Name").setValue("Name");
//        mDatabase.child("Value").setValue("Mike");





    }
}
