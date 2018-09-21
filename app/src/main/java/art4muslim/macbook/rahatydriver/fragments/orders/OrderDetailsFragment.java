package art4muslim.macbook.rahatydriver.fragments.orders;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import art4muslim.macbook.rahatydriver.R;
import art4muslim.macbook.rahatydriver.application.BaseApplication;
import art4muslim.macbook.rahatydriver.fragments.MapCurrentFragment;
import art4muslim.macbook.rahatydriver.session.Constants;
import art4muslim.macbook.rahatydriver.utils.circularimageview.AlertDialogManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static art4muslim.macbook.rahatydriver.session.Constants.KEY_API_TOKEN;
import static art4muslim.macbook.rahatydriver.session.Constants.baseUrlImages;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    LinearLayout _linearProducts;
    String id, status;
    View v;
    RelativeLayout _relative1;
    GoogleMap m_map;
    Button _btn_call ,_btn_done,_btn_sum, _btn_cancel, _btn_deliver, _btn_refuse;
    RelativeLayout _rel;
    TextView _txtPhone, _txt_customer_name, _txt_price_delevring, _txt_price_total, _grid_text;
    TextView _txt_price, _txt_product, _txt_down_count;
    ImageView _grid_image;
    String phoneDriver;
    final int REQUEST_PERMISSION_CALL = 1001;
    RelativeLayout _relativeLayout, _relativeNameCustomer;
    RelativeLayout _relativeLayoutGlob, _relative_notif;
    LinearLayout _linearLayout0;
    LinearLayout _linearLayout;
    ProgressBar _progressBar;
    boolean isRightToLeft ;
    boolean isDone = false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final String TAG = OrderDetailsFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_order_details, container, false);
        id = getArguments().getString("ID");
        status = getArguments().getString("STATUS");
        getActivity().setTitle(getString(R.string.txt_order_detail)+" "+id);
        initFields();
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        fetchDetailsOrders();

        _btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("pending")){
                    showDialog();
                }else{
                    String url = Constants.DELEVER_ORDER_URL;
                    applyForOrder("",url,getString(R.string.deliver_success));
                }
            }
        });

        _rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(v.findViewById(R.id.relative_notif));

                notifyClient();

            }
        });
        _btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (!status.equals("applied")){
                    url = Constants.REFUSE_ORDER_URL;
                    //isRefused = true;
                } else {

                    url = Constants.CANCEL_ORDER_URL;
                }

                Log.e(TAG,"_btn_cancel url = "+url);
                applyForOrder("",url,getString(R.string.cancel_sucess));
            }
        });


        _btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);

                    return;
                } else {

                    if (!phoneDriver.isEmpty()) {
                        Intent intent = new Intent();
                        Uri uri = Uri.parse("tel:" + phoneDriver.trim());
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        });
     /*   _btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.CANCEL_ORDER_URL;
                applyForOrder("",url);
            }
        });
        _btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.DELEVER_ORDER_URL;
                applyForOrder("",url);
            }
        });

        */

        if (status.equals("delivered")){
            _btn_deliver.setVisibility(View.GONE);
            _btn_refuse.setVisibility(View.GONE);
            _btn_cancel.setVisibility(View.GONE);
            _btn_done.setVisibility(View.GONE);
            _btn_deliver.setEnabled(false);
            _btn_refuse.setEnabled(false);
            _btn_cancel.setEnabled(false);
            _btn_done.setEnabled(false);
            _relativeLayout.setVisibility(View.GONE);
        }else if(status.equals("applied")){
            _relativeLayout.setVisibility(View.GONE);
            _btn_done.setText(getString(R.string.txt_okrequest));
            _btn_cancel.setText(getString(R.string.cancelOrder));
            _btn_call.setVisibility(View.VISIBLE);
            _rel.setVisibility(View.VISIBLE);
            _txtPhone.setVisibility(View.VISIBLE);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return  v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (status.equals("pending")){

        downCount();
    }
    }

    private void downCount(){
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {

                if (getActivity() != null)
                    if (!getActivity().isFinishing())
                        _txt_down_count.setText(String.format(getResources().getString(R.string.txtexcute), "" + millisUntilFinished / 1000));
            }

            public void onFinish() {

                if (getActivity() != null)
                    if (!getActivity().isFinishing())
                        _txt_down_count.setText(getResources().getString(R.string.finish_timing));

                _btn_done.setEnabled(false);
                // if(!active)
                //   getActivity().finish();
            }
        }.start();
    }
    private void initFields(){
        _rel = (RelativeLayout)v.findViewById(R.id.rel);
        _relativeLayout = (RelativeLayout)v.findViewById(R.id.relativeLayout);
        _relativeNameCustomer = (RelativeLayout)v.findViewById(R.id.relative2);
        _relativeLayoutGlob = (RelativeLayout)v.findViewById(R.id.relativeLayoutGlob);
        _relative_notif = (RelativeLayout)v.findViewById(R.id.relative_notif);
        _relative1 = (RelativeLayout)v.findViewById(R.id.relative1);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        _linearLayout0 = (LinearLayout) v.findViewById(R.id.linearLayout0);
        _linearProducts = (LinearLayout) v.findViewById(R.id.linearProducts);
        _btn_call = (Button)v.findViewById(R.id.btn_call);
        _btn_done = (Button)v.findViewById(R.id.btn_done);
        _btn_refuse = (Button)v.findViewById(R.id.btn_refuse);
        _btn_deliver = (Button)v.findViewById(R.id.btn_deliver);
        _btn_cancel = (Button)v.findViewById(R.id.btn_cancel);
        _btn_sum = (Button)v.findViewById(R.id.btn_sum);
        _txtPhone = (TextView)v.findViewById(R.id.txtPhone);
        _grid_text = (TextView)v.findViewById(R.id.grid_text);
        _txt_customer_name = (TextView)v.findViewById(R.id.txt_customer_name);
        _txt_price_delevring = (TextView)v.findViewById(R.id.txt_price_delevring);
        _txt_price_total = (TextView)v.findViewById(R.id.txt_price_total);
        _txt_down_count = (TextView)v.findViewById(R.id.txt_down_count);
        _grid_image = (ImageView)v.findViewById(R.id.grid_image);

        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);

    }

    public void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_cadeau);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Button dialogButtonnon = (Button) dialog.findViewById(R.id.btn_accept);
        final EditText _edt_time = (EditText) dialog.findViewById(R.id.edt_time);
        final TextView _txt_id_order = (TextView) dialog.findViewById(R.id.txt_id_order);
        _txt_id_order.setText(id);
        dialogButtonnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn_call.setVisibility(View.VISIBLE);
                _rel.setVisibility(View.VISIBLE);
                _txtPhone.setVisibility(View.VISIBLE);
               // _linearLayout0.setVisibility(View.VISIBLE);
               RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); // You might want to tweak these to WRAP_CONTENT
                isDone = true;

                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
               // _linearLayout0.setLayoutParams(lp);
                //_linearLayout.setVisibility(View.GONE);
               // _relativeLayout.setBackgroundResource(R.color.colorPrimary);
                _relativeLayout.setVisibility(View.GONE);

                String url = Constants.APPLY_FOR_ORDER_URL;

                if (_edt_time.getText().toString().isEmpty())
                    _edt_time.setError(getString(R.string.error_field_required));
                else {
                    int number = Integer.parseInt(_edt_time.getText().toString());

                    if (number<5){
                        _edt_time.setError(getString(R.string.error_number_5));
                    }else{
                        applyForOrder(_edt_time.getText().toString(),url,getString(R.string.accept_order_success));
                        dialog.dismiss();
                    }

                }
            }
        });

        dialog.show();

    }


    private void fetchDetailsOrders(){

        String url = Constants.GET_DETAILS_ORDERS_URL+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken()+"&id="+id;

        _relativeLayoutGlob.setVisibility(View.GONE);
        _progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "fetchDetailsOrders url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "fetchDetailsOrders response === "+response.toString());
                _relativeLayoutGlob.setVisibility(View.VISIBLE);
                _progressBar.setVisibility(View.GONE);
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject catsArray = resJsonObj.getJSONObject("data");

                            id = catsArray.getString("id");

                            String created_at = catsArray.getString("created_at");
                            String updated_at = catsArray.getString("updated_at");
                            String order_cost = catsArray.getString("order_cost");
                            String delivery_cost = catsArray.getString("delivery_cost");
                            String amount = catsArray.getString("amount");
                            String scheduled_at = catsArray.getString("scheduled_at");
                            boolean is_scheduled = catsArray.getBoolean("is_scheduled");
                            String state = catsArray.getString("state");
                            String expect_time = catsArray.getString("expect_time");
                            String delivery_time = catsArray.getString("delivery_time");
                            String driver_id = catsArray.getString("driver_id");
                            String client_Latitude = catsArray.getString("client_Latitude");
                            String client_id = catsArray.getString("client_id");
                            String app_commission = catsArray.getString("app_commission");
                            String client_longitude = catsArray.getString("client_longitude");
                            String category_id = catsArray.getString("category_id");
                            String descripation = catsArray.getString("descripation");
                            String type = catsArray.getString("type");

                            JSONArray products = catsArray.getJSONArray("products");

                            for (int i =0;i<products.length();i++){
                                JSONObject jsonObject = products.getJSONObject(i);
                                String ar_name= jsonObject.getString("ar_name");
                                String en_name= jsonObject.getString("en_name");
                                String price= jsonObject.getString("price");
                                String thumbnail= jsonObject.getString("thumbnail");
                                JSONObject pivotObj = jsonObject.getJSONObject("pivot");
                                int quantity = pivotObj.getInt("quantity");

                                JSONObject category = jsonObject.getJSONObject("category");
                                String ar_name_cat= category.getString("ar_name");
                                String en_name_cat= category.getString("en_name");
                                String thumbnail_cat= category.getString("thumbnail");
                                if (!isRightToLeft ) {
                                    _grid_text.setText(en_name_cat);
                                }else  _grid_text.setText(ar_name_cat);
                                Picasso.with(getActivity())
                                        .load(baseUrlImages+thumbnail_cat)
                                        .fit()
                                        .into(_grid_image);
                                addViews(ar_name,price,quantity);

                            }
                        if (products.length()==0){
                            _relative1.setVisibility(View.GONE);
                        }

                        JSONObject client = catsArray.getJSONObject("client");

                        String name = client.getString("name");
                        String phone = client.getString("phone");
                        String home_phone = client.getString("home_phone");
                        String thumbnail = client.getString("thumbnail");
                        String stateClient = client.getString("state");
                        _txt_customer_name.setText(name);
                        _txtPhone.setText(phone);
                        phoneDriver = phone;
                        _txt_price_delevring.setText(delivery_cost+" "+getString(R.string.ryal));
                        _txt_price_total.setText(order_cost+" "+getString(R.string.ryal));
                        int total = Integer.parseInt(delivery_cost)+ Integer.parseInt(order_cost);
                        _btn_sum.setText(amount+" "+getString(R.string.ryal));
                        m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(client_Latitude), Double.parseDouble(client_longitude)), 15));
                        m_map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(client_Latitude), Double.parseDouble(client_longitude)))
                        )
                                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.pin));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  _relativeLayoutGlob.setVisibility(View.GONE);
                _progressBar.setVisibility(View.GONE);
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

    private void applyForOrder(final String time, String url, final String msgSuccess){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();
      //  _progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "applyForOrder url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // _progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                Log.e(TAG, "applyForOrder response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");

                    if (status.equals("1")){
                        _btn_cancel.setText(getString(R.string.cancelOrder));
                        _btn_done.setText(getString(R.string.txt_okrequest));
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msgSuccess,true,1);

                    }else{
                        String msg = resJsonObj.getString("msg");
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,1);

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
               // _progressBar.setVisibility(View.GONE);
                dialog.dismiss();
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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());
                params.put("order_id", ""+ id);
                params.put("expect_time", ""+time);

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

    private void notifyClient( ){
      //  final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
        //        getResources().getString(R.string.loading), true);
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();
      //  _progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "notifyClient url "+Constants.NOTIFY_CLIENT_URL);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, Constants.NOTIFY_CLIENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // _progressBar.setVisibility(View.GONE);
              //  dialog.dismiss();
                Log.e(TAG, "notifyClient response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");

                    if (status.equals("1")){
                    //    _btn_cancel.setText(getString(R.string.cancelOrder));
                      //  _btn_done.setText(getString(R.string.txt_okrequest));
                       // AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.messagesucess),true,0);

                    }else{
                        String msg = resJsonObj.getString("msg");
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);

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
               // _progressBar.setVisibility(View.GONE);
               // dialog.dismiss();
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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());
                params.put("order_id", ""+ id);


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

    private void addViews(String ar_name, String price, int quantity){

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.products, null);
        _linearProducts.addView(v);
        _linearProducts.requestLayout();
        _txt_product = (TextView)v.findViewById(R.id.txt_product);
        _txt_price = (TextView)v.findViewById(R.id.txt_price);

        _txt_product.setText(ar_name);
         int p = Integer.parseInt(price)*quantity;
        _txt_price.setText(p+" "+getString(R.string.ryal));


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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
        if(m_map != null) {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo  mg3 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(  isGPSEnabled&& ( mWifi.isConnected() ||  mg3.isConnected())){

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return;
                } else {
                    m_map.setMyLocationEnabled(true);
                }
            }

            m_map.getUiSettings().setMyLocationButtonEnabled(true);

          /*  m_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (latLng != null)
                        displayCurrentLocation(latLng.latitude, latLng.longitude);
                }
            });
            */


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CALL:
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);

                    return;
                } else {
                    if (!phoneDriver.isEmpty()) {
                        Intent intent = new Intent();
                        Uri uri = Uri.parse("tel:" + phoneDriver.trim());
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }

                break;


        }
    }
}
