package com.legacy.easybuy;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class TodoListActivity extends AppCompatActivity {

    private CheckBox scanToggle;
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    String Username, List;
    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "USERNAME";
    public static final String UPLOAD_URL = "http://10.42.0.1:8081/buyer_update_inventory";
    public static final String PREFS_LOGIN = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        scanToggle = (CheckBox) findViewById(R.id.checkbox);
        View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scanToggle.isChecked()) {
                    startScan();
                } else {
                    stopScan();
                }
            }
        };

        scanToggle.setOnClickListener(checkBoxListener);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Username = settings.getString(PREFS_USERNAME, "default");
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScanStatus(scanToggle.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        scanToggle.setChecked(loadScanStatus());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(PREFS_LOGIN, false);
                editor.apply();
                Intent and_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(and_intent);
                finish();
                return true;
            case R.id.action_add_task:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner , null);
                mBuilder.setTitle("Select a new item");

                final Spinner mSpinner1 = (Spinner) mView.findViewById(R.id.spinner1);
                final Spinner mSpinner2 = (Spinner) mView.findViewById(R.id.spinner2);
                try {
                    Field popup = Spinner.class.getDeclaredField("mPopup");
                    popup.setAccessible(true);
                    android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mSpinner2);
                    popupWindow.setHeight(700);
                } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.items_to_do));
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner1.setAdapter(adapter1);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.items1));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner2.setAdapter(adapter2);

                mSpinner1.setOnItemSelectedListener(new OnSpinnerItemClicked(mSpinner2));

                mBuilder.setPositiveButton("Add" , new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String task = String.valueOf(mSpinner2.getSelectedItem());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                });
                mBuilder.setNegativeButton("CANCEL" , new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setView(mView);
                mBuilder.setCancelable(true);
                mBuilder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.todoitem,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        public Spinner mSpinner2;
        public OnSpinnerItemClicked(Spinner spinner) {
            this.mSpinner2 = spinner;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            if (pos == 0){
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(TodoListActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.items1));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner2.setAdapter(adapter2);
            } else if (pos == 1) {
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(TodoListActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.items2));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner2.setAdapter(adapter2);
            } else if (pos == 2) {
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(TodoListActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.items3));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner2.setAdapter(adapter2);
            }
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private void saveScanStatus(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check", isChecked);
        editor.apply();
    }

    private boolean loadScanStatus() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check", false);
    }

    private void startScan() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    private void stopScan() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void updateList(View view){
        List = "";
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            List += "," + cursor.getString(idx);
        }
        Log.d("list", List);
        cursor.close();
        db.close();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals("Yes"))
                            Toast.makeText(TodoListActivity.this, "List Updated", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(TodoListActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                          Toast.makeText(TodoListActivity.this, "Unable to update list.", Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();

                String KEY_Username = "Username";
                String KEY_List = "Inventory";

                params.put(KEY_Username, Username);
                params.put(KEY_List, List);

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
