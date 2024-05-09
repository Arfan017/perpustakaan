package com.inventory.perpustakaan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inventory.perpustakaan.Adapter.AdapterDashboard;
import com.inventory.perpustakaan.Api.konfig;
import com.inventory.perpustakaan.Model.ModelBuku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailBukuActivity extends AppCompatActivity {
    ImageView IVGambarBuku;
    TextView TVJudulBuku, TVPenerbitBuku1, TVPenerbitBuku2, TVPenulis, TVISBN, TVThnTerbit, TVHalaman, TVStok, TVRakBuku, TVSinopsis, TVRating;
    Button BTNPinjam, BTNUlasan;
    String id_buku, status_member, nama_buku, penulis, penerbit, nisn_isbn, tahun_terbit, halaman_buku, nama_rak, stok, tentang, gambar_buku, rating;
    SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String STATUS_MEMBER = "status_member";
    public static final String ID_USER = "id_user";
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_buku);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString(ID_USER, null);

        IVGambarBuku = findViewById(R.id.IVGambarBuku);
        TVJudulBuku = findViewById(R.id.TVJudulBuku);
        TVPenerbitBuku1 = findViewById(R.id.TVPenerbit1);
        TVPenerbitBuku2 = findViewById(R.id.TVPenerbit2);
        TVPenulis = findViewById(R.id.TVPenulis);
        TVISBN = findViewById(R.id.TVISBN);
        TVThnTerbit = findViewById(R.id.TVThnTerbit);
        TVHalaman = findViewById(R.id.TVHalaman);
        TVRakBuku = findViewById(R.id.TVRak);
        TVStok = findViewById(R.id.TVStok);
        TVRating = findViewById(R.id.TVRating);
        TVSinopsis = findViewById(R.id.TVSinopsis);

        BTNPinjam = findViewById(R.id.BtnPinjam);
        BTNUlasan = findViewById(R.id.BtnUlasan);

        id_buku = getIntent().getStringExtra("id_buku");

        CekStatusMember(id_user);
        GetDetailDataBuku(id_buku);
        BTNPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_member = sharedpreferences.getString(STATUS_MEMBER, null);

                if (Integer.parseInt(status_member) == 0) {
                    PesanAlert();
                } else if (Integer.parseInt(status_member) == 2) {
                    PesanAlert1();
                } else {
                    Intent intent = new Intent(DetailBukuActivity.this, KeranjangBukuActivity.class);
                    intent.putExtra("id_buku", id_buku);
                    intent.putExtra("image", gambar_buku);
                    intent.putExtra("namabuku", nama_buku);
                    intent.putExtra("penulis", penulis);
                    intent.putExtra("penerbit", penerbit);
                    intent.putExtra("stok", stok);
                    startActivity(intent);
                }
            }
        });

        BTNUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailBukuActivity.this, UlasanActivity.class);
                intent.putExtra("id_buku", id_buku);
                startActivity(intent);
                }
        });
    }

    private void PesanAlert() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailBukuActivity.this);

        // Set Alert Title
        builder.setTitle("Peringatan !");

        // Set the message show for the Alert time
        builder.setMessage("Anda belum menjadi member. \nAnda tidak dapat meminjam buku");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Daftar Member", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            Intent intent = new Intent(DetailBukuActivity.this, FormulirActivity.class);
            startActivity(intent);
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("Nanti", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void PesanAlert1() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailBukuActivity.this);

        // Set Alert Title
        builder.setTitle("Peringatan !");

        // Set the message show for the Alert time
        builder.setMessage("Anda sudah mendaftar sebagi member, silahkan tunggu konfirmasi admin");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void GetDetailDataBuku(String id_buku) {
        ProgressDialog asyncDialog = new ProgressDialog(DetailBukuActivity.this);
        asyncDialog.setMessage("Mengambil Data...");
        asyncDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlGetDetailDataBuku,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                nama_buku = jsonObject.getString("nama_buku");
                                penulis = jsonObject.getString("penulis");
                                penerbit = jsonObject.getString("penerbit");
                                nisn_isbn = jsonObject.getString("nisn_isbn");
                                tahun_terbit = jsonObject.getString("tahun_terbit");
                                halaman_buku = jsonObject.getString("halaman_buku");
                                nama_rak = jsonObject.getString("nama_rak");
                                stok = jsonObject.getString("stok");
                                tentang = jsonObject.getString("tentang");
                                gambar_buku = jsonObject.getString("gambar_buku");
                                rating = jsonObject.getString("rating");

//                                IVGambarBuku.set(no_member);
                                TVJudulBuku.setText(nama_buku);
                                TVPenerbitBuku1.setText("By: " + penerbit);
                                TVPenerbitBuku2.setText(": " + penerbit);
                                TVPenulis.setText(": " + penulis);
                                TVISBN.setText(": " + nisn_isbn);
                                TVThnTerbit.setText(": " + tahun_terbit);
                                TVHalaman.setText(": " + halaman_buku);
                                TVRakBuku.setText(": " + nama_rak);
                                TVStok.setText(": " + stok);
                                TVRating.setText(rating);
                                TVSinopsis.setText(": " + tentang);

                                asyncDialog.dismiss();

                            } else {
                                // Login gagal
                                asyncDialog.dismiss();
                                Toast.makeText(DetailBukuActivity.this, "ada kesalahan", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBukuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_buku", id_buku);
                return params;
            }
        };
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void CekStatusMember(String id_user) {
        ProgressDialog asyncDialog = new ProgressDialog(DetailBukuActivity.this);
        asyncDialog.setMessage("Mengambil Data...");
        asyncDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserCekStatusMember,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                String statusmember = jsonObject.getString("statusmember");

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(STATUS_MEMBER, statusmember);
                                editor.commit();

                                asyncDialog.dismiss();

                            } else {
                                // Login gagal
                                asyncDialog.dismiss();
//                                Toast.makeText(DetailBukuActivity.this, "ada kesalahan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        asyncDialog.dismiss();
                        Toast.makeText(DetailBukuActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}