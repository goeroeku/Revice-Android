package com.linkensky.revice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkensky.revice.R;
import com.linkensky.revice.realm.OrderModel;

import io.realm.RealmResults;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>{

    private Context context;
    private RealmResults<OrderModel> realmResults;

    public HistoryListAdapter(Context context, RealmResults<OrderModel> realmResults) {
        this.context = context;
        this.realmResults = realmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        ViewHolder vh = new ViewHolder(itemView, this.context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderModel orderModel = realmResults.get(position);
        Integer status = orderModel.getStatus();
        if(status == 0) {
            holder.serviceName.setText("Menunggu...");
        }else if(status == 2){
            holder.serviceName.setText("Dibatalkan...");

        }else{
            holder.serviceName.setText(orderModel.getServiceName());
        }

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
        holder.date.setText(orderModel.getCreatedAt());
//        holder.orderId = orderModel.getId();
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView serviceName;
        public final TextView status;
        public final TextView date;
        public View container;

//        public String orderId;
        private Context context;

        public ViewHolder(final View container, final Context context) {
            super(container);
            this.context = context;
            this.container = container;
            this.serviceName = (TextView) container.findViewById(R.id.tvOrderName);
            this.status = (TextView) container.findViewById(R.id.tvOrderStatus);
            this.date = (TextView) container.findViewById(R.id.tvDate);

            //Set Click Listener
//            container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, OrderDetailActivity.class);
//                    intent.putExtra("orderId", orderId);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

}
