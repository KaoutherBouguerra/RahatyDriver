package art4muslim.macbook.rahatydriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import art4muslim.macbook.rahatydriver.application.BaseApplication;
import art4muslim.macbook.rahatydriver.session.Constants;
import art4muslim.macbook.rahatydriver.session.SessionManager;
import art4muslim.macbook.rahatydriver.utils.circularimageview.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
public class LoginActivity extends AppCompatActivity {
    TextView btForgetPassword;
    CheckBox checkbox;
    Button btSignIn;
    @NotEmpty(messageId =  R.string.validation_mobile, order = 1)
    protected EditText inputPhone;
    @NotEmpty(messageId = R.string.nonEmpty, order = 2)
    @MinLength(value = 4, messageId =  R.string.validation_number_length, order = 3)
    protected EditText inputPassword;
    private ProgressDialog pDialog;
    public static final String KEY_PHONE= "user_phone";
    public static final String KEY_PASSWORD = "user_pass";
    private static final String TAG = LoginActivity.class.getSimpleName();
    // Session Manager Class
    SessionManager session;
    public static String AccessTOken = null;
    boolean isRightToLeft  ;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeField();

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // LoginFirst();

                attemptLogin();
            }
        });
        btForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is checkbox checked?
                if (((CheckBox) v).isChecked()) {
                    BaseApplication.session.saveLoginState(true);
                } else {
                    BaseApplication.session.saveLoginState(false);
                }

            }
        });
     /*   checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        */
    }

    public void initializeField() {
        inputPhone = (EditText) findViewById(R.id.etphone);
        inputPassword = (EditText) findViewById( R.id.etPassword);
        btSignIn = (Button) findViewById(R.id.btn_signin);


        _progressBar=(ProgressBar) findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout4);

        btForgetPassword = (TextView) findViewById(R.id.btnForgetPassword);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
    }


    private void attemptLogin() {


        // Reset errors.
        inputPhone.setError(null);
        inputPassword.setError(null);


        // Store values at the time of the login attempt.
        String phone = inputPhone.getText().toString();
        String password = inputPassword.getText().toString();


        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            inputPhone.setError(getString(R.string.error_field_required));
            focusView = inputPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_field_required));
            focusView = inputPassword;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);

            _progressBar.setVisibility(View.VISIBLE);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
            LoginFirst(phone, password);
        }
    }


    private void LoginFirst(final String phone, final String password) {

        String url = Constants.LOGIN_URL;
        //+"phone="+phone+"&password="+password;

        Log.e(TAG, "LoginFirst url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(Constants.androiStudioMode.equals("debug")) {
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", response);
                }
                Log.v("LoginFirstRequest","response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status==1) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        JSONObject userObj = dataObj.getJSONObject("user");
                        String api_token= dataObj.getString("api_token");

                        BaseApplication.session.saveAccessToken(api_token);
                        int id = userObj.getInt("id");
                        String name = userObj.getString("name");
                        String phone = userObj.getString("phone");
                        String longitude = userObj.getString("longitude");
                        String latitude = userObj.getString("latitude");
                        String is_paid = userObj.getString("is_paid");
                        String thumbnail = userObj.getString("thumbnail");
                      //  String state = userObj.getString("state");

                        BaseApplication.session.createLoginSession(id,name,phone,"",thumbnail,"");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {

                        String msg = jsonObject.getString("msg");
                        inputPhone.setText("");
                        inputPassword.setText("");
                        _linearLayout.setVisibility(View.VISIBLE);
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),msg,false,0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

                if (error instanceof AuthFailureError) {

                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.networkerror),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {
            /*     @Override
                public String getBodyContentType() {
                    return "application/json";
                }

               @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", ""+ password);
                params.put("locale", "ar");
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
        BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


    }
}
