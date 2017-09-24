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

public class Cosmetics_Inventory extends Activity{

    ListView myList2;
    Button getChoice2;
    SharedPreferences sharedpreferences2 ;
    public static final String MyPREFERENCES2 = "MyUserChoice2";
    ArrayList<String> selectedItems2 = new ArrayList<String>();


    String Username;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "Username";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cosmetics_inventory);
        myList2 = (ListView)findViewById(R.id.list1);
        getChoice2 = (Button)findViewById(R.id.getchoice1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.cosmetics_items));
        myList2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList2.setAdapter(adapter2);
        sharedpreferences2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
        if(sharedpreferences2.contains(MyPREFERENCES2)){
            LoadSelections2();
        }

        getChoice2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected2 = "";
                int cntChoice2 = myList2.getCount();
                SparseBooleanArray sparseBooleanArray2 = myList2.getCheckedItemPositions();
                for (int i = 0; i < cntChoice2; i++) {
                    if (sparseBooleanArray2.get(i)) {
                        selected2 += myList2.getItemAtPosition(i).toString() + "\n";
                        Log.d("list", selected2);
                        System.out.println("Checking list while adding:" + myList2.getItemAtPosition(i).toString());
                        SaveSelections2();
                    }
                }
//                Intent and_intent=new Intent(getApplicationContext(),Inventory.class);
//                startActivity(and_intent);
                finish();
            }
        });
    }



    private void SaveSelections2() {
        SharedPreferences.Editor prefEditor2 = sharedpreferences2.edit();
        String savedItems2 = getSavedItems2();
        prefEditor2.putString(MyPREFERENCES2, savedItems2);
        prefEditor2.apply();
    }
    private String getSavedItems2() {
        String savedItems2 = "";
        int count2 = this.myList2.getAdapter().getCount();
        for (int i = 0; i < count2; i++) {
            if (this.myList2.isItemChecked(i)) {
                if (savedItems2.length() > 0) {
                    savedItems2 += "," + this.myList2.getItemAtPosition(i);
                } else {
                    savedItems2 += this.myList2.getItemAtPosition(i);
                }
            }
        }
        return savedItems2;
    }

    private void LoadSelections2() {
        if (sharedpreferences2.contains(MyPREFERENCES2)) {
            String savedItems2 = sharedpreferences2.getString(MyPREFERENCES2, "");
            selectedItems2.addAll(Arrays.asList(savedItems2.split(",")));
            int count2 = this.myList2.getAdapter().getCount();
            for (int i = 0; i < count2; i++) {
                String currentItem2 = (String) myList2.getAdapter()
                        .getItem(i);
                if (selectedItems2.contains(currentItem2)) {
                    myList2.setItemChecked(i, true);

                } else {
                    myList2.setItemChecked(i, false);
                }
            }
        }
    }

}


