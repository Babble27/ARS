package com.example.bernh.androidruderscan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Data> mDataset;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            // each data item is just a string in this case
            public TextView title, year, genre;
            public MyViewHolder(View v) {
                super(v);
                v.setOnLongClickListener(this);
                title = (TextView) v.findViewById(R.id.title);
            }

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder delControl = new AlertDialog.Builder(context);
                delControl.setMessage("delete "+mDataset.get(getAdapterPosition()).getTitle()+ " ?").setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(getAdapterPosition());
                    }
                });
                AlertDialog alert = delControl.create();
                alert.show();

                return true;
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Data> myDataset, Context context) {
            this.mDataset = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.data_view, parent, false);
        //...
            return new MyViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Data data = mDataset.get(position);
            holder.title.setText(data.getTitle());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void removeItem(int position){
            mDataset.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDataset.size());
        }

        public void addItem(Data data){
            mDataset.add(data);
            notifyItemRangeChanged(mDataset.size()-1,mDataset.size());
        }


    }

