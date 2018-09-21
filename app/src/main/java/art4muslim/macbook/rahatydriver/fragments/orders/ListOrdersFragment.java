package art4muslim.macbook.rahatydriver.fragments.orders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import art4muslim.macbook.rahatydriver.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrdersFragment extends Fragment {


    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_list_orders, container, false);
        TabOrdersFragment schedule = new TabOrdersFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
        fragmentTransaction.commit();
        return v;
    }

}
