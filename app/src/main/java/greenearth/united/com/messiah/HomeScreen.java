package greenearth.united.com.messiah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseFirestore firebaseFirestore;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        findViewById(R.id.logout).setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();


    }

    @Override
    public void onClick(View view) {

        firebaseFirestore.collection("Testing").document("XTVTjbtQmSVhX2jsbwPn").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomeScreen.this, "Testing document deleted", Toast.LENGTH_SHORT).show();
            }
        });

//        firebaseFirestore.collection("Users").whereEqualTo("name", "Sammmy").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if(task.isSuccessful())
//                {
//
//
//
//                    if(task.getResult().size() == 0) {
//                        Toast.makeText(HomeScreen.this, "No document retrieved", Toast.LENGTH_SHORT).show();
//                    }
//
//                    Toast.makeText(HomeScreen.this, "task successful", Toast.LENGTH_SHORT).show();
//                    for (DocumentSnapshot document : task.getResult()) {
//
//                       // Toast.makeText(HomeScreen.this, ""+document.getData(), Toast.LENGTH_SHORT).show();
//                        Log.d("getData",document.getId());
//
//                       // Toast.makeText(HomeScreen.this, ""+d, Toast.LENGTH_SHORT).show();
//
//                        long points = document.getLong("Points");
//
//                        points = points+20;
//
//                        firebaseFirestore.collection("Users").document(document.getId()).update("Points", points).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                                Toast.makeText(HomeScreen.this, "Points has been updated", Toast.LENGTH_SHORT).show();
//
//                            }
//                        })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(HomeScreen.this, "Points - NOT UPDATED", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//
//
//
//
//
//
//
//                    }
//
//                }
//                else
//                {
//                    Toast.makeText(HomeScreen.this, "User named Sammy not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });


        //firebaseFirestore.collection("")

}
}
