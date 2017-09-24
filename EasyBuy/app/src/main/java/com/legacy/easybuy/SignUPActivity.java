package com.legacy.easybuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;


public class SignUPActivity extends AppCompatActivity {

    EditText usernameEdit, passwordEdit, confirmpasswordEdit,phoneEdit;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "USERNAME";
    public static final String PREFS_PASSWORD = "PASSWORD";
//    public static final String PREFS_CONFIRM_PASSWORD = "PASSWORD";
    public static final String PREFS_LOGIN = "LOGIN";
    public static final String UPLOAD_URL = "http://10.42.0.1:8081/buyer_set_profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        usernameEdit = (EditText) findViewById(R.id.editTextUserName);
        passwordEdit = (EditText) findViewById(R.id.editTextPassword);
        confirmpasswordEdit = (EditText) findViewById(R.id.editTextConfirmPassword);
        phoneEdit = (EditText) findViewById(R.id.phone_no);
    }

    public void onSave(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirmpassword = confirmpasswordEdit.getText().toString();
        if(username.equals("")||password.equals("")||confirmpassword.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(confirmpassword))
        {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            editor.putString(PREFS_USERNAME, username);
            editor.putString(PREFS_PASSWORD, passwordEdit.getText().toString());
            editor.putBoolean(PREFS_LOGIN, true);
            editor.apply();
            sendProfile();
            Intent and_intent = new Intent(getApplicationContext(), TodoListActivity.class);
            startActivity(and_intent);
            finish();
        }
    }

    private void sendProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals("Yes"))
                        Toast.makeText(SignUPActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SignUPActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();

                String KEY_Username = "Username";
                String KEY_Phone = "Phone";

                params.put(KEY_Username, usernameEdit.getText().toString());
                params.put(KEY_Phone, phoneEdit.getText().toString());

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
