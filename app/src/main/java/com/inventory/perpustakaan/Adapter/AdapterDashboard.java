package com.inventory.perpustakaan.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import static com.inventory.perpustakaan.Api.konfig.UrlImageBuku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventory.perpustakaan.DashboardActivity;
import com.inventory.perpustakaan.DetailBukuActivity;
import com.inventory.perpustakaan.Interface.IClickListener;
import com.inventory.perpustakaan.LoginActivity;
import com.inventory.perpustakaan.Model.ModelBuku;
import com.inventory.perpustakaan.R;
import com.inventory.perpustakaan.UlasanActivity;

import java.util.ArrayList;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.RecyclerViewHolder> {
    private final IClickListener iClickListener;
    private ArrayList<ModelBuku> ListBuku;
    private Context mcontext;

    public AdapterDashboard(ArrayList<ModelBuku> ListBuku, Context mcontext, IClickListener iClickListener) {
        this.ListBuku = ListBuku;
        this.mcontext = mcontext;
        this.iClickListener = iClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardbuku, parent, false);
        return new RecyclerViewHolder(view, iClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ModelBuku DataBuku = ListBuku.get(position);
        Glide.with(mcontext).load(UrlImageBuku + DataBuku.getGambar_buku()).into(holder.IVbuku);
        holder.TVbuku.setText(DataBuku.getNama_buku());
        holder.TVpenerbit.setText("By: " + DataBuku.getPenerbit());
//        Glide.with(mcontext).load(UrlImage + DataBuku.getGambar_buku()).into(holder.IVrating);
        holder.TVdesk.setText(DataBuku.getTentang());
        holder.RBrating.setRating(Float.valueOf(DataBuku.getRating()));
        holder.id_buku = String.valueOf(DataBuku.getId_buku());
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return ListBuku.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private RatingBar RBrating;
        private TextView TVbuku, TVpenerbit, TVdesk;
        private ImageView IVbuku, IVrating;
        private Button BtnPinjam, BtnUlasan;
        String id_buku;


        public RecyclerViewHolder(@NonNull View itemView, IClickListener iClickListener) {
            super(itemView);
            TVbuku = itemView.findViewById(R.id.TVBUku);
            TVpenerbit = itemView.findViewById(R.id.TVPenerbit);
            TVdesk = itemView.findViewById(R.id.TVDesk);
            IVbuku = itemView.findViewById(R.id.IVBUku);
//            IVrating = itemView.findViewById(R.id.IVRating);
            BtnPinjam = itemView.findViewById(R.id.BTNPinjam);
            BtnUlasan = itemView.findViewById(R.id.BTNUlasan);
            RBrating = itemView.findViewById(R.id.RBRating);

            BtnPinjam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iClickListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            iClickListener.OnItemClick(pos);

                            Intent intent = new Intent(view.getContext(), DetailBukuActivity.class);
                            intent.putExtra("id_buku", id_buku);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });

            BtnUlasan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iClickListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            iClickListener.OnItemClick(pos);
                            Intent intent = new Intent(view.getContext(), UlasanActivity.class);
                            intent.putExtra("id_buku", id_buku);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}