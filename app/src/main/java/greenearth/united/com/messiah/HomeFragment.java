package greenearth.united.com.messiah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private DocumentSnapshot lastVisible;

    private String currentUserId = "";

    //private Boolean isFirstPageFirstLoad = true;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        volunteership_list = new ArrayList<>();
        volunteershp_list_view = view.findViewById(R.id.volunteership_list_view);


        volunteerRecyclerAdaptor = new VolunteerRecyclerAdaptor(volunteership_list);

        volunteershp_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        volunteershp_list_view.setAdapter(volunteerRecyclerAdaptor);

        firebaseFirestore = FirebaseFirestore.getInstance();

        volunteershp_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if(reachedBottom)
                {
                    String desc = lastVisible.getString("desc");
                    Toast.makeText(container.getContext(), "Reched "+desc, Toast.LENGTH_SHORT).show();

                    loadMorePost();
                }
            }
        });

       //here
        Query firstQuery = firebaseFirestore.collection("Posts")
               .orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
        try {
            currentUserId = mAuth.getCurrentUser().getUid();

//            Query firstQuery = firebaseFirestore.collection("Posts")
//                    .whereEqualTo("user_id", currentUserId)
//
//                    .orderBy("timestamp", Query.Direction.DESCENDING).limit(3);

            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (mAuth.getCurrentUser() != null) {
//                        if(isFirstPageFirstLoad)
//                        {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
//                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (mAuth.getCurrentUser() != null) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String postID = doc.getDocument().getId();
                                    VolunteerPost volunteerPost = doc.getDocument().toObject(VolunteerPost.class).withId(postID);

//                                    if(isFirstPageFirstLoad)
//                                    {
                                    volunteership_list.add(volunteerPost);
//                                    }
//                                    else
//                                    {
//                                        volunteership_list.add(0, volunteerPost);
//                                    }

                                    volunteerRecyclerAdaptor.notifyDataSetChanged();
                                }
                            }
                        }

                        //isFirstPageFirstLoad = false;
                    }
                }
            });


            // Inflate the layout for this fragment

        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Exception", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void loadMorePost()
    {

        //here
        Query nextQuery = firebaseFirestore.collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);
        try {
            currentUserId = mAuth.getCurrentUser().getUid();

//            Query nextQuery = firebaseFirestore.collection("Posts")
//                    .whereEqualTo("user_id", currentUserId)
//                    .orderBy("timestamp", Query.Direction.DESCENDING)
//                    .startAfter(lastVisible)
//                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(e != null)
                    {
                        return;
                    }
                    if (mAuth.getCurrentUser() != null) {

                        if (!documentSnapshots.isEmpty()) {
                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (mAuth.getCurrentUser() != null) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        String postID = doc.getDocument().getId();
                                        VolunteerPost volunteerPost = doc.getDocument().toObject(VolunteerPost.class).withId(postID);

                                        volunteership_list.add(volunteerPost);

                                        volunteerRecyclerAdaptor.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Exception", Toast.LENGTH_SHORT).show();
        }
    }



}
