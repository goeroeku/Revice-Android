package com.linkensky.revice.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.realm.CurrentUserModel;

import io.realm.Realm;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Realm realm = Realm.getInstance(this);
        CurrentUserModel currentUser = realm.where(CurrentUserModel.class).findFirst();
        if(currentUser != null){
            //Set all view
            TextView nama = (TextView) findViewById(R.id.tvNama);
            TextView email = (TextView) findViewById(R.id.tvEmail);
            TextView alamat = (TextView) findViewById(R.id.tvAlamat);
            TextView hp = (TextView) findViewById(R.id.tvHp);

            nama.setText(currentUser.getNama());
            email.setText(currentUser.getEmail());
            alamat.setText(currentUser.getAlamat());
            hp.setText(currentUser.getTelp());
        }
    }

}
