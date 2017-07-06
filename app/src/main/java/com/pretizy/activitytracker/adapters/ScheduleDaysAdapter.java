package com.pretizy.activitytracker.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pretizy.activitytracker.QrActivity;
import com.pretizy.activitytracker.R;
import com.pretizy.activitytracker.Util;
import com.pretizy.activitytracker.model.Schedule;
import com.pretizy.activitytracker.ui.EventActivity;

import java.util.List;
import java.util.Random;

/**
 * Created by gerald on 10/8/16.
 */
public class ScheduleDaysAdapter extends RecyclerView.Adapter<ScheduleDaysAdapter.ViewHolder>{
    private List<Schedule> mDataset;
    private static int [] COLOR_PALLETES =  new int[]{
            Color.parseColor("#28ABE3"),
            Color.parseColor("#1FDA9A"),
            Color.parseColor("#F7EAC8"),
            Color.parseColor("#FDF200"),
            Color.parseColor("#FA6900"),
            Color.parseColor("#F38630"),
            Color.parseColor("#69D2E7")
    };
    private Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View v;
        public TextView title;
        public TextView time;
        public CardView cardView;
        public ViewHolder(View v) {
            super(v);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView title = (TextView)v.findViewById(R.id.title);
            CardView cardView= (CardView)v.findViewById(R.id.card_view);
            this.cardView = cardView;
            this.time = time;
            this.title = title;
            this.v = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleDaysAdapter(List<Schedule> myDataset, Activity activity) {
        mDataset = myDataset;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleDaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        long millis = mDataset.get(position).getTime().getTime();
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        hour+=1;
        holder.time.setText(hour+":"+minute+" "+mDataset.get(position).getAm_pm());
        String title= mDataset.get(position).getTitle();
        if(title.length()>15){
            holder.title.setText(title.substring(0, 15)+"...");
        }else {
            holder.title.setText(title);
        }

        holder.cardView.setBackgroundColor(COLOR_PALLETES[new Random().nextInt(6)]);
        final Activity context =activity;
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("title", mDataset.get(position).getTitle());
                intent.putExtra("id", mDataset.get(position).getId());
                intent.putExtra("description", mDataset.get(position).getData());
                intent.putExtra("time", mDataset.get(position).getTime()+" ");
                context.startActivityForResult(intent, 900);
            }
        });
        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, QrActivity.class);
                intent.putExtra("qr-text", Util.ScheduleToCode(mDataset.get(position)));
                context.startActivityForResult(intent, 900);
                return false;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
