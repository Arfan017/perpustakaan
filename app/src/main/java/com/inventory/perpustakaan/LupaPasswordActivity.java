package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Captcha.SECRET_KEY;
import static com.inventory.perpustakaan.Captcha.SITE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.inventory.perpustakaan.Api.konfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPasswordActivity extends AppCompatActivity {

    EditText ETemail_lupapass, ETpassword_lupapass, ETkonfirpassword_lupapass;
    Button BTNgantipass;
    String Email, PassBaru, KonfPassBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lupa_password);

        ETemail_lupapass = findViewById(R.id.ETemail_lupapass);
        ETpassword_lupapass = findViewById(R.id.ETpassword_lupapass);
        ETkonfirpassword_lupapass = findViewById(R.id.ETkonfirpassword_lupapass);
        BTNgantipass = findViewById(R.id.BTNgantipass);

        BTNgantipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = ETemail_lupapass.getText().toString();
                PassBaru = ETpassword_lupapass.getText().toString();
                KonfPassBaru = ETkonfirpassword_lupapass.getText().toString();
                if (ETemail_lupapass.getText().toString().isEmpty()){
                    ETemail_lupapass.setError("Email tidak boleh kosong");
                } else if (ETpassword_lupapass.getText().toString().isEmpty()) {
                    ETpassword_lupapass.setError("Password tidak boleh kosong");
                } else if (ETkonfirpassword_lupapass.getText().toString().isEmpty()) {
                    ETkonfirpassword_lupapass.setError("Konfirmasi Password tidak boleh kosong");
                } else {
                    if (PassBaru.equals(KonfPassBaru)){
                        verifyGoogleReCAPTCHA();
                    } else
                    {
                        Toast.makeText(LupaPasswordActivity.this, "Password dan Konfirmasi Password Tidak Sama", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void LupasPass(String Email, String PassBaru) {
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserLupaPassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                Toast.makeText(LupaPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LupaPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Login gagal
                                Toast.makeText(LupaPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LupaPasswordActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data ke server
                Map<String, String> params = new HashMap<>();
                params.put("email", Email);
                params.put("password", PassBaru);
                return params;
            }
        };
        // Menambahkan request ke antrian request Volley
        Volley.newRequestQueue(this).add(request);
    }

    private void verifyGoogleReCAPTCHA() {
        // below line is use for getting our safety
        // net client and verify with reCAPTCHA
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                // after getting our client we have
                // to add on success listener.
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        // in below line we are checking the response token.
                        if (!response.getTokenResult().isEmpty()) {
                            // if the response token is not empty then we
                            // are calling our verification method.
                            handleVerification(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when we get any error.
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            // below line is use to display an error message which we get.
                            Log.d("TAG", "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            // below line is use to display a toast message for any error.
                            Toast.makeText(LupaPasswordActivity.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void handleVerification(final String responseToken) {
        // inside handle verification method we are
        // verifying our user with response token.
        // url to sen our site key and secret key
        // to below url using POST method.
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // in this we are making a string request and
        // using a post method to pass the data.
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // inside on response method we are checking if the
                        // response is successful or not.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // if the response is successful then we are
                                // showing below toast message.
                                Toast.makeText(LupaPasswordActivity.this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show();

                                LupasPass(Email, KonfPassBaru);

                            } else {
                                // if the response if failure we are displaying
                                // a below toast message.
                                Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.getString("error-codes")), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            // if we get any exception then we are
                            // displaying an error message in logcat.
                            Log.d("TAG", "JSON exception: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // inside error response we are displaying
                        // a log message in our logcat.
                        Log.d("TAG", "Error message: " + error.getMessage());
                    }
                }) {
            // below is the getParams method in which we will
            // be passing our response token and secret key to the above url.
            @Override
            protected Map<String, String> getParams() {
                // we are passing data using hashmap
                // key and value pair.
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}