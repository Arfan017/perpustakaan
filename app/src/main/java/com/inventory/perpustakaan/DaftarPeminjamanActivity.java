package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.ID_USER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.SHARED_PREFS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inventory.perpustakaan.Adapter.AdapterPinjamBuku;
import com.inventory.perpustakaan.Api.konfig;
import com.inventory.perpustakaan.Model.ModelPinjamBuku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaftarPeminjamanActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ModelPinjamBuku> modelPinjamBukuArrayList;
    SharedPreferences sharedpreferences;
    String id_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_peminjaman);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_member = sharedpreferences.getString(ID_USER, null);

        recyclerView = findViewById(R.id.RVBuku);
        modelPinjamBukuArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetDataPinjamBuku(id_member);

    }

    private void GetDataPinjamBuku(String id_member) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        asyncDialog.setMessage("Mengambil Data Buku...");
        asyncDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlGetDataPinjamBuku,
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
                                modelPinjamBukuArrayList.add(new ModelPinjamBuku(
                                        buku.getString("id"),
                                        buku.getString("nama_buku"),
                                        buku.getString("gambar_buku"),
                                        buku.getString("tgl_pinjam"),
                                        buku.getString("tgl_kembali"),
                                        buku.getString("denda")
                                ));
                            }
                            //creating adapter object and setting it to recyclerview
                            AdapterPinjamBuku adapter = new AdapterPinjamBuku(modelPinjamBukuArrayList, DaftarPeminjamanActivity.this);
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
                        Toast.makeText(DaftarPeminjamanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_member", id_member);
                params.put("status", "belum kembali");
                return params;
            }
        };;

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}