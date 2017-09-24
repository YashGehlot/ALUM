package com.innovativeincarnates.yash.loginsignup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.Bundle;
import android.view.View.OnClickListener;
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
import static com.innovativeincarnates.yash.loginsignup.profileSet.UPLOAD_URL;


/**
 * Created by sakshi on 3/21/2017.
 */

public class Grocery_Inventory extends Activity{

    ListView myList1;
    Button getChoice1;
    SharedPreferences sharedpreferences1 ;
    public static final String MyPREFERENCES1 = "MyUserChoice1" ;
    ArrayList<String> selectedItems1 = new ArrayList<String>();
    /*
    String Username;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "Username";
    */
    @Override

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            /*
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            Username = settings.getString(PREFS_USERNAME, "default");
            */
            setContentView(R.layout.grocery_inventory);
            myList1 = (ListView)findViewById(R.id.list);
            getChoice1 = (Button)findViewById(R.id.getchoice);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.grocery_items));
            myList1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            myList1.setAdapter(adapter1);
            sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
            if(sharedpreferences1.contains(MyPREFERENCES1)){
                LoadSelections1();
            }

        getChoice1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected1 = "";
                int cntChoice1 = myList1.getCount();
                SparseBooleanArray sparseBooleanArray = myList1.getCheckedItemPositions();
                for (int i = 0; i < cntChoice1; i++) {
                    if (sparseBooleanArray.get(i)) {
                        selected1 += myList1.getItemAtPosition(i).toString() + "\n";
                        Log.d("list", selected1);
                        System.out.println("Checking list while adding:" + myList1.getItemAtPosition(i).toString());
                        SaveSelections1();
                    }

                }

                finish();

            }
        });
    }



    private void SaveSelections1() {
        SharedPreferences.Editor prefEditor1 = sharedpreferences1.edit();
        String savedItems1 = getSavedItems1();
        prefEditor1.putString(MyPREFERENCES1, savedItems1);
        prefEditor1.apply();
    }
    private String getSavedItems1() {
       String savedItems1 = "";
        int count1 = this.myList1.getAdapter().getCount();
        for (int i = 0; i < count1; i++) {
            if (this.myList1.isItemChecked(i)) {
                if (savedItems1.length() > 0) {
                    savedItems1 += "," + this.myList1.getItemAtPosition(i);
                } else {
                    savedItems1 += this.myList1.getItemAtPosition(i);
                }
            }
        }
        return savedItems1;
    }

    private void LoadSelections1() {
        if (sharedpreferences1.contains(MyPREFERENCES1)) {
            String savedItems1 = sharedpreferences1.getString(MyPREFERENCES1, "");
            selectedItems1.addAll(Arrays.asList(savedItems1.split(",")));
            int count1 = this.myList1.getAdapter().getCount();
            for (int i = 0; i < count1; i++) {
                String currentItem1 = (String) myList1.getAdapter()
                        .getItem(i);
                if (selectedItems1.contains(currentItem1)) {
                    myList1.setItemChecked(i, true);

                } else {
                    myList1.setItemChecked(i, false);
                }
            }
        }
    }


}


