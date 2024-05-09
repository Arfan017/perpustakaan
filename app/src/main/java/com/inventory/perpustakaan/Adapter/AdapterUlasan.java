package com.inventory.perpustakaan.Adapter;

import static com.inventory.perpustakaan.Api.konfig.UrlImageBuku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventory.perpustakaan.Interface.IClickListener;
import com.inventory.perpustakaan.Model.ModelBuku;
import com.inventory.perpustakaan.Model.ModelUlasan;
import com.inventory.perpustakaan.R;

import java.util.ArrayList;

public class AdapterUlasan extends RecyclerView.Adapter<AdapterUlasan.RecyclerViewHolder> {
    private ArrayList<ModelUlasan> ListUlasan;
    private Context mcontext;

    public AdapterUlasan(ArrayList<ModelUlasan> ListUlasan, Context mcontext) {
        this.ListUlasan = ListUlasan;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ulasanbuku, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ModelUlasan DataUlasan = ListUlasan.get(position);
        Glide.with(mcontext).load(UrlImageBuku+DataUlasan.getGambarbuku()).into(holder.IVbuku);

        holder.TVnama.setText(DataUlasan.getNama());
        holder.TVulasan.setText(DataUlasan.getUlasan());
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return ListUlasan.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView IVbuku;
        TextView TVnama, TVulasan;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            IVbuku = itemView.findViewById(R.id.IVBuku);
            TVnama = itemView.findViewById(R.id.TVNama);
            TVulasan = itemView.findViewById(R.id.TVUlasan);
        }
    }
}