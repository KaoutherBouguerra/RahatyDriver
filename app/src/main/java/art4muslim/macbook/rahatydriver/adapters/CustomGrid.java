package art4muslim.macbook.rahatydriver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.models.Category;

import java.util.ArrayList;

/**
 * Created by macbook on 29/12/2017.
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;

    ArrayList<Category> categories;

    public CustomGrid(Context c, ArrayList<Category> categories ) {
        mContext = c;
        this.categories = categories;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categories.size();
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
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(categories.get(position).getName());
            imageView.setImageResource(categories.get(position).getImage());
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
