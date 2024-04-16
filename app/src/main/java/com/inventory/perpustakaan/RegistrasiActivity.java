package com.inventory.perpustakaan;

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

public class RegistrasiActivity extends AppCompatActivity {
    EditText EtNama, EtEmail, EtUsername, EtPassword, EtKonfirPassword;
    TextView TvLogin;
    Button BtnRegister;
    String nama, email, username, password, konfirpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrasi);

        EtNama = findViewById(R.id.ETnama);
        EtEmail = findViewById(R.id.ETemail);
        EtUsername = findViewById(R.id.ETusername);
        EtPassword = findViewById(R.id.ETpassword);
        EtKonfirPassword = findViewById(R.id.ETkonfirpassword);

        TvLogin = findViewById(R.id.TVlogin);

        BtnRegister = findViewById(R.id.BTNregister);
        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = EtNama.getText().toString();
                email = EtEmail.getText().toString();
                password = EtPassword.getText().toString();
                konfirpassword = EtKonfirPassword.getText().toString();
                if (password.equals(konfirpassword)){
                    Register(nama, email, username, konfirpassword);
                }
                else{
                    Toast.makeText(RegistrasiActivity.this, "Password dan Konfirmasi Password Tidak Sama", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Register(String nama, String email, String username, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserRegister,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                Toast.makeText(RegistrasiActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Login gagal
                                Toast.makeText(RegistrasiActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrasiActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data ke server
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        // Menambahkan request ke antrian request Volley
        Volley.newRequestQueue(this).add(request);
    }
}