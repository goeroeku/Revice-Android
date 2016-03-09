package com.linkensky.revice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.realm.JobModel;
import com.linkensky.revice.realm.NotificationModel;

import io.realm.RealmResults;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder>{

    private Context context;
    private RealmResults<NotificationModel> realmResults;

    public NotificationListAdapter(Context context, RealmResults<NotificationModel> realmResults) {
        this.context = context;
        this.realmResults = realmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        ViewHolder vh = new ViewHolder(itemView, this.context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationModel notificationModel = realmResults.get(position);
        holder.title.setText(notificationModel.getTitle());
        holder.desc.setText(notificationModel.getDesc());
        holder.date.setText(notificationModel.getDatetime());

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView title;
        public final TextView desc;
        public final TextView date;

        public View container;

        private Context context;

        public ViewHolder(final View container, final Context context) {
            super(container);
            this.context = context;
            this.container = container;
            this.title = (TextView) container.findViewById(R.id.tvNotifTitle);
            this.desc = (TextView) container.findViewById(R.id.tvNotifDesc);
            this.date = (TextView) container.findViewById(R.id.tvDate);

        }
    }

}
