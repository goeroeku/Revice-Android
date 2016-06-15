package com.linkensky.revice.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.fragments.HistoryFragment;
import com.linkensky.revice.fragments.OrderFragment;
import com.linkensky.revice.gcm.RegistrationIntentService;
import com.linkensky.revice.fragments.HomeFragment;
import com.linkensky.revice.R;
import com.linkensky.revice.adapters.ViewPagerAdapter;
import com.linkensky.revice.realm.CurrentUserModel;


import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends LocationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";


    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
           actionBar.setDisplayShowTitleEnabled(false);
        }



        Boolean isPlayService = checkPlayServices();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isLogin = sharedPreferences.getBoolean(RevicePreferences.IS_LOGGED_IN, false);

//        Set Up Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Get Current User
        Realm realm = Realm.getInstance(this);
        if(isLogin){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_account).setVisible(true);

            RealmResults<CurrentUserModel> currentUserResult = realm.where(CurrentUserModel.class).findAll();

            if(currentUserResult.size() != 0){
                CurrentUserModel currentUser = currentUserResult.first();
                String role = currentUser.getRoleName();
                if(role.equals("admin") || role.equals("pemilik")){
                    menu.findItem(R.id.nav_jobs).setVisible(true);
                }
            }
        }

//        Set Up Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons(tabLayout);


//        Set Up Play Service For GCM
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

//                boolean sentToken = sharedPreferences
//                        .getBoolean(RevicePreferences.SENT_TOKEN_TO_SERVER, false);

            }
        };

        if (isPlayService && isLogin) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, OptionsActivity.class));
        }

        if (id == R.id.action_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(id == R.id.nav_account){
            startActivity(new Intent(this, UserDetailActivity.class));
        }

        if(id == R.id.nav_jobs){
            startActivity(new Intent(this, JobsActivity.class));
        }

        if(id == R.id.nav_options){
            startActivity(new Intent(this, OptionsActivity.class));
        }

        if(id == R.id.nav_notification){
            startActivity(new Intent(this, NotificationActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RevicePreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private void setupTabIcons(TabLayout tabLayout) {


        TabLayout.Tab tab;

        if((tab = tabLayout.getTabAt(0))!= null){
            tab.setIcon(R.drawable.ic_near_me_white_24dp);
        }
        if((tab = tabLayout.getTabAt(1))!= null){
            tab.setIcon(R.drawable.ic_contacts_white_24dp);
        }
        if((tab = tabLayout.getTabAt(2))!= null){
            tab.setIcon(R.drawable.ic_history_white_24dp);
        }

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(), "Sekitar");
        viewPagerAdapter.addFragment(new OrderFragment(), "Pemesanan");
        viewPagerAdapter.addFragment(new HistoryFragment(), "Riwayat");
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
