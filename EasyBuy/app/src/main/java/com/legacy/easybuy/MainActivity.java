package com.legacy.easybuy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "DataFile";
    public static final String PREFS_USERNAME = "USERNAME";
    public static final String PREFS_PASSWORD = "PASSWORD";
    public static final String PREFS_LOGIN = "LOGIN";
    String Username, Password;
    Boolean loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Username = settings.getString(PREFS_USERNAME, "default");
        Password = settings.getString(PREFS_PASSWORD, "default");
        loginStatus = settings.getBoolean(PREFS_LOGIN, false);

        if (loginStatus) {
            Intent and_intent = new Intent(getApplicationContext(), TodoListActivity.class);
            startActivity(and_intent);
            finish();
        }
    }

    public void sign_up(View view){
        Intent intentSignUP = new Intent(getApplicationContext(),SignUPActivity.class);
        startActivity(intentSignUP);
    }

    public void sign_in(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.sign_in_dialog, null);
        mBuilder.setTitle("Enter your credentials");
        final EditText ad_username = (EditText) mView.findViewById(R.id.editTextUserNameToLogin);
        final EditText ad_password = (EditText) mView.findViewById(R.id.editTextPasswordToLogin);

        mBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ad_username.getText().toString().equals(Username) && ad_password.getText().toString().equals(Password)) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(PREFS_LOGIN, true);
                    editor.apply();
                    Intent and_intent = new Intent(getApplicationContext(),TodoListActivity.class);
                    startActivity(and_intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
