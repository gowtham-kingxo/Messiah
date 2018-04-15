package greenearth.united.com.messiah;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");


// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Do something with value!

                Toast.makeText(HomeScreen.this, ""+input.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });





        alert.show();


        // Creates an Intent that will load a map of San Francisco
//        Uri gmmIntentUri = Uri.parse("geo:13.0717105,80.2035242?q=13.0717105,80.2035242(volunteer activity)");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);




        //Delete a document
//        firebaseFirestore.collection("Testing").document("XTVTjbtQmSVhX2jsbwPn").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(HomeScreen.this, "Testing document deleted", Toast.LENGTH_SHORT).show();
//            }
//        });

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
