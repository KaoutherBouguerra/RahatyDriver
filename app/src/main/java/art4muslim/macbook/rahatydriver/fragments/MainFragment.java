package art4muslim.macbook.rahatydriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.adapters.CustomGrid;
import art4muslim.macbook.rahatydriver.models.Category;

import java.util.ArrayList;


public class MainFragment extends Fragment {
    GridView grid;
    String[] web = {
            "Google",
            "Github",
            "Instagram",
            "Facebook",
            "Flickr",
            "Pinterest",
            "Quora",
            "Twitter"

    } ;
    int[] imageId = {
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home,
            R.mipmap.home
    };
    View v;
    ArrayList<Category> cats = new ArrayList<Category>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_main, container, false);
        initFields();
        cats.add(new Category(1,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(2,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(3,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(4,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(5,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(6,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(7,"غاز" ,  R.mipmap.cat_gaz));
        cats.add(new Category(8,"غاز" ,  R.mipmap.cat_gaz));

        CustomGrid adapter = new CustomGrid(getActivity(), cats);

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                DetailSectionFragment schedule = new DetailSectionFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
                fragmentTransaction.commit();
            }
        });
        return v;
    }

    private void initFields(){
        grid=(GridView)v.findViewById(R.id.grid);
    }
}
