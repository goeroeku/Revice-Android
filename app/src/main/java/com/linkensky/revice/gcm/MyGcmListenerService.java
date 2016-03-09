/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkensky.revice.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.linkensky.revice.R;
import com.linkensky.revice.RevicePreferences;
import com.linkensky.revice.activity.MainActivity;
import com.linkensky.revice.realm.CurrentUserModel;
import com.linkensky.revice.realm.JobModel;
import com.linkensky.revice.realm.NotificationModel;
import com.linkensky.revice.realm.OrderModel;

import io.realm.Realm;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private Realm realm;
    private SharedPreferences sharedPreferences;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("GCM Listener", "Incoming GCM");
        if(!sharedPreferences.getBoolean(RevicePreferences.IS_LOGGED_IN, false)){
            Log.e("GCM Listener", "Not Logged In");
            return;
        }
        realm = Realm.getInstance(this);
        CurrentUserModel currentUser = realm.where(CurrentUserModel.class).findFirst();
        String role = currentUser.getRoleName();
        Integer type = Integer.parseInt(data.getString("type", "0"));
        Log.d("GCM Listener", "Type :" + type);
        switch (type){
            case RevicePreferences.TYPE_NEWORDER:
                Log.d("GCM Listener", "New Order");
                Log.d("GCM Listener", "Role : " + role);

                if(!(role.equals("admin") || role.equals("pemilik"))){

                    Log.e("GCM Listener", "Not Privillege");

                    return;
                }
                //Update Job Database
                realm.beginTransaction();

                //Create Job Model
                JobModel job = realm.createObject(JobModel.class);
                job.setId(data.getString("id"));
                job.setIdPemesan(data.getString("user_id"));
                job.setNamaPemesan(data.getString("user_name"));
                job.setStatus(Integer.parseInt(data.getString("status", "0")));
                job.setWaktu(data.getString("datetime"));
                job.setProblemType(data.getString("problem_type"));

                //Create Notification model
                NotificationModel notificationModel = realm.createObject(NotificationModel.class);
                notificationModel.setDatetime(data.getString("datetime"));
                notificationModel.setDesc("Pesanan Baru");
                notificationModel.setTitle(data.getString("user_name"));
                notificationModel.setType(Integer.parseInt(data.getString("type", "0")));

                realm.commitTransaction();
                sendNotification("Job baru diterima");
                break;
            case RevicePreferences.TYPE_UPDATEORDER:
                Log.d("GCM Listener", "New Update");

                JobModel jobModel = realm.where(JobModel.class).equalTo("id", data.getString("id")).findFirst();
                OrderModel orderModel = realm.where(OrderModel.class).equalTo("id", data.getString("id")).findFirst();
                Log.d("GCM Listener", "Order ID : " + data.getString("id"));
                realm.beginTransaction();
                if(role.equals("admin") || role.equals("pemilik")){
                    if (jobModel != null){
                        jobModel.setStatus(Integer.parseInt(data.getString("status", "0")));
                    }
                }

                if(orderModel != null){
                    Log.d("GCM Listener", "Order Model Is not NULL");

                    orderModel.setStatus(Integer.parseInt(data.getString("status", "0")));
                    orderModel.setServiceId(data.getString("service_id"));
                    orderModel.setServiceName(data.getString("service_name"));

                    if(!data.getString("status", "0").equals("2")) {
                        NotificationModel notification = realm.createObject(NotificationModel.class);
                        notification.setDatetime(data.getString("datetime"));
                        notification.setDesc("Pesanan Diproses");
                        notification.setTitle(data.getString("service_name"));
                        notification.setType(type);
                        sendNotification("Pemesanan Di Proses");
                    }

                }else{
                    Log.d("GCM Listener", "Order Model Is NULL");

                }

                realm.commitTransaction();
                break;
            default:
                Log.e("GCM Listener", "Not Handled GCM");

        }


    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("REVICE")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
