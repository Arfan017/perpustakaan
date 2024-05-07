package com.inventory.perpustakaan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inventory.perpustakaan.Api.konfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton FABAdd, FABLogout, FABEdit, FABSimpan;
    TextView TVlogout, TVedit, TVsimpan, TVnomember, TVnama;
    Boolean isAllFabsVisible;
    EditText ETnama, ETjenkel, ETnohp, ETpekerjaan, ETemail, ETttllahir, ETalamat,
            EditNoIdentitas, EditNama, EditJenkel, EditTtl, EditEmail, EditAlamatIdentitas,
            EditAlamatSekarang, EditNohp, EditPekerjaan, EditNamaInstitusi, EditAlamatInstitusi;
    ScrollView SVedit;
    Button BtnSimpan, BtnBatal;
    SharedPreferences sharedpreferences;
    String id_user;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_USER = "id_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString(ID_USER, null);

        // inisialisasi EditText
        ETnama = findViewById(R.id.ETNama);
        ETnohp = findViewById(R.id.ETNohp);
        ETemail = findViewById(R.id.ETEmail);
        ETalamat = findViewById(R.id.ETAlamat);
        ETttllahir = findViewById(R.id.ETTtl);

        EditNoIdentitas = findViewById(R.id.EditNoIdentitas);
        EditNama = findViewById(R.id.EditNama);
        EditJenkel = findViewById(R.id.EditJenkel);
        EditTtl = findViewById(R.id.EditTtl);
        EditEmail = findViewById(R.id.EditEmail);
        EditAlamatIdentitas = findViewById(R.id.EditAlamatIdentitas);
        EditAlamatSekarang = findViewById(R.id.EditAlamatSekarang);
        EditNohp = findViewById(R.id.EditNohp);
        EditPekerjaan = findViewById(R.id.EditPekerjaan);
        EditNamaInstitusi = findViewById(R.id.EditNamaInstitusi);
        EditAlamatInstitusi = findViewById(R.id.EditAlamatInstitusi);

        // inisialisasi ScollView
        SVedit = findViewById(R.id.SVEdit);
        SVedit.setVisibility(View.GONE);

        // non aktifkan EditText
        ETnama.setEnabled(false);
        ETnohp.setEnabled(false);
        ETemail.setEnabled(false);
        ETalamat.setEnabled(false);
        ETttllahir.setEnabled(false);

        // inisialisasi Button
        FABAdd = findViewById(R.id.add_fab);
        FABLogout = findViewById(R.id.logout_fab);
        FABEdit = findViewById(R.id.edit_fab);
        BtnSimpan = findViewById(R.id.BTNSimpan);
        BtnBatal = findViewById(R.id.BTNBatal);

        // inisialisasi TextView
        TVnama = findViewById(R.id.TVNama);
        TVnomember = findViewById(R.id.TVNomember);
        TVlogout = findViewById(R.id.TVLogout);
        TVedit = findViewById(R.id.TVEdit);

        // inisalisasi Visibility (hilangkan FAB)
        FABLogout.setVisibility(View.GONE);
        FABEdit.setVisibility(View.GONE);
        TVlogout.setVisibility(View.GONE);
        TVedit.setVisibility(View.GONE);

        isAllFabsVisible = false;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        SelectProfile(id_user);

        // fungsi button
        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String NoIdentitas = EditNoIdentitas.getText().toString();
                String Nama = EditNama.getText().toString();
                String Jenkel = EditJenkel.getText().toString();
                String Ttl = EditTtl.getText().toString();
                String Email = EditEmail.getText().toString();
                String AlamatIdentitas = EditAlamatIdentitas.getText().toString();
                String AlamatSekarang = EditAlamatSekarang.getText().toString();
                String Nohp = EditNohp.getText().toString();
                String Pekerjaan = EditPekerjaan.getText().toString();
                String NamaInstitusi = EditNamaInstitusi.getText().toString();
                String AlamatInstitusi = EditAlamatInstitusi.getText().toString();

                EditProfile(NoIdentitas,
                        Nama,
                        Jenkel,
                        Ttl,
                        Email,
                        AlamatIdentitas,
                        AlamatSekarang,
                        Nohp,
                        Pekerjaan,
                        NamaInstitusi,
                        AlamatInstitusi);
            }
        });

        BtnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SVedit.setVisibility(View.GONE);
            }
        });

        FABAdd.setOnClickListener(view -> {
            if (!isAllFabsVisible) {

                FABLogout.show();
                FABEdit.show();

                TVlogout.setVisibility(View.VISIBLE);
                TVedit.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;
            } else {
                FABLogout.hide();
                FABEdit.hide();

                TVlogout.setVisibility(View.GONE);
                TVedit.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }
        });
        FABEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SVedit.setVisibility(View.VISIBLE);
                Toast.makeText(ProfileActivity.this, "Edit klik!", Toast.LENGTH_SHORT
                ).show();
            }
        });
        FABLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Person Added", Toast.LENGTH_SHORT
                ).show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.buku) {
                startActivity(new Intent(getApplicationContext(), DaftarBukuActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.riwayat) {
                startActivity(new Intent(getApplicationContext(), DaftarRiwayatPeminjamanActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (id == R.id.profile) {
                return true;
            }
            return false;
        });
    }

    private void SelectProfile(String id_user) {
        ProgressDialog asyncDialog = new ProgressDialog(ProfileActivity.this);
        asyncDialog.setMessage("Mengambil Data...");
        asyncDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                String no_member = jsonObject.getString("no_member");
                                String nama = jsonObject.getString("nama");
                                String jenkel = jsonObject.getString("jenkel");
                                String no_hp = jsonObject.getString("no_hp");
                                String pekerjaan = jsonObject.getString("pekerjaan");
                                String email = jsonObject.getString("email");
                                String ttgl_lahir = jsonObject.getString("ttgl_lahir");
                                String alamat = jsonObject.getString("alamat");

                                TVnama.setText(nama);
                                TVnomember.setText(no_member);
                                ETnama.setText(nama);
                                ETnohp.setText(no_hp);
                                ETemail.setText(email);
                                ETttllahir.setText(ttgl_lahir);
                                ETalamat.setText(alamat);

                                SVedit.setVisibility(View.GONE);
                                asyncDialog.dismiss();

                            } else {
                                // Login gagal
                                asyncDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "ada kesalahan", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
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

    private void EditProfile(String NoIdentitas, String Nama, String Jenkel, String Ttl, String Email, String AlamatIdentitas, String AlamatSekarang, String Nohp, String Pekerjaan, String NamaInstitusi, String AlamatInstitusi
    ) {
        // Buatkan request untuk mengirim data ke server
        ProgressDialog asyncDialog = new ProgressDialog(ProfileActivity.this);
        asyncDialog.setMessage("Nengirim Data...");
        asyncDialog.show();

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserEditProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                asyncDialog.dismiss();
                                SelectProfile(id_user);

                            } else {
                                // Login gagal
                                asyncDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "ada kesalahan", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("NoIdentitas", NoIdentitas.toString());
                params.put("Nama", Nama.toString());
                params.put("Jenkel", Jenkel.toString());
                params.put("Ttl", Ttl.toString());
                params.put("Email", Email.toString());
                params.put("AlamatIdentitas", AlamatIdentitas.toString());
                params.put("AlamatSekarang", AlamatSekarang.toString());
                params.put("Nohp", Nohp.toString());
                params.put("Pekerjaan", Pekerjaan.toString());
                params.put("NamaInstitusi", NamaInstitusi.toString());
                params.put("AlamatInstitusi", AlamatInstitusi.toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}