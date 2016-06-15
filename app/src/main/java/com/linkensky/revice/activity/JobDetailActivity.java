package com.linkensky.revice.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import com.linkensky.revice.api.model.OrderItem;
import com.linkensky.revice.api.model.StatusRequest;
import com.linkensky.revice.realm.CurrentUserModel;
import com.linkensky.revice.realm.ServiceModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SharedPreferences sharedPreferences;
    private Realm realm;
    private CurrentUserModel currentUser;
    private LatLng pos;
    private GoogleMap mMap;
    private OrderItem item;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class,
                sharedPreferences.getString(RevicePreferences.AUTH_TOKEN, ""), this);

        String id = getIntent().getStringExtra("orderId");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        LoadData(reviceApi, id);

        Button bTerima = (Button) findViewById(R.id.bTerima);
        bTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Memproses...");
                progressDialog.show();
                ServiceModel service = currentUser.getService().first();
                Call<Order> orderStatus = reviceApi.editStatus(item.getId(),new StatusRequest(1, service.getId()));
                orderStatus.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccess()) {
                            progressDialog.hide();
                            ShowDialog("Sukses!", "Job Di Terima");
                        } else {
                            progressDialog.hide();
                            ShowDialog("Error!", "Gagal Menerima Job, Error: EJD04");
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        progressDialog.hide();
                        ShowDialog("Error!", "Gagal Menerima Job,Error: EJD05");
                    }
                });
            }
        });
        Button bPanggil = (Button) findViewById(R.id.bPanggil);
        bPanggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + item.getUserData().getTelp()));
                startActivity(callIntent);
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapJob);
        mapFragment.getMapAsync(this);

    }

    private void LoadData(ReviceApi reviceApi, String id) {
        realm = Realm.getInstance(this);

        currentUser = realm.where(CurrentUserModel.class).findFirst();

        final TextView idPesan = (TextView) findViewById(R.id.tvIdPesan);
        final TextView lokasi = (TextView) findViewById(R.id.tvDetailLokasi);
        final TextView masalah = (TextView) findViewById(R.id.tvDetailMasalah);
        final TextView tipe = (TextView) findViewById(R.id.tvTipe);
        final TextView status = (TextView) findViewById(R.id.tvStatus);
        final TextView userId = (TextView) findViewById(R.id.tvUserId);
        final TextView nama = (TextView) findViewById(R.id.tvNama);
        final TextView alamat = (TextView) findViewById(R.id.tvAlamat);
        final TextView telp = (TextView) findViewById(R.id.tvTelp);

        progressDialog.setMessage("Mengambil Data");
        progressDialog.show();
        final Call<Order> orderCall = reviceApi.getOrder(id);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccess()) {
                    item = response.body().getOrder();
                    idPesan.setText(item.getId());
                    masalah.setText(item.getProblemDesc());
                    lokasi.setText(item.getLocationDesc());
                    tipe.setText(item.getProblemType());

                    String statusText = "";
                    switch (Integer.parseInt(item.getStatus())){
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
                    userId.setText(item.getUserId());
                    nama.setText(item.getUserData().getNama());
                    alamat.setText(item.getUserData().getAlamat());
                    telp.setText(item.getUserData().getTelp());

                    pos = new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()));

                    if (mMap != null) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(pos)
                                .title("Lokasi Anda");
                        Marker marker = mMap.addMarker(markerOptions);
                        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(pos, 15);

                        mMap.animateCamera(center);
                    }

                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    ShowDialog("Error!", "Gagal Mengambil Data, Error: EJD01");
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                progressDialog.dismiss();
                ShowDialog("Error!", "Gagal Mengambil Data, Error: EJD02");
            }
        });
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

        if(pos != null){
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pos)
                    .title("Lokasi Anda");
            Marker marker = mMap.addMarker(markerOptions);
            CameraUpdate center= CameraUpdateFactory.newLatLngZoom(pos, 15);

            mMap.animateCamera(center);
        }
    }
}
