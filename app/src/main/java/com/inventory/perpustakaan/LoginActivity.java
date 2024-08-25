package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Captcha.SECRET_KEY;
import static com.inventory.perpustakaan.Captcha.SITE_KEY;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.ID_USER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.PASSWORD_KEY;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.SHARED_PREFS;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.STATUS_MEMBER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.USERNAME_KEY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    TextView TvLupaPassword, TvDaftar, Tvlanjut;
    EditText EtUsername, EtPassword;
    Button BtnLogin;
    String username, password;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        username = sharedpreferences.getString(USERNAME_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);

        EtUsername = findViewById(R.id.ETusernamelogin);
        EtPassword = findViewById(R.id.ETpasswordlogin);
        TvLupaPassword = findViewById(R.id.TVlupapassword);
        TvDaftar = findViewById(R.id.TVdaftar);
        Tvlanjut = findViewById(R.id.TVlanjut);
        BtnLogin = findViewById(R.id.BTNlogin);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EtUsername.getText().toString().isEmpty()) {
                    EtUsername.setError("Username tidak boleh kosong");
                } else if (EtPassword.getText().toString().isEmpty()) {
                    EtPassword.setError("Password tidak boleh kosong");
                }else {
                    verifyGoogleReCAPTCHA();
                }
            }
        });

        TvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FormulirActivity.class);
                startActivity(intent);
            }
        });

        Tvlanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(STATUS_MEMBER, "0");
                editor.apply();
                startActivity(intent);
                finish();
            }
        });

        TvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (username != null && password != null) {
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        }
    }

    private void loginApp(String username, String password) {
        // Buatkan request untuk mengirim data ke server
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                String message = jsonObject.getString("message");
                                String id_member = jsonObject.getString("id_member");
                                String status_member = jsonObject.getString("status_member");

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(USERNAME_KEY, username);
                                editor.putString(PASSWORD_KEY, password);
                                editor.putString(ID_USER, id_member);
                                editor.putString(STATUS_MEMBER, status_member);

                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Login gagal
//                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                PesanAlert1();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("username", username.toString());
                params.put("password", password.toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    //=================== captcha ===================//
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
                            Toast.makeText(LoginActivity.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show();

                                String username = EtUsername.getText().toString().trim();
                                String password = EtPassword.getText().toString();
                                loginApp(username.trim(), password.trim());
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

    private void PesanAlert1() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

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

}