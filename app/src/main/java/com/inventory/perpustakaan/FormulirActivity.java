package com.inventory.perpustakaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.inventory.perpustakaan.Api.konfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormulirActivity extends AppCompatActivity {
    EditText EtNoIdentitas, EtNama, EtJenisKelamin, EtTtgLahir, EtAlamat1,
            EtAlamat2, EtNoTelp, EtPekerjaan, EtInstitusi;
    Button BtnKirim;
    String id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1,
            alamat2, notelp, pekerjaan, institusi;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulir);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID_KEY, null);

        EtNoIdentitas = findViewById(R.id.ETnoidentitas);
        EtNama = findViewById(R.id.ETnama);
        EtJenisKelamin = findViewById(R.id.ETjeniskelamin);
        EtTtgLahir = findViewById(R.id.ETttgllahir);
        EtAlamat1 = findViewById(R.id.ETalamat1);
        EtAlamat2 = findViewById(R.id.ETalamat2);
        EtNoTelp = findViewById(R.id.ETnotelp);
        EtPekerjaan = findViewById(R.id.ETperkerjaan);
        EtInstitusi = findViewById(R.id.ETinstitusi);

        BtnKirim = findViewById(R.id.BTNkirim);

        noidentitas = EtNoIdentitas.getText().toString();
        nama = EtNama.getText().toString();
        jeniskelamin = EtJenisKelamin.getText().toString();
        ttgllahir = EtTtgLahir.getText().toString();
        alamat1 = EtAlamat1.getText().toString();
        alamat2 = EtAlamat2.getText().toString();
        notelp = EtNoTelp.getText().toString();
        pekerjaan = EtPekerjaan.getText().toString();
        institusi = EtInstitusi.getText().toString();

        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Daftar(id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1,
                        alamat2, notelp, pekerjaan, institusi);
            }
        });
    }

    private void Daftar(String id, String noidentitas, String nama, String jeniskelamin, String ttgllahir, String alamat1,
                        String alamat2, String notelp, String pekerjaan, String institusi) {
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserDaftar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
//                                Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(FormulirActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
                                Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Login gagal
                                Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormulirActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data ke server
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("noidentitas", noidentitas);
                params.put("nama", nama);
                params.put("jeniskelamin", jeniskelamin);
                params.put("ttgllahir", ttgllahir);
                params.put("alamat1", alamat1);
                params.put("alamat2", alamat2);
                params.put("notelp", notelp);
                params.put("pekerjaan", pekerjaan);
                params.put("institusi", institusi);

                return params;
            }
        };
        // Menambahkan request ke antrian request Volley
        Volley.newRequestQueue(this).add(request);
    }
}