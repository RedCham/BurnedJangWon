package com.example.burnedjangwon;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.support.v7.widget.RecycleView;
//import com.google.firebase.database.core.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private List<Point> mDataset;
    private String id;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_nickname;
        public TextView TextView_msg;
        public TextView TextView_order;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            TextView_nickname = v.findViewById(R.id.IDText);
            TextView_msg = v.findViewById(R.id.pointText);
            TextView_order = v.findViewById(R.id.orderText);
            rootView = v;

        }

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(List<Point> myDataset, Context context, String id) {
        //{"1","2"}
        mDataset = myDataset;
        this.id = id;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Point chat = mDataset.get(position);

        holder.TextView_nickname.setText(chat.getID());
        holder.TextView_msg.setText(chat.getPoint());
        holder.TextView_order.setText(Integer.toString(position+1));
        holder.TextView_order.setTextColor(Color.rgb(200,200,200));
        holder.TextView_msg.setTextColor(Color.rgb(200,200,200));

        if(chat.getID().equals(this.id)) {
            //holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            //holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.TextView_nickname.setTextColor(Color.rgb(200,0,0));
        }
        else {
            //holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            //holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.TextView_nickname.setTextColor(Color.rgb(200,200,200));

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자
        return mDataset == null ? 0 :  mDataset.size();
    }

    public Point getPoint(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addPoint(Point p) {
        mDataset.add(p);
        notifyItemInserted(mDataset.size()-1); //갱신
    }

}
