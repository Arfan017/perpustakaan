package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Api.konfig.UrlUserDaftar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormulirActivity extends AppCompatActivity {
    EditText EtNoIdentitas, EtNama, EtJenisKelamin, EtTtgLahir, EtAlamat1, EtAlamat2, EtNoTelp, EtPekerjaan, EtNamaInstitusi, EtAlamatInstitusi;
    Button BtnKirim;
    String id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1, alamat2, notelp, pekerjaan, namatinstitusi, alamatinstitusi, encodedImageString;
    SharedPreferences sharedpreferences;
    CircleImageView circleImageprofile;
    Bitmap capturedImage;
    int SizeofImage;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String STATUS_MEMBER = "status_member";
    public static final String ID_USER = "id_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulir);

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
                String _id = id;
                noidentitas = EtNoIdentitas.getText().toString();
                nama = EtNama.getText().toString();
                jeniskelamin = EtJenisKelamin.getText().toString();
                ttgllahir = EtTtgLahir.getText().toString();
                alamat1 = EtAlamat1.getText().toString();
                alamat2 = EtAlamat2.getText().toString();
                notelp = EtNoTelp.getText().toString();
                pekerjaan = EtPekerjaan.getText().toString();
                namatinstitusi = EtNamaInstitusi.getText().toString();
                alamatinstitusi = EtAlamatInstitusi.getText().toString();

                if (EtNoIdentitas.getText().toString().isEmpty()) {
                    EtNoIdentitas.setError("No Identitas tidak boleh kosong");
                } else if (EtNama.getText().toString().isEmpty()) {
                    EtNama.setError("Nama tidak boleh kosong");
                } else if (EtJenisKelamin.getText().toString().isEmpty()) {
                    EtJenisKelamin.setError("Jenis Kelamin tidak boleh kosong");
                } else if (EtTtgLahir.getText().toString().isEmpty()) {
                    EtTtgLahir.setError("Tempat, tanggal lahir tidak boleh kosong");
                } else if (EtAlamat1.getText().toString().isEmpty()) {
                    EtAlamat1.setError("Alamat tidak boleh kosong");
                } else if (EtAlamat2.getText().toString().isEmpty()) {
                    EtAlamat2.setError("Alamat tidak boleh kosong");
                } else if (EtNoTelp.getText().toString().isEmpty()) {
                    EtNoTelp.setError("No Hp tidak boleh kosong");
                } else if (EtPekerjaan.getText().toString().isEmpty()) {
                    EtPekerjaan.setError("Pekerjaan tidak boleh kosong");
                } else if (EtNamaInstitusi.getText().toString().isEmpty()) {
                    EtNamaInstitusi.setError("Nama Institusi tidak boleh kosong");
                } else if (EtAlamatInstitusi.getText().toString().isEmpty()) {
                    EtAlamatInstitusi.setError("Alamat Institusi tidak boleh kosong");
                }

                Daftar(_id, noidentitas, nama, jeniskelamin, ttgllahir, alamat1, alamat2, notelp, pekerjaan, namatinstitusi, alamatinstitusi);
            }
        });

        circleImageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Dexter.withActivity(FormulirActivity.this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent, "pilih gambar"), 1);
                                    } else {
                                        Toast.makeText(FormulirActivity.this, "Izin diperlukan untuk mengakses penyimpanan", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "pilih gambar"), 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri filepath = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(filepath);
                    capturedImage = BitmapFactory.decodeStream(imageStream);
                    showResolution("Before resize", capturedImage);

                    capturedImage = resizeBitmap(capturedImage, 800, 800);
                    showResolution("After resize", capturedImage);

                    circleImageprofile.setImageBitmap(capturedImage);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    SizeofImage = baos.toByteArray().length / 1024;

                    if (SizeofImage > 1000) {
                        Toast.makeText(FormulirActivity.this, "Ukuran gambar melebihi 1MB", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void showResolution(String stage, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        String message = stage + " resolution: " + width + " x " + height;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.d("Image Resolution", message);
    }

    private String encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void Daftar(String id, String noidentitas, String nama, String jeniskelamin, String ttgllahir, String alamat1,
                        String alamat2, String notelp, String pekerjaan, String namatinstitusi, String alamatinstitusi) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUserDaftar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                        PesanAlert();
                    }
                } catch (Exception e) {
                    Toast.makeText(FormulirActivity.this, "Failed to upload image from android: " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Upload Error", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FormulirActivity.this, "Failed to upload image from android", Toast.LENGTH_SHORT).show();
                Log.e("Upload Error", error.toString());
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
                params.put("image", encodebitmap(capturedImage));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, // Waktu tunggu dalam milidetik
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Jumlah percobaan ulang
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxHeight;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private void PesanAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormulirActivity.this);

        builder.setTitle("Pemberitahuan");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(STATUS_MEMBER, "2");
        editor.commit();

        builder.setMessage("Anda telah mendaftar sebagai member, silahkan tunggu verifikasi admin.");
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}
