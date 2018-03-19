package greenearth.united.com.messiah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class HomeFragment extends Fragment {

    private RecyclerView volunteershp_list_view;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        volunteershp_list_view = getActivity().findViewById(R.id.volunteership_list_view);

        // Inflate the layout for this fragment
        return view;
    }


}
