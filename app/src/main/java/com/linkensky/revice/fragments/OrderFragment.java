package com.linkensky.revice.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.api.ReviceApi;
import com.linkensky.revice.api.ServiceGenerator;
import com.linkensky.revice.api.model.Order;
import com.linkensky.revice.api.model.OrderItem;
import com.linkensky.revice.api.model.OrderRequest;
import com.linkensky.revice.realm.OrderModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View view;
    private View mOrderForm;
    private View mOrderProgress;
    private Location currentLocation;


    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isLogin = sharedPreferences.getBoolean(RevicePreferences.IS_LOGGED_IN,false);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            if (isLogin){
                view = inflater.inflate(R.layout.fragment_order, container, false);
            }else
            {
                view = inflater.inflate(R.layout.not_login, container, false);
            }
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        if (!isLogin) return view;

        mOrderProgress = view.findViewById(R.id.order_progress);
        mOrderForm = view.findViewById(R.id.order_form);

        final TextView detailLokasi = (TextView) view.findViewById(R.id.textDetailLokasi);
        final TextView descMasalah = (TextView) view.findViewById(R.id.textDescMasalah);
        final RadioGroup typeMasalah = (RadioGroup) view.findViewById(R.id.radioTipe);
        Button button = (Button) view.findViewById(R.id.buttonPesan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation == null){
                    ShowDialog("Gagal!", "Lokasi tidak tersedia, Pastikan GPS anda aktif. Error: EOR01");
                    return;
                }

                showProgress(true);
                ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class,
                        sharedPreferences.getString(RevicePreferences.AUTH_TOKEN, ""), getContext());

                double dist = Double.parseDouble(sharedPreferences.getString(RevicePreferences.OPTION_DISTANCE, "10.0"));
                int max = Integer.parseInt(sharedPreferences.getString(RevicePreferences.OPTION_MAXITEM, "10"));

                //Get type
                int selectedId = typeMasalah.getCheckedRadioButtonId();
                String type = "Motor";

                if(selectedId == R.id.radioMotor){
                    type = "Motor";
                }else{
                    type = "Mobil";
                }
                Call<Order> orderCall = reviceApi.createOrder(
                        new OrderRequest(type, descMasalah.getText().toString(),
                                detailLokasi.getText().toString(),
                                String.valueOf(currentLocation.getLatitude()),
                                String.valueOf(currentLocation.getLongitude()),
                                dist,
                                max
                        )
                );

                orderCall.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        showProgress(false);
                        if(response.isSuccess()){

                            //Simpan ke realm
                            OrderItem orderItem = response.body().getOrder();
                            Realm realm = Realm.getInstance(getContext());
                            realm.beginTransaction();
                            OrderModel orderModel = realm.createObject(OrderModel.class);
                            orderModel.setId(orderItem.getId());
                            orderModel.setCreatedAt(orderItem.getCreatedAt());
                            orderModel.setDesc(orderItem.getProblemDesc());
                            orderModel.setLat(orderItem.getLat());
                            orderModel.setLng(orderItem.getLng());
                            orderModel.setStatus(Integer.parseInt(orderItem.getStatus()));
                            orderModel.setType(orderItem.getProblemType());
                            orderModel.setUpdatedAt(orderItem.getUpdatedAt());
                            orderModel.setUserId(orderItem.getUserId());
                            orderModel.setLocation(orderItem.getLocationDesc());
                            realm.commitTransaction();

                           ShowDialog("Berhasil!", "Pemesanan Berhasil Dilakukan");
                        }else{
                            Log.e("Pemesanan", "Pemesana Gagal");
                            ShowDialog("Gagal!", "Gagal Membuat Pesanan. Error: EOR02");
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.e("Pemesanan", "Pemesana Failed");
                        showProgress(false);
                        ShowDialog("Gagal!", "Gagal Membuat Pesanan. Error: EOR03");

                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void ShowDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
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

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mOrderForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mOrderForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOrderForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mOrderProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mOrderProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOrderProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mOrderProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mOrderForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
