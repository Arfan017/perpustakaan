package com.inventory.perpustakaan.Adapter;

import static com.inventory.perpustakaan.Api.konfig.UrlImageBuku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventory.perpustakaan.Model.ModelBuku;
import com.inventory.perpustakaan.Model.ModelPinjamBuku;
import com.inventory.perpustakaan.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdapterPinjamBuku extends RecyclerView.Adapter<AdapterPinjamBuku.RecyclerViewHolder> {
    private ArrayList<ModelPinjamBuku> ListPinjamBuku;
    private Context mcontext;

    public AdapterPinjamBuku(ArrayList<ModelPinjamBuku> ListPinjamBuku, Context mcontext) {
        this.ListPinjamBuku = ListPinjamBuku;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listpinjambuku, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ModelPinjamBuku DataPinjamBuku = ListPinjamBuku.get(position);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");

            holder.TVtglpinjam.setText("Tgl Pinjam: " + LocalDate.parse(DataPinjamBuku.getTgl_pinjam()).format(formatters).toString());
            holder.TVtglkembali.setText("Tgl Pinjam: " + LocalDate.parse(DataPinjamBuku.getTgl_kembali()).format(formatters).toString());
        }
        Glide.with(mcontext).load(UrlImageBuku + DataPinjamBuku.getGambar_buku()).into(holder.IVbuku);
        holder.TVbuku.setText(DataPinjamBuku.getNama_buku());
        holder.TVdenda.setText(DataPinjamBuku.getDenda());
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return ListPinjamBuku.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView TVbuku, TVtglpinjam, TVtglkembali, TVdenda;
        private ImageView IVbuku;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            TVbuku = itemView.findViewById(R.id.TVBUku);
            TVtglpinjam = itemView.findViewById(R.id.TVTglPinjam);
            TVtglkembali = itemView.findViewById(R.id.TVTglKembali);
            IVbuku = itemView.findViewById(R.id.IVBUku);
            TVdenda = itemView.findViewById(R.id.TVDenda);

        }
    }
}