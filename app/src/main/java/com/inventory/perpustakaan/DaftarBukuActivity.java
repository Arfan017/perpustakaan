package com.inventory.perpustakaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inventory.perpustakaan.Adapter.AdapterBuku;
import com.inventory.perpustakaan.Api.konfig;
import com.inventory.perpustakaan.Interface.IClickListener;
import com.inventory.perpustakaan.Model.ModelBuku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaftarBukuActivity extends AppCompatActivity implements IClickListener {
    private RecyclerView recyclerView;
    private ArrayList<ModelBuku> modelBukuArrayList;
    Button BTNkategori0, BTNkategori1, BTNkategori2, BTNkategori3, BTNkategori4, BTNkategori5,
            BTNkategori6, BTNkategori7, BTNkategori8, BTNkategori9,  BTNkategori10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_buku);
        recyclerView = findViewById(R.id.idCourseRV);
        modelBukuArrayList = new ArrayList<>();

        BTNkategori0 = findViewById(R.id.BTNkategori0);
        BTNkategori1 = findViewById(R.id.BTNkategori1);
        BTNkategori2 = findViewById(R.id.BTNkategori2);
        BTNkategori3 = findViewById(R.id.BTNkategori3);
        BTNkategori4 = findViewById(R.id.BTNkategori4);
        BTNkategori5 = findViewById(R.id.BTNkategori5);
        BTNkategori6 = findViewById(R.id.BTNkategori6);
        BTNkategori7 = findViewById(R.id.BTNkategori7);
        BTNkategori8 = findViewById(R.id.BTNkategori8);
        BTNkategori9 = findViewById(R.id.BTNkategori9);
        BTNkategori10 = findViewById(R.id.BTNkategori10);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.buku);

        GetDataBuku("0");

        View.OnClickListener kategoriOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelBukuArrayList.clear();
                String kategori = (String) view.getTag();
                GetDataBuku(kategori);
            }
        };

        BTNkategori0.setOnClickListener(kategoriOnClickListener);
        BTNkategori0.setTag("0");

        BTNkategori1.setOnClickListener(kategoriOnClickListener);
        BTNkategori1.setTag("1");

        BTNkategori2.setOnClickListener(kategoriOnClickListener);
        BTNkategori2.setTag("2");

        BTNkategori3.setOnClickListener(kategoriOnClickListener);
        BTNkategori3.setTag("3");

        BTNkategori4.setOnClickListener(kategoriOnClickListener);
        BTNkategori4.setTag("4");

        BTNkategori5.setOnClickListener(kategoriOnClickListener);
        BTNkategori5.setTag("5");

        BTNkategori6.setOnClickListener(kategoriOnClickListener);
        BTNkategori6.setTag("6");

        BTNkategori7.setOnClickListener(kategoriOnClickListener);
        BTNkategori7.setTag("7");

        BTNkategori8.setOnClickListener(kategoriOnClickListener);
        BTNkategori8.setTag("8");

        BTNkategori9.setOnClickListener(kategoriOnClickListener);
        BTNkategori9.setTag("9");

        BTNkategori10.setOnClickListener(kategoriOnClickListener);
        BTNkategori10.setTag("10");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.buku) {
                return true;
            } else if (id == R.id.riwayat) {
                startActivity(new Intent(getApplicationContext(), DaftarRiwayatPeminjamanActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void GetDataBuku(String id_rak) {
        ProgressDialog asyncDialog = new ProgressDialog(DaftarBukuActivity.this);
        asyncDialog.setMessage("Mengambil Data Buku...");
        asyncDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlGetDataBukuByRak,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject buku = array.getJSONObject(i);

                                //adding the product to product list
                                modelBukuArrayList.add(new ModelBuku(
                                        buku.getString("kd_buku"),
                                        buku.getString("nama_buku"),
                                        buku.getString("penulis"),
                                        buku.getString("penerbit"),
                                        buku.getString("nisn_isbn"),
                                        buku.getString("tahun_terbit"),
                                        buku.getString("halaman_buku"),
                                        buku.getString("id_rak"),
                                        buku.getString("stok"),
                                        buku.getString("tentang"),
                                        buku.getString("gambar_buku"),
                                        buku.getString("id_rating")
                                ));
                            }
                            //creating adapter object and setting it to recyclerview
                            AdapterBuku adapter = new AdapterBuku(modelBukuArrayList, DaftarBukuActivity.this, DaftarBukuActivity.this);
                            recyclerView.setAdapter(adapter);
                            asyncDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DaftarBukuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_rak", id_rak);
                return params;
            }
        };
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(DaftarBukuActivity.this, DetailBukuActivity.class);
        intent.putExtra("id_buku", modelBukuArrayList.get(position).getId_buku()).toString();
        startActivity(intent);
    }
}