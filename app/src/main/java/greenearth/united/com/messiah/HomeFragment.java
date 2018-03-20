package greenearth.united.com.messiah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView volunteershp_list_view;

    private List<VolunteerPost> volunteership_list;

    private FirebaseFirestore firebaseFirestore;

    private VolunteerRecyclerAdaptor volunteerRecyclerAdaptor;

    private FirebaseAuth mAuth;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        volunteership_list = new ArrayList<>();
        volunteershp_list_view = view.findViewById(R.id.volunteership_list_view);


        volunteerRecyclerAdaptor = new VolunteerRecyclerAdaptor(volunteership_list);

        volunteershp_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        volunteershp_list_view.setAdapter(volunteerRecyclerAdaptor);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING);


            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (mAuth.getCurrentUser() != null)
                    {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges())
                        {

                            if (mAuth.getCurrentUser() != null)
                            {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    VolunteerPost volunteerPost = doc.getDocument().toObject(VolunteerPost.class);

                                    volunteership_list.add(volunteerPost);

                                    volunteerRecyclerAdaptor.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            });



            // Inflate the layout for this fragment
            return view;

    }



}
