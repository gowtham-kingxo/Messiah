package greenearth.united.com.messiah;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

/**
 * Created by gowtham g on 21-03-2018.
 */

public class postID {

    @Exclude
    public String postID;

    public <T extends postID> T withId(@NonNull final String id)
    {
        this.postID = id;
        return (T) this;
    }




}
