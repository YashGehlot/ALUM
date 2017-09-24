package com.legacy.easybuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        String s = getIntent().getStringExtra("Profile");
        String[] info = s.split("\\$");
        TextView itemName = (TextView) findViewById(R.id.itemName);
        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAdd = (TextView) findViewById(R.id.shopAdd);
        TextView shopOffer = (TextView) findViewById(R.id.shopOffer);
        itemName.setText(info[0]);
        shopName.setText(info[1]);
        shopAdd.setText(info[2]);
        shopOffer.setText(info[3]);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}