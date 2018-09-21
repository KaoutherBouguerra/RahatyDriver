package art4muslim.macbook.rahatydriver.adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.fragments.CartFragment;
import art4muslim.macbook.rahatydriver.models.Product;

import java.util.ArrayList;

/**
 * Created by macbook on 29/12/2017.
 */

public class CustomGridDetails extends BaseAdapter {
    private Context mContext;

    int i;
    ArrayList<Product> products;
    FragmentTransaction fragmentTransaction;
    public CustomGridDetails(Context c, ArrayList<Product> products,FragmentTransaction fragmentTransaction  ) {
        mContext = c;
        this.products = products;
        this.fragmentTransaction = fragmentTransaction;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.detail_grid_single, null);
            TextView _txt_title = (TextView) grid.findViewById(R.id.txt_title);
            TextView _txt_price = (TextView) grid.findViewById(R.id.txt_price);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            ImageView _img_moin = (ImageView)grid.findViewById(R.id.img_moin);
            ImageView _img_plus = (ImageView)grid.findViewById(R.id.img_plus);
            Button _btn_add = (Button)grid.findViewById(R.id.btn_add);
            final EditText _edt_num = (EditText)grid.findViewById(R.id.edt_num);
            i = Integer.parseInt(_edt_num.getText().toString());
            _txt_title.setText(products.get(position).getName());
            _txt_price.setText(products.get(position).getPrice()+" "+mContext.getString(R.string.ryal));
            imageView.setImageResource(products.get(position).getImage());

            if (i==0)
                _img_moin.setClickable(false);
            else _img_moin.setClickable(true);

            _img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i++;
                    _edt_num.setText(""+i);
                }
            });

            _img_moin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i--;
                    _edt_num.setText(""+i);
                }
            });

            _btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("dmsd;sd","dddddd");
                    CartFragment schedule = new CartFragment();
                    fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
                    fragmentTransaction.commit();
                }
            });
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
