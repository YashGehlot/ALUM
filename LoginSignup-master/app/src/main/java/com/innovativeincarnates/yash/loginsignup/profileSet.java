package com.innovativeincarnates.yash.loginsignup;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.innovativeincarnates.yash.loginsignup.R.id.editTextPassword;
import static com.innovativeincarnates.yash.loginsignup.R.id.editTextUserName;


/**
 * Created by yash on 9/3/17.
 */

public class profileSet extends Activity {

    Button save;
    EditText editTextfullName,editTextphoneNo,editTextshopName,editTextAddress;

    String USERNAME;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        final Dialog dialog = new Dialog(profileSet.this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");

        save =(Button)findViewById(R.id.profile_Save);


        editTextfullName=(EditText)findViewById(R.id.profile_full_name);
        editTextphoneNo=(EditText)findViewById(R.id.phone_no);
        editTextshopName=(EditText)findViewById(R.id.shop_name);
        editTextAddress=(EditText)findViewById(R.id.address);

    // Set On ClickListener
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                String fullname=editTextfullName.getText().toString();
                String phoneno=editTextphoneNo.getText().toString();
                String shopname=editTextshopName.getText().toString();
                String address=editTextAddress.getText().toString();
                uploadData(fullname,phoneno,shopname,address);
                USERNAME=fullname;
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREFS_USERNAME, USERNAME);
                editor.apply();
            }
        });

    }

    public static final String UPLOAD_URL = "http://10.42.0.1:8081/set_Profile";

    private void uploadData(final String a,final String b,final String c,final String d){

    final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //Disimissing the progress dialog
                    loading.dismiss();
                    //Showing toast message of the response
                    Log.d(TAG,"Listener");

                    // Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();

                        Toast.makeText(profileSet.this, "Saved your profile.\n Turn on your mobile hotspot with SSID '3G'" , Toast.LENGTH_LONG).show();
                        Intent intentact=new Intent(getApplicationContext(),Inventory.class);
                        startActivity(intentact);
                        finish();

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //Dismissing the progress dialog
                    loading.dismiss();
                    Log.e(TAG,"ErrorListener"+volleyError.getMessage());
                    //Showing toast
                    Toast.makeText(profileSet.this, "Unable to update inventory", Toast.LENGTH_LONG).show();
                }
            }){


        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            //Converting Bitmap to String
            /*
            String Username = "Username";
            String Phone = "Phone";
            String shopname = "ArIES";
            String Address = "ECE_Dept";
            */

            //Creating parameters
            Map<String,String> params = new Hashtable<String, String>();

            String KEY_Username = "Username";
            String KEY_Phone = "Phone";
            String KEY_shopeName = "shopname";
            String KEY_Address = "Address";

            params.put(KEY_Username, a);
            params.put(KEY_Phone, b);
            params.put(KEY_shopeName, c);
            params.put(KEY_Address, d);

            //returning parameters
            return params;
        }
    };

    //Creating a Request Queue
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    protected void onStop()
    {
        super.onStop();
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(PREFS_USERNAME, USERNAME);
//        editor.apply();
    }

}