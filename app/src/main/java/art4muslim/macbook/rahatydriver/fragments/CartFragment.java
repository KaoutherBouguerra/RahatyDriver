package art4muslim.macbook.rahatydriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.adapters.CustomListProducts;
import art4muslim.macbook.rahatydriver.models.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    View v;
    ListView _list_item;
    Button _btn_go;
    ArrayList<Product> products = new ArrayList<Product>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_cart, container, false);
        initFields();
        products.add(new Product(1,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        products.add(new Product(2,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        products.add(new Product(3,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
      //  products.add(new Product(4,"جرة غاز أزرق" , "6 ريال للمتر" , R.mipmap.line));
        CustomListProducts adapter = new CustomListProducts(getActivity(), products);

        _list_item.setAdapter(adapter);

        _btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to map

                MapCurrentFragment schedule = new MapCurrentFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
                fragmentTransaction.commit();

            }
        });

        return v;
    }

    private void initFields(){

        _list_item = (ListView)v.findViewById(R.id.list_item);
        _btn_go =(Button)v.findViewById(R.id.btn_go);
    }
}
