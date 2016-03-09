package com.linkensky.revice.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get order data
        String orderId = getIntent().getStringExtra("orderId");
        Realm realm = Realm.getInstance(this);
        order = realm.where(OrderModel.class).equalTo("id", orderId).findFirst();

        final TextView id = (TextView) findViewById(R.id.tvIdPesan);
        TextView lokasi = (TextView) findViewById(R.id.tvDetailLokasi);
        TextView masalah = (TextView) findViewById(R.id.tvDetailMasalah);
        TextView tipe = (TextView) findViewById(R.id.tvTipe);
        TextView status = (TextView) findViewById(R.id.tvNotifDesc);
        Button bBatal = (Button) findViewById(R.id.bBatal);
        bBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class,
                        sharedPreferences.getString(RevicePreferences.AUTH_TOKEN, ""));
                Call<Order> orderCall1 = reviceApi.editStatus(id.getText().toString(),new StatusRequest(2, ""));
                orderCall1.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccess()) {
                            ShowDialog("Sukses!", "Status Di Ubah");
                        } else {
                            ShowDialog("Error!", "Gagal Mengubah Status, EOD02");
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        ShowDialog("Error!", "Gagal Mengubah Status, EOD03");

                    }
                });
            }
        });

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

        pos = new LatLng(Double.parseDouble(order.getLat()), Double.parseDouble(order.getLng()));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOrder);
        mapFragment.getMapAsync(this);

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
