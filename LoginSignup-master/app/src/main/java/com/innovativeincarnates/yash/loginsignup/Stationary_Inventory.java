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


/**
 * Created by sakshi on 3/21/2017.
 */

public class Stationary_Inventory extends Activity{

    ListView myList3;
    Button getChoice3;
    SharedPreferences sharedpreferences3 ;
    public static final String MyPREFERENCES3 = "MyUserChoice3" ;
    ArrayList<String> selectedItems3 = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stationary_inventory);
        myList3 = (ListView)findViewById(R.id.list2);
        getChoice3 = (Button)findViewById(R.id.getchoice2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.stationary_items));
        myList3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList3.setAdapter(adapter3);
        sharedpreferences3 = getSharedPreferences(MyPREFERENCES3, Context.MODE_PRIVATE);
        if(sharedpreferences3.contains(MyPREFERENCES3)){
            LoadSelections3();
        }

        getChoice3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected3 = "";
                int cntChoice3 = myList3.getCount();
                SparseBooleanArray sparseBooleanArray = myList3.getCheckedItemPositions();
                for (int i = 0; i < cntChoice3; i++) {
                    if (sparseBooleanArray.get(i)) {
                        selected3 += myList3.getItemAtPosition(i).toString() + "\n";
                        Log.d("list", selected3);
                        System.out.println("Checking list while adding:" + myList3.getItemAtPosition(i).toString());
                        SaveSelections3();
                    }
                }

                finish();

            }
        });
    }



    private void SaveSelections3() {
        SharedPreferences.Editor prefEditor3 = sharedpreferences3.edit();
        String savedItems3 = getSavedItems3();
        prefEditor3.putString(MyPREFERENCES3, savedItems3);
        prefEditor3.apply();
    }
    private String getSavedItems3() {
        String savedItems3 = "";
        int count3 = this.myList3.getAdapter().getCount();
        for (int i = 0; i < count3; i++) {
            if (this.myList3.isItemChecked(i)) {
                if (savedItems3.length() > 0) {
                    savedItems3 += "," + this.myList3.getItemAtPosition(i);
                } else {
                    savedItems3 += this.myList3.getItemAtPosition(i);
                }
            }
        }
        return savedItems3;
    }

    private void LoadSelections3() {
        if (sharedpreferences3.contains(MyPREFERENCES3)) {
            String savedItems3 = sharedpreferences3.getString(MyPREFERENCES3, "");
            selectedItems3.addAll(Arrays.asList(savedItems3.split(",")));
            int count3 = this.myList3.getAdapter().getCount();
            for (int i = 0; i < count3; i++) {
                String currentItem3 = (String) myList3.getAdapter()
                        .getItem(i);
                if (selectedItems3.contains(currentItem3)) {
                    myList3.setItemChecked(i, true);

                } else {
                    myList3.setItemChecked(i, false);
                }
            }
        }
    }




}


