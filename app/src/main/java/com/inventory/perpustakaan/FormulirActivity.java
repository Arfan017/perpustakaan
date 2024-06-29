package com.inventory.perpustakaan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormulirActivity extends AppCompatActivity {
    EditText EtNoIdentitas, EtNama, EtJenisKelamin, EtTtgLahir, EtAlamat1,
            EtAlamat2, EtNoTelp, EtPekerjaan, EtNamaInstitusi, EtAlamatInstitusi;
    Button BtnKirim;
    String id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1,
            alamat2, notelp, pekerjaan, namatinstitusi, alamatinstitusi, encodedImageString;
    SharedPreferences sharedpreferences;
    CircleImageView circleImageprofile;
    Bitmap bitmap;
    int SizeofImage;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String STATUS_MEMBER = "status_member";
    public static final String ID_USER = "id_user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulir);

        // Initialization
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID_USER, null);

        EtNoIdentitas = findViewById(R.id.ETnoidentitas);
        EtNama = findViewById(R.id.ETnama);
        EtJenisKelamin = findViewById(R.id.ETjeniskelamin);
        EtTtgLahir = findViewById(R.id.ETttgllahir);
        EtAlamat1 = findViewById(R.id.ETalamat1);
        EtAlamat2 = findViewById(R.id.ETalamat2);
        EtNoTelp = findViewById(R.id.ETnotelp);
        EtPekerjaan = findViewById(R.id.ETperkerjaan);
        EtNamaInstitusi = findViewById(R.id.ETnamainstitusi);
        EtAlamatInstitusi = findViewById(R.id.ETalamatinstitusi);
        circleImageprofile = findViewById(R.id.profile_image);
        BtnKirim = findViewById(R.id.BTNkirim);

        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Capture data
                noidentitas = EtNoIdentitas.getText().toString().trim();
                nama = EtNama.getText().toString().trim();
                jeniskelamin = EtJenisKelamin.getText().toString().trim();
                ttgllahir = EtTtgLahir.getText().toString().trim();
                alamat1 = EtAlamat1.getText().toString().trim();
                alamat2 = EtAlamat2.getText().toString().trim();
                notelp = EtNoTelp.getText().toString().trim();
                pekerjaan = EtPekerjaan.getText().toString().trim();
                namatinstitusi = EtNamaInstitusi.getText().toString().trim();
                alamatinstitusi = EtAlamatInstitusi.getText().toString().trim();

                // Validate data
                if (noidentitas.isEmpty() || nama.isEmpty() || jeniskelamin.isEmpty() || ttgllahir.isEmpty() ||
                        alamat1.isEmpty() || notelp.isEmpty() || pekerjaan.isEmpty() || namatinstitusi.isEmpty() ||
                        alamatinstitusi.isEmpty() || encodedImageString == null) {
                    Toast.makeText(FormulirActivity.this, "Harap isi semua data", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("FormulirActivity", "Mengirim data: " + noidentitas + ", " + nama + ", " + jeniskelamin + ", " +
                            ttgllahir + ", " + alamat1 + ", " + alamat2 + ", " + notelp + ", " + pekerjaan + ", " +
                            namatinstitusi + ", " + alamatinstitusi);
                    Daftar(id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1, alamat2, notelp, pekerjaan, namatinstitusi, alamatinstitusi);
                }
            }
        });

        circleImageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(FormulirActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // Handle the case where permission is denied
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                encodebitmap(bitmap);
                if (SizeofImage > 1000) {
                    Toast.makeText(FormulirActivity.this, "Ukuran gambar melebihi 1MB", Toast.LENGTH_SHORT).show();
                } else {
                    circleImageprofile.setImageBitmap(bitmap);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteofimage = byteArrayOutputStream.toByteArray();
        SizeofImage = byteofimage.length / 1024; // convert to KB
        encodedImageString = Base64.encodeToString(byteofimage, Base64.DEFAULT);
    }

    private void Daftar(String id, String noidentitas, String nama, String jeniskelamin, String ttgllahir, String alamat1,
                        String alamat2, String notelp, String pekerjaan, String namatinstitusi, String alamatinstitusi) {
        StringRequest request = new StringRequest(Request.Method.POST, konfig.UrlUserDaftar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                PesanAlert();
                                Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
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
                params.put("namatinstitusi", namatinstitusi);
                params.put("alamatinstitusi", alamatinstitusi);
                params.put("gambar", encodedImageString);
                Log.d("FormulirActivity", "Params: " + params.toString()); // Logging params
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
        Toast.makeText(FormulirActivity.this, "Mengirim data ke database", Toast.LENGTH_SHORT).show();
    }

    private void PesanAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormulirActivity.this);
        builder.setTitle("Pemberitahuan");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(STATUS_MEMBER, "2");
        editor.apply();

        builder.setMessage("Anda telah mendaftar sebagai member, silahkan tunggu verifikasi admin.");
        builder.setCancelable(false);

        builder.setNegativeButton("Ok", (dialog, which) -> {
            dialog.cancel();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}