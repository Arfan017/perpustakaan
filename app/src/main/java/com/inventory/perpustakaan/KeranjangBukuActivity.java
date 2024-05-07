package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Api.konfig.UrlImage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.inventory.perpustakaan.Api.konfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KeranjangBukuActivity extends AppCompatActivity {
    ImageView IVImage;
    EditText ETNama_buku, ETstatus, ETpenulis, ETpenerbit, ETtglambil, ETtglkembali;
    String nama_buku, status, penulis, penerbit, gambar_buku;
    Button BTNPinjam;
    private String id_user, id_buku;
    SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_USER = "id_user";
    LocalTime jamSaatIni = null;
    LocalTime jamPembanding = null;
    LocalDate tglambil = null;
    LocalDate tglkembali = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keranjang_buku);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString(ID_USER, null);

        id_buku = getIntent().getStringExtra("id_buku");
        nama_buku = getIntent().getStringExtra("namabuku");
        status = getIntent().getStringExtra("stok");
        penulis = getIntent().getStringExtra("penulis");
        penerbit = getIntent().getStringExtra("penerbit");
        gambar_buku = getIntent().getStringExtra("image");

        IVImage = findViewById(R.id.IVImage);
        ETNama_buku = findViewById(R.id.ETNamaBuku);
        ETstatus = findViewById(R.id.ETStatusBuku);
        ETpenulis = findViewById(R.id.ETPenulis);
        ETpenerbit = findViewById(R.id.ETPenerbit);
        ETtglambil = findViewById(R.id.ETTglAmbil);
        ETtglkembali = findViewById(R.id.ETTglkembali);
        BTNPinjam = findViewById(R.id.BTNPinjam);

        Glide.with(this)
                .load(UrlImage+gambar_buku)
                .into(IVImage);

        if (status.isEmpty()){
            ETstatus.setText("Kosong");
        } else {
            ETstatus.setText("Ada");
        }

        ETNama_buku.setText(nama_buku);
        ETpenulis.setText(penulis);
        ETpenerbit.setText(penerbit);

        ETNama_buku.setEnabled(false);
        ETstatus.setEnabled(false);
        ETpenulis.setEnabled(false);
        ETpenerbit.setEnabled(false);
        ETtglambil.setEnabled(false);
        ETtglkembali.setEnabled(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            jamSaatIni = LocalTime.now();
            jamPembanding = LocalTime.of(15, 0, 0);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");

            if (jamSaatIni.isAfter(jamPembanding)){
                tglambil = LocalDate.now().plusDays(1);
                tglkembali = tglambil.plusDays(3);

                ETtglambil.setText(tglambil.format(formatters).toString());
                ETtglkembali.setText(tglkembali.format(formatters).toString());
            } else {
                tglambil = LocalDate.now();
                tglkembali = tglambil.plusDays(3);

                ETtglambil.setText(tglambil.format(formatters).toString());
                ETtglkembali.setText(tglkembali.format(formatters).toString());
            }
        }

        BTNPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinjam(id_user, id_buku, tglambil.toString(), tglkembali.toString() );
            }
        });
    }

    private void pinjam(String id_user, String id_buku, String tgl_pinjam, String tgl_kembali) {
        // Buatkan request untuk mengirim data ke server
        RequestQueue queue = Volley.newRequestQueue(KeranjangBukuActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlPinjamBuku,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {

                                Intent intent = new Intent(KeranjangBukuActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(KeranjangBukuActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Login gagal
                                Toast.makeText(KeranjangBukuActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(KeranjangBukuActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_member", id_user.toString());
                params.put("id_buku", id_buku.toString());
                params.put("tgl_ambil", tgl_pinjam);
                params.put("tgl_kembali", tgl_kembali);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}