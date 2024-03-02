package com.js.pocketaccount.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.js.pocketaccount.Models.Accounts;
import com.js.pocketaccount.R;

public class AccountAdapter extends FirebaseRecyclerAdapter<Accounts,AccountAdapter.MyViewHolder> {
    public AccountAdapter(@NonNull FirebaseRecyclerOptions<Accounts> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull AccountAdapter.MyViewHolder holder, int position, @NonNull Accounts model) {

        holder.name.setText(model.getName());
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.narration.setText(model.getNarration());

    }

    @NonNull
    @Override
    public AccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_layout_viewholder,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,amount,narration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTvHolder);
            date = itemView.findViewById(R.id.dateTvHolder);
            amount = itemView.findViewById(R.id.amountTvHolder);
            narration = itemView.findViewById(R.id.narrationTvHolder);
        }
    }
}
