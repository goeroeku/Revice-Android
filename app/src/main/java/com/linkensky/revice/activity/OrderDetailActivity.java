package com.linkensky.revice.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linkensky.revice.R;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.api.ReviceApi;
import com.linkensky.revice.api.ServiceGenerator;
import com.linkensky.revice.api.model.Order;
import com.linkensky.revice.api.model.StatusRequest;
import com.linkensky.revice.realm.OrderModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng pos;
    private GoogleMap mMap;
    private OrderModel order;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private TextView id;
    private TextView lokasi;
    private TextView masalah;
    private TextView tipe;
    private TextView status;
    private Realm realm;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get order data
        orderId = getIntent().getStringExtra("orderId");
        realm = Realm.getInstance(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);


        id = (TextView) findViewById(R.id.tvIdPesan);
        lokasi = (TextView) findViewById(R.id.tvDetailLokasi);
        masalah = (TextView) findViewById(R.id.tvDetailMasalah);
        tipe = (TextView) findViewById(R.id.tvTipe);
        status = (TextView) findViewById(R.id.tvNotifDesc);
        Button bBatal = (Button) findViewById(R.id.bBatal);


        LoadData();

        bBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mengubah status!");
                progressDialog.show();
                final ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class,
                        sharedPreferences.getString(RevicePreferences.AUTH_TOKEN, ""), OrderDetailActivity.this);
                Call<Order> orderCall1 = reviceApi.editStatus(id.getText().toString(), new StatusRequest(2, ""));
                orderCall1.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        progressDialog.dismiss();
                        if (response.isSuccess()) {
                            ShowDialog("Sukses!", "Status Di Ubah");
                            LoadData();
                        } else {
                            ShowDialog("Error!", "Gagal Mengubah Status, EOD02");
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        progressDialog.dismiss();
                        ShowDialog("Error!", "Gagal Mengubah Status, EOD03");

                    }
                });
            }
        });
        pos = new LatLng(Double.parseDouble(order.getLat()), Double.parseDouble(order.getLng()));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOrder);
        mapFragment.getMapAsync(this);

    }

    private void LoadData() {
        order = realm.where(OrderModel.class).equalTo("id", orderId).findFirst();
        id.setText(order.getId());
        lokasi.setText(order.getLocation());
        masalah.setText(order.getDesc());
        tipe.setText(order.getType());
        String statusText = "";
        switch (order.getStatus()){
            case 0:
                statusText = "Menunggu";
                break;
            case 1:
                statusText = "Diterima";
                break;
            case 2:
                statusText = "Dibatalkan";
                break;
        }

        status.setText(statusText);
    }

    public void ShowDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pos)
                .title("Lokasi Anda");
        Marker marker = mMap.addMarker(markerOptions);
        CameraUpdate center=CameraUpdateFactory.newLatLngZoom(pos, 15);

        mMap.animateCamera(center);
    }
}
