package art4muslim.macbook.rahatydriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.application.BaseApplication;
import art4muslim.macbook.rahatydriver.session.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    boolean isRightToLeft  ;
    View v;
    private static final String TAG = MapCurrentFragment.class.getSimpleName();

    ProgressBar _progressBar;
    TableLayout _tableLayout1;
    TextView _txt_current_balance, _txt_order_balance, _txt_orders_number, _txt_amount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_account, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        initFields();

        getBalance();

        return  v;
    }

    private void initFields(){
        _txt_current_balance = (TextView)v.findViewById(R.id.txt_current_balance);
        _txt_order_balance = (TextView)v.findViewById(R.id.txt_order_balance);
        _txt_orders_number = (TextView)v.findViewById(R.id.txt_orders_number);
        _txt_amount = (TextView)v.findViewById(R.id.txt_amount);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _tableLayout1=(TableLayout) v.findViewById(R.id.tableLayout1);



    }

    private void getBalance(){
         String url = Constants.GET_BALANCE_URL;
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();

        _tableLayout1.setVisibility(View.GONE);
        _progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "getBalance url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getBalance response === "+response.toString());
                _tableLayout1.setVisibility(View.VISIBLE);
                _progressBar.setVisibility(View.GONE);
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject ordersObj = resJsonObj.getJSONObject("data");

                        String orders_number = ordersObj.getString("orders_number");
                        String order_balance = ordersObj.getString("order_balance");
                        String current_balance = ordersObj.getString("current_balance");
                        String amount = ordersObj.getString("amount");
                        _txt_amount.setText(amount+" "+getString(R.string.ryal));
                        _txt_current_balance.setText(current_balance+" "+getString(R.string.ryal));
                        _txt_order_balance.setText(order_balance+" "+getString(R.string.ryal));
                        _txt_orders_number.setText(orders_number+" "+getString(R.string.orders));

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);
                _progressBar.setVisibility(View.GONE);
                _tableLayout1.setVisibility(View.VISIBLE);
                if (error instanceof AuthFailureError) {
                    // AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    //   AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }

            }
        }) {

         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String accessId = BaseApplication.session.getAccessToken();
                if(Constants.androiStudioMode.equals("debug")){
                    Log.v("accessid", accessId);}
                headers.put("X-Auth-Token", "" + accessId);
                return headers;
            }
            */

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());

                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(hisRequest);
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
