package art4muslim.macbook.rahatydriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.adapters.CustomGridDetails;
import art4muslim.macbook.rahatydriver.models.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSectionFragment extends Fragment {


    View v;
    GridView grid;
    ArrayList<Product> products = new ArrayList<Product>();
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
    boolean isRightToLeft  ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_detail_section, container, false);
        initFields();
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        products.add(new Product(1,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        products.add(new Product(2,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        products.add(new Product(3,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        products.add(new Product(4,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        CustomGridDetails adapter = new CustomGridDetails(getActivity(), products,fragmentTransaction);

        grid.setAdapter(adapter);

        return v;
    }
    private void initFields(){
        grid=(GridView)v.findViewById(R.id.grid);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {


        if (!isRightToLeft ) {
            menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.backright));
        }else  menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.back));

        menu.findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                MapCurrentFragment schedule1 = new MapCurrentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }
}
