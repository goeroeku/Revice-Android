package com.linkensky.revice.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.utils.ServiceParcelable;

public class ServiceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ServiceParcelable serviceParcelable = getIntent().getParcelableExtra("service");
        TextView nama = (TextView) findViewById(R.id.tvNama);
        TextView kategori = (TextView) findViewById(R.id.tvKategori);
        TextView keterangan = (TextView) findViewById(R.id.tvKeterangan);
        TextView telp = (TextView) findViewById(R.id.tvHp);
        TextView alamat = (TextView) findViewById(R.id.tvAlamat);

        //Set Text
        nama.setText(serviceParcelable.getNama());
        kategori.setText(serviceParcelable.getTipeName());
        keterangan.setText(serviceParcelable.getDeskripsi());
        telp.setText(serviceParcelable.getTelp());
        alamat.setText(serviceParcelable.getAlamat());
    }

}
