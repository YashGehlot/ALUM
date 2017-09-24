package com.innovativeincarnates.yash.loginsignup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Inventory extends Activity{

    String Username;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "Username";


    Button btngrocery , btncosmetics , btnstationary ;
    TextView specialOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Username = settings.getString(PREFS_USERNAME, "default");

        btngrocery=(Button)findViewById(R.id.buttongrocery);
        btngrocery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intent1=new Intent(getApplicationContext(),Grocery_Inventory.class);
                startActivity(intent1);
            }
        });

        btncosmetics=(Button)findViewById(R.id.buttoncosmetics);
        btncosmetics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intent2=new Intent(getApplicationContext(),Cosmetics_Inventory.class);
                startActivity(intent2);
            }
        });

        btnstationary=(Button)findViewById(R.id.buttonstationary);
        btnstationary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intent3=new Intent(getApplicationContext(),Stationary_Inventory.class);
                startActivity(intent3);
            }
        });
        specialOffers = (TextView) findViewById(R.id.specialOffer);
    }

    public void SpecialOffer(View V)
    {
        final Dialog dialog = new Dialog(Inventory.this);
        dialog.setContentView(R.layout.special_offer_layout);
        dialog.setTitle("Your Special Offer");

        // get the Refferences of views
        final  EditText editTextSpecial=(EditText)dialog.findViewById(R.id.editTextSpecialOffer);

        Button btnUpdateSpeial=(Button)dialog.findViewById(R.id.buttonSave);

        // Set On ClickListener

        btnUpdateSpeial.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String specialOffer=editTextSpecial.getText().toString();
                sendOffer(specialOffer);
                dialog.cancel();

            }
        });

        dialog.show();
    }


    public void update(View view){
        String selected = "";
        String MyPREFERENCES1 = "MyUserChoice1" ;
        SharedPreferences sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        ArrayList<String> selectedItems1 = new ArrayList<String>();
        if (sharedpreferences1.contains(MyPREFERENCES1)) {
            String savedItems = sharedpreferences1.getString(MyPREFERENCES1, "");
            selectedItems1.addAll(Arrays.asList(savedItems.split(",")));
            int count = selectedItems1.size();
            for (int i = 0; i < count; i++) {
                selected += ","+selectedItems1.get(i);
            }

        }
        String MyPREFERENCES2 = "MyUserChoice2" ;
        SharedPreferences sharedpreferences2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
        ArrayList<String> selectedItems2 = new ArrayList<String>();
        if (sharedpreferences2.contains(MyPREFERENCES2)) {
            String savedItems = sharedpreferences2.getString(MyPREFERENCES2, "");
            selectedItems2.addAll(Arrays.asList(savedItems.split(",")));
            int count = selectedItems2.size();
            for (int i = 0; i < count; i++) {
                selected += ","+selectedItems2.get(i);
            }

        }
        String MyPREFERENCES3 = "MyUserChoice3" ;
        SharedPreferences sharedpreferences3 = getSharedPreferences(MyPREFERENCES3, Context.MODE_PRIVATE);
        ArrayList<String> selectedItems3 = new ArrayList<String>();
        if (sharedpreferences3.contains(MyPREFERENCES3)) {
            String savedItems = sharedpreferences3.getString(MyPREFERENCES3, "");
            selectedItems3.addAll(Arrays.asList(savedItems.split(",")));
            int count = selectedItems3.size();
            for (int i = 0; i < count; i++) {
                selected += ","+selectedItems3.get(i);
            }

        }
        addList(selected);
        //Log.d("list", selected);
    }

    public static final String UPLOAD_URL = "http://10.42.0.1:8081/seller_update_inventory";

    private void addList(final String Inventory){
//*/
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

                        Toast.makeText(Inventory.this, "Updated your Inventory" , Toast.LENGTH_LONG).show();


                    }
                    },
                    new Response.ErrorListener() {
                        @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e(TAG,"ErrorListener"+volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(Inventory.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
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
                String KEY_Inventory = "Inventory";

                params.put(KEY_Username, Username);
                params.put(KEY_Inventory, Inventory);
                //Toast.makeText(Inventory.this,Username,Toast.LENGTH_LONG).show();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    public static final String UPLOAD_URL_1 = "http://10.42.0.1:8081/seller_offer";

    private void sendOffer(final String Offer){

        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Log.d(TAG,"Listener");
                        if(s.equals("Yes")) {
                            specialOffers.setText(Offer);
                            Toast.makeText(Inventory.this, "Your Offer has been Updated", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e(TAG,"ErrorListener"+volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(Inventory.this, "Unable to update offer", Toast.LENGTH_LONG).show();
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
                String KEY_Offer = "Offer";

                params.put(KEY_Username, Username);
                params.put(KEY_Offer, Offer);
                //Toast.makeText(Inventory.this,Username,Toast.LENGTH_LONG).show();
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

