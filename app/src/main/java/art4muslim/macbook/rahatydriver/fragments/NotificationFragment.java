package art4muslim.macbook.rahatydriver.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
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
import art4muslim.macbook.rahatydriver.adapters.CustomNotifListAdapter;
import art4muslim.macbook.rahatydriver.application.BaseApplication;
import art4muslim.macbook.rahatydriver.fragments.orders.DoneOrderFragment;
import art4muslim.macbook.rahatydriver.models.Notification;
import art4muslim.macbook.rahatydriver.session.Constants;
import art4muslim.macbook.rahatydriver.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static art4muslim.macbook.rahatydriver.session.Constants.KEY_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,ViewTreeObserver.OnScrollChangedListener{


    View v;
    TextView _txt_msg;
    private static final String TAG = DoneOrderFragment.class.getSimpleName();
    private List<Notification> myOrdersList = new ArrayList<Notification>();
    private static String url = "";
    private ProgressDialog pDialog;
    private ListView listView;
    private CustomNotifListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionman;
    boolean isRightToLeft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        getActivity().setTitle(getString(R.string.item_Notif));
        listView = (ListView) v.findViewById(R.id.list);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
      /*  myOrdersList.add(new Notification("123","02/01/2018","22:00"));
        myOrdersList.add(new Notification("123","02/01/2018","22:00"));
        myOrdersList.add(new Notification("123","02/01/2018","22:00"));
        myOrdersList.add(new Notification("123","02/01/2018","22:00"));
        */
        adapter = new CustomNotifListAdapter(getActivity(), myOrdersList,"current",fragmentTransaction);
        listView.setAdapter(adapter);
        _txt_msg =(TextView)v.findViewById(R.id.txt_msg);




        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getNotifications();
            }
        } );
        return v;
    }
    @Override
    public void onStart() {
        Log.e(TAG,"onStart");
        super.onStart();
        myOrdersList.clear();
        adapter.clear();
        listView.getViewTreeObserver().addOnScrollChangedListener(this);

        // fetchnewOrders();
    }

    @Override
    public void onStop() {
        Log.e(TAG,"onStop");
        super.onStop();
        listView.getViewTreeObserver().removeOnScrollChangedListener(this);

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        Log.e(TAG,"onRefresh");
        myOrdersList.clear();
        adapter.clear();
        getNotifications();
    }
    @Override
    public void onScrollChanged() {
        if (listView.getFirstVisiblePosition() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }

    }

    private void getNotifications(){
        swipeRefreshLayout.setEnabled(true);
        String url = Constants.GET_NOTIFICATION_URL+KEY_API_TOKEN+"="+BaseApplication.session.getAccessToken();

        Log.e(TAG, "getNotifications url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getNotifications response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");

                    if (status.equals("1")){

                        JSONObject ordersObj = resJsonObj.getJSONObject("data");
                        JSONArray catsArray = ordersObj.getJSONArray("data");

                        if (catsArray.length()==0)
                            _txt_msg.setVisibility(View.VISIBLE);
                        for (int i = 0; i<catsArray.length();i++){

                            JSONObject adrJsonObj = catsArray.getJSONObject(i);
                            int id = adrJsonObj.getInt("id");
                            String order_date = adrJsonObj.getString("created_at");
                            String ar_title = adrJsonObj.getString("ar_title");
                            String en_title = adrJsonObj.getString("en_title");
                            String ar_body = adrJsonObj.getString("ar_body");
                            String en_body = adrJsonObj.getString("en_body");
                            String[] str = order_date.split(" ");
                            myOrdersList.add(new Notification(ar_title,en_title,ar_body,en_body,""+id,str[0],str[1]));
                        }

                        adapter.notifyDataSetChanged();

                    }
                    swipeRefreshLayout.setEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setEnabled(false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setEnabled(false);
                _txt_msg.setVisibility(View.VISIBLE);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

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

         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<>();
             params.put("api_token", ""+ BaseApplication.session.getAccessToken());
             params.put("locale", "ar");
             return params;
         }
  */

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
