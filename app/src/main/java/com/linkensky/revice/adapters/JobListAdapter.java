package com.linkensky.revice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.realm.JobModel;

import io.realm.RealmResults;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder>{

    private Context context;
    private RealmResults<JobModel> realmResults;

    public JobListAdapter(Context context, RealmResults<JobModel> realmResults) {
        this.context = context;
        this.realmResults = realmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jobs, parent, false);
        ViewHolder vh = new ViewHolder(itemView, this.context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JobModel jobModel = realmResults.get(position);
        Integer status = jobModel.getStatus();
        holder.name.setText(jobModel.getNamaPemesan());
        holder.type.setText(jobModel.getProblemType());
        String statusText = "";
        switch (status){
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


        holder.status.setText("Status: " + statusText);
        holder.date.setText(jobModel.getWaktu());
//        holder.orderId = orderModel.getId();
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView status;
        public final TextView type;
        public final TextView date;

        public View container;

        private Context context;

        public ViewHolder(final View container, final Context context) {
            super(container);
            this.context = context;
            this.container = container;
            this.name = (TextView) container.findViewById(R.id.tvJobsName);
            this.status = (TextView) container.findViewById(R.id.tvJobsStatus);
            this.type = (TextView) container.findViewById(R.id.tvJobsType);
            this.date = (TextView) container.findViewById(R.id.tvDate);

        }
    }

}
