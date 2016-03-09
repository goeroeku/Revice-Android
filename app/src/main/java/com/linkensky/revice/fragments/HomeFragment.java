package com.linkensky.revice.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linkensky.revice.R;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.activity.MainActivity;
import com.linkensky.revice.activity.ServiceDetailActivity;
import com.linkensky.revice.api.ReviceApi;
import com.linkensky.revice.api.ServiceGenerator;
import com.linkensky.revice.api.model.Auth;
import com.linkensky.revice.api.model.ClosestServices;
import com.linkensky.revice.api.model.Credentials;
import com.linkensky.revice.api.model.LocationRequest;
import com.linkensky.revice.api.model.ServiceItem;
import com.linkensky.revice.api.model.Services;
import com.linkensky.revice.utils.ServiceParcelable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;

    private Marker currentPosMarker;
    private Location currentLocation;
    private static View view;

    private Map<Marker, ServiceItem> closestService = new HashMap<Marker, ServiceItem>();
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapHome);
        mapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);

        if(currentLocation!=null) drawMarker();

    }

    public void updateMarker(Location location){

        if(currentLocation == null) {
            currentLocation = location;
        }
        else
        {
            if(currentLocation.getLongitude() == location.getLongitude() && currentLocation.getLatitude() == location.getLatitude()){
                return;
            }
        }


        currentLocation = location;
        Log.d("Location Update", "Lat: " + String.valueOf(currentLocation.getLatitude()) + "Lng: " + String.valueOf(currentLocation.getLongitude()));


        drawMarker();

    }

    public void drawMarker(){

        if(mMap == null) return;
        //Load Marker
        double dist = sharedPreferences.getFloat(RevicePreferences.OPTION_DISTANCE, 10.0f);
        int max = sharedPreferences.getInt(RevicePreferences.OPTION_MAXITEM, 10);
        ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class);
        Call<ClosestServices> closestServicesCall = reviceApi.closestService(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                dist,
                max
        );
        closestServicesCall.enqueue(new Callback<ClosestServices>() {
            @Override
            public void onResponse(Call<ClosestServices> call, Response<ClosestServices> response) {
                if(response.isSuccess()){
                    mMap.clear();
                    List<ServiceItem> serviceItems = response.body().getData();
                    for (int i=0;i<response.body().getTotal();i++){
                        String lat = serviceItems.get(i).getLat();
                        String lng = serviceItems.get(i).getLng();
                        String nama = serviceItems.get(i).getNama();
                        LatLng pos = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(pos)
                                .title(nama);
                        Marker marker = mMap.addMarker(markerOptions);
                        closestService.put(marker, serviceItems.get(i));
                    }
                }
                else
                {
                    Log.e("Get Closest Service", "Error Fetch Data");
                }
            }

            @Override
            public void onFailure(Call<ClosestServices> call, Throwable t) {
                Log.e("Get Closest Service", "Failed Fetch Data");
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ServiceItem item = closestService.get(marker);
        Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
        ServiceParcelable serviceParcelable = new ServiceParcelable(
          item.getId(),item.getNama(), item.getAlamat(), item.getDeskripsi(), item.getTelp(), item.getTipeName()
        );

        intent.putExtra("service", serviceParcelable);
        startActivity(intent);

    }


}
