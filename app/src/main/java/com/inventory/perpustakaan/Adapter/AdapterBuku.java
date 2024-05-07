package com.inventory.perpustakaan.Adapter;

import static com.inventory.perpustakaan.Api.konfig.UrlImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventory.perpustakaan.Interface.IClickListener;
import com.inventory.perpustakaan.Model.ModelBuku;
import com.inventory.perpustakaan.R;

import java.util.ArrayList;

public class AdapterBuku extends RecyclerView.Adapter<AdapterBuku.RecyclerViewHolder> {
    private final IClickListener iClickListener;
    private ArrayList<ModelBuku> ListBuku;
    private Context mcontext;

    public AdapterBuku(ArrayList<ModelBuku> ListBuku, Context mcontext, IClickListener iClickListener) {
        this.ListBuku = ListBuku;
        this.mcontext = mcontext;
        this.iClickListener = iClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardbuku1, parent, false);
        return new RecyclerViewHolder(view, iClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ModelBuku DataBuku = ListBuku.get(position);
        holder.courseTV.setText(DataBuku.getNama_buku());
        Glide.with(mcontext).load(UrlImage+DataBuku.getGambar_buku()).into(holder.courseIV);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return ListBuku.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTV;
        private ImageView courseIV;

        public RecyclerViewHolder(@NonNull View itemView, IClickListener iClickListener) {
            super(itemView);
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iClickListener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            iClickListener.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}