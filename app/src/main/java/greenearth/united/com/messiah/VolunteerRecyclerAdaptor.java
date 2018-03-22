package greenearth.united.com.messiah;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gowtham g on 19-03-2018.
 */

public class VolunteerRecyclerAdaptor extends RecyclerView.Adapter<VolunteerRecyclerAdaptor.ViewHolder> {

    private FirebaseFirestore firebaseFirestore;

    public  List<VolunteerPost> volunteership_list;
    public Context context;

    private FirebaseAuth mAuth;



    public VolunteerRecyclerAdaptor(List<VolunteerPost> volunteership_list)
    {
            this.volunteership_list = volunteership_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_list_item, parent, false);

        context = parent.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);


        String desc_data = volunteership_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = volunteership_list.get(position).getImage_url();
        String thumbUri = volunteership_list.get(position).getImage_thumb();
        holder.setVolunteershipImage(image_url, thumbUri);

        final String postId = volunteership_list.get(position).postID;

        final String currentUserId = mAuth.getCurrentUser().getUid();


        String user_id  = volunteership_list.get(position).getUser_id();
        holder.setUserImageandUsername(user_id);

        long millisecond = volunteership_list.get(position).getTimestamp().getTime();
        String dateString = android.text.format.DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();

        holder.setTime(dateString);

        //Get likes count
        firebaseFirestore.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if(! documentSnapshots.isEmpty())
                {
                    //it finds how many likes are present

                    int count = documentSnapshots.size();

                    holder.updateLikesCount(count);

                }
                else
                {
                    holder.updateLikesCount(0);
                }

            }
        });


        //Get likes
        firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            //below is added since getDrawable requires minSDK 21
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e)
            {
                if(documentSnapshot.exists())
                {
                    holder.postLikeImage.setImageDrawable(context.getDrawable(R.drawable.clapped));
                }
                else
                {
                    holder.postLikeImage.setImageDrawable(context.getDrawable(R.drawable.claps));
                }
            }
        });


        //Likes feature
        holder.postLikeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(!task.getResult().exists())
                        {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            // firebaseFirestore.collection("Posts").document(postId).collection("Likes") -> instead
                            firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).set(likesMap);
                        }
                        else
                        {
                            firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).delete();

                        }

                    }
                });






            }
        });


    }

    @Override
    public int getItemCount() {
        return volunteership_list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private TextView descView;

        private ImageView volunteership_Image_View;

        private TextView username;
        private CircleImageView userImage;

        private TextView postDate;

        private ImageView postLikeImage;
        private TextView postLikeCount;

        public ViewHolder(View itemView)
        {

            super(itemView);
            mView = itemView;

            postLikeImage = mView.findViewById(R.id.postLikeImage);
        }

        public void setDescText(String descText)
        {
            descView = mView.findViewById(R.id.postDescription);
            descView.setText(descText);
        }

        public void setVolunteershipImage(String downloadUri, String thumbUri)
        {
            volunteership_Image_View = mView.findViewById(R.id.postImage);

            RequestOptions placeholderrequest = new RequestOptions();
            placeholderrequest.placeholder(R.drawable.greyimage);

            //thumb nail of the image used here

            Glide.with(context).setDefaultRequestOptions(placeholderrequest).load(downloadUri)
                    .thumbnail(Glide.with(context).load(thumbUri))
                    .into(volunteership_Image_View);
        }

        public void setUserImageandUsername(String user_id)
        {
            username = mView.findViewById(R.id.postUserName);
            userImage = mView.findViewById(R.id.postUserImage_CircleView);

            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            String name = task.getResult().getString("name");
                            String image = task.getResult().getString("image");

                           // mainImageURI = Uri.parse(image);

                            RequestOptions placeholderrequest = new RequestOptions();
                            placeholderrequest.placeholder(R.drawable.greyimage);

                            username.setText(name);

                            Glide.with(context).applyDefaultRequestOptions(placeholderrequest).load(image).into(userImage);

                        }
                        else
                        {
                            Toast.makeText(context, "Data doesn't Exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, "Firestore Retrieval ERROR: "+error, Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }

        public void setTime(String date)
        {
            postDate = mView.findViewById(R.id.postDate);
            postDate.setText(date);

        }

        public void updateLikesCount(int count)
        {
            String likeString ="";
            if(count>0)
            {
                likeString = " likes";
            }
            else
            {
                likeString = " like";
            }


            postLikeCount = mView.findViewById(R.id.postLikeCount);
            postLikeCount.setText(""+ count + likeString);
        }
    }


}
