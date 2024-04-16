package com.inventory.perpustakaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView TvLupaPassword, TvDaftar;
    EditText EtUsername, EtPassword;
    Button BtnLogin;
    String username, password;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USERNAME_KEY = "username_key";
    public static final String PASSWORD_KEY = "password_key";
    public static final String ID_KEY = "id_key";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//
//        username = sharedpreferences.getString(USERNAME_KEY, null);
//        password = sharedpreferences.getString(PASSWORD_KEY, null);

        EtUsername = findViewById(R.id.ETusernamelogin);
        EtPassword = findViewById(R.id.ETpasswordlogin);
        TvLupaPassword = findViewById(R.id.TVlupapassword);
        TvDaftar = findViewById(R.id.TVdaftar);
        BtnLogin = findViewById(R.id.BTNlogin);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = EtUsername.getText().toString();
                String password = EtPassword.getText().toString();
                loginApp(username, password);
            }
        });

        TvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginApp(String username, String password) {
        // Buatkan request untuk mengirim data ke server
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.171.41/perpustakaan/php/query/userlogin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            boolean success = jsonObject.getBoolean("success");
//                            String[] messageSplit = message.split("[ ]");

                            if (success) {
//                                String id = messageSplit[3];
//
//                                SharedPreferences.Editor editor = sharedpreferences.edit();
//                                editor.putString(USERNAME_KEY, username);
//                                editor.putString(PASSWORD_KEY, password);
//                                editor.putString(ID_KEY, id);
//
//                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, FormulirActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Login gagal
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
//                Toast.makeText(LoginActivity.this, "sedang mengirim " + username + " Dan " + password, Toast.LENGTH_SHORT).show();
                Map<String, String> params = new HashMap<>();
                params.put("username", username.toString());
                Log.d("params are 1 :", "" + params);
                params.put("password", password.toString());
                Log.d("params are 2 :", "" + params);
                return params;
            }
        };
        // Menambahkan request ke antrian request Volley
        Volley.newRequestQueue(this).add(request);
    }
}