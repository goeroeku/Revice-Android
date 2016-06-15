package com.linkensky.revice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.linkensky.revice.R;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.api.ReviceApi;
import com.linkensky.revice.api.ServiceGenerator;
import com.linkensky.revice.api.model.Auth;
import com.linkensky.revice.api.model.Credentials;
import com.linkensky.revice.api.model.ServiceItem;
import com.linkensky.revice.api.model.User;
import com.linkensky.revice.api.model.UserItem;
import com.linkensky.revice.realm.CurrentUserModel;
import com.linkensky.revice.realm.ServiceModel;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        TextView mRegisterText = (TextView) findViewById(R.id.tvRegister);
        mRegisterText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            ReviceApi reviceApi = ServiceGenerator.createService(ReviceApi.class, this);
            Call<Auth> authCall = reviceApi.authUser(new Credentials(email, password));

            authCall.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    if (response.isSuccess()){
                        //Set shared pref and get current user info
                        sharedPreferences.edit().putBoolean(RevicePreferences.IS_LOGGED_IN, true).apply();
                        sharedPreferences.edit().putString(RevicePreferences.AUTH_TOKEN, response.body().getToken()).apply();
                        ReviceApi reviceApiAuth = ServiceGenerator.createService(ReviceApi.class, response.body().getToken(), LoginActivity.this);
                        Call<User> userCall = reviceApiAuth.getCurrentUser();
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccess()){
                                    //Save Current User Info
                                    UserItem userItem = response.body().getUser();
                                    Realm realm = Realm.getInstance(LoginActivity.this);

                                    //Clear userdata
                                    RealmResults<CurrentUserModel> results = realm.where(CurrentUserModel.class).findAll();
                                    RealmResults<ServiceModel> serviceRes = realm.where(ServiceModel.class).findAll();

                                    realm.beginTransaction();
                                    results.clear();
                                    serviceRes.clear();
                                    CurrentUserModel currentUser = realm.createObject(CurrentUserModel.class);
                                    currentUser.setAlamat(userItem.getAlamat());
                                    currentUser.setEmail(userItem.getEmail());
                                    currentUser.setId(userItem.getId());
                                    currentUser.setNama(userItem.getNama());
                                    currentUser.setRoleName(userItem.getRoleName());
                                    currentUser.setTelp(userItem.getTelp());
                                    currentUser.setVerified(userItem.getVerified());

                                    List<ServiceItem> serviceItems = userItem.getManagedService();
                                    for (int i = 0; i<serviceItems.size(); i++){
                                        ServiceModel serviceModel = realm.createObject(ServiceModel.class);
                                        serviceModel.setId(serviceItems.get(i).getId());
                                        serviceModel.setNama(serviceItems.get(i).getNama());
                                        serviceModel.setTipe(serviceItems.get(i).getTipeName());
                                        currentUser.getService().add(serviceModel);

                                    }
                                    realm.commitTransaction();


                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    showProgress(false);
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                    sharedPreferences.edit().putBoolean(RevicePreferences.IS_LOGGED_IN, false).apply();
                                    sharedPreferences.edit().putString(RevicePreferences.AUTH_TOKEN, "").apply();

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                showProgress(false);
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                                sharedPreferences.edit().putBoolean(RevicePreferences.IS_LOGGED_IN, false).apply();
                                sharedPreferences.edit().putString(RevicePreferences.AUTH_TOKEN, "").apply();
                            }
                        });



                    }else{
                        showProgress(false);
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<Auth> call, Throwable t) {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();

                }
            });

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

