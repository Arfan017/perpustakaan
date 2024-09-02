package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Api.konfig.UrlUserDaftar;
import static com.inventory.perpustakaan.Captcha.SECRET_KEY;
import static com.inventory.perpustakaan.Captcha.SITE_KEY;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.ID_USER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.PASSWORD_KEY;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.SHARED_PREFS;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.STATUS_MEMBER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.USERNAME_KEY;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormulirActivity extends AppCompatActivity {
    EditText EtNoIdentitas, EtNama, EtTempatLahir, EtTgLahir, EtAlamat1,
            EtAlamat2, EtNoTelp, EtPekerjaan, EtNamaInstitusi, EtAlamatInstitusi,
            ETemail, ETusername, ETpassword, ETkonfpassword;
    Button BtnKirim;
    String id, noidentitas, nama, jeniskelamin, tempatlahir, tgllahir,
            alamat1, alamat2, notelp, pekerjaan, namatinstitusi,
            alamatinstitusi, username, email, password, kartuidentitas, _id;
    SharedPreferences sharedpreferences;
    CircleImageView circleImageprofile;
    Spinner dropdownJenisKelamin, dropdownKartuIdentitas;
    Bitmap capturedImage;
    CheckBox checkBox;
    int SizeofImage;
    String[] kartuIdentitas = {"KTP", "Kartu Mahasiswa",
            "Kartu Pelajar", "SIM"};

    String[] jenisKelamin = {"Laki-laki", "Perempuan"};

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulir);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID_USER, null);

        EtNoIdentitas = findViewById(R.id.ETnoidentitas);
        EtNama = findViewById(R.id.ETnama);
        dropdownJenisKelamin = findViewById(R.id.dropdown_JK);
        dropdownKartuIdentitas = findViewById(R.id.dropdown_kartuidentitas);
        EtTempatLahir = findViewById(R.id.ETtempatlahir);
        EtTgLahir = findViewById(R.id.ETtgllahir);
        EtAlamat1 = findViewById(R.id.ETalamat1);
        EtAlamat2 = findViewById(R.id.ETalamat2);
        EtNoTelp = findViewById(R.id.ETnotelp);
        EtPekerjaan = findViewById(R.id.ETperkerjaan);
        EtNamaInstitusi = findViewById(R.id.ETnamainstitusi);
        EtAlamatInstitusi = findViewById(R.id.ETalamatinstitusi);
        circleImageprofile = findViewById(R.id.profile_image);
        ETusername = findViewById(R.id.ETusername);
        ETpassword = findViewById(R.id.ETpassword);
        ETkonfpassword = findViewById(R.id.ETkonfpassword);
        ETemail = findViewById(R.id.ETemail);
        BtnKirim = findViewById(R.id.BTNkirim);
        checkBox = findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    EtAlamat2.setEnabled(false);
                    EtAlamat2.setText(EtAlamat1.getText().toString());
                } else {
                    EtAlamat2.setEnabled(true);
                    EtAlamat2.setText("");
                }
            }
        });

        ArrayAdapter<String> adapterJenisKelamin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisKelamin);
        dropdownJenisKelamin.setAdapter(adapterJenisKelamin);

        ArrayAdapter<String> adapterKartuIdentitas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kartuIdentitas);
        dropdownKartuIdentitas.setAdapter(adapterKartuIdentitas);

        EtTgLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleForm();
            }
        });

        circleImageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImageClick(v);
            }
        });

        EtTempatLahir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Tidak perlu diisi jika tidak ada aksi sebelum teks berubah
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Mengecek apakah inputan mengandung angka
                if (charSequence.toString().matches(".*\\d.*")) {
                    EtTempatLahir.setError("Tempat lahir tidak boleh berisi angka");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Tidak perlu diisi jika tidak ada aksi setelah teks berubah
            }
        });

        EtNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Tidak perlu diisi jika tidak ada aksi sebelum teks berubah
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Mengecek apakah inputan mengandung angka
                if (charSequence.toString().matches(".*\\d.*")) {
                    EtNama.setError("Nama tidak boleh berisi angka");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Tidak perlu diisi jika tidak ada aksi setelah teks berubah
            }
        });

        // Inisialisasi picker gambar untuk Android 13+
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                // Gambar dipilih
                setImageFromUri(uri);
            } else {
                // Tidak ada gambar yang dipilih
            }
        });
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FormulirActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update TextView with selected date
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        EtTgLahir.setText(selectedDate);
                    }
                },
                year, month, day);

        // Show the dialog
        datePickerDialog.show();
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
                    capturedImage = resizeBitmap(capturedImage, 800, 800);

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

    private String encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void Daftar(String id, String kartuidentitas, String noidentitas, String nama, String jeniskelamin, String tempatlahir, String tgllahir, String alamat1,
                        String alamat2, String notelp, String pekerjaan, String namatinstitusi, String alamatinstitusi, String username, String email, String password) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUserDaftar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success) {
                        Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                        PesanAlert();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(STATUS_MEMBER, "2");
                        editor.apply();

                        startActivity(new Intent(FormulirActivity.this, LoginActivity.class));

                    } else {
                        Toast.makeText(FormulirActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FormulirActivity.this, "Gagal memproses response server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kartuidentitas", kartuidentitas);
                params.put("noidentitas", noidentitas);
                params.put("nama", nama);
                params.put("jeniskelamin", jeniskelamin);
                params.put("tempatlahir", tempatlahir);
                params.put("tgllahir", tgllahir);
                params.put("alamat1", alamat1);
                params.put("alamat2", alamat2);
                params.put("notelp", notelp);
                params.put("pekerjaan", pekerjaan);
                params.put("namatinstitusi", namatinstitusi);
                params.put("alamatinstitusi", alamatinstitusi);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("gambar", encodebitmap(capturedImage));
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
                            Toast.makeText(FormulirActivity.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(FormulirActivity.this, "Pengguna terverifikasi dengan Google reCAPTCHA", Toast.LENGTH_SHORT).show();
                                Daftar(_id, kartuidentitas, noidentitas, nama, jeniskelamin, tempatlahir, tgllahir, alamat1, alamat2, notelp, pekerjaan, namatinstitusi, alamatinstitusi, username, email, password);
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

    private boolean validateForm() {
        boolean isValid = true;

        if (EtNoIdentitas.getText().toString().isEmpty()) {
            EtNoIdentitas.setError("No Identitas tidak boleh kosong");
            isValid = false;
        }
        if (EtNama.getText().toString().isEmpty()) {
            EtNama.setError("Nama tidak boleh kosong");
            isValid = false;
        }
        if (EtTempatLahir.getText().toString().isEmpty()) {
            EtTempatLahir.setError("Tempat lahir tidak boleh kosong");
            isValid = false;
        }
        if (EtTgLahir.getText().toString().isEmpty()) {
            EtTgLahir.setError("Tanggal lahir tidak boleh kosong");
            isValid = false;
        }
        if (EtAlamat1.getText().toString().isEmpty()) {
            EtAlamat1.setError("Alamat tidak boleh kosong");
            isValid = false;
        }
        if (EtAlamat2.getText().toString().isEmpty()) {
            EtAlamat2.setError("Alamat tidak boleh kosong");
            isValid = false;
        }
        if (EtNoTelp.getText().toString().isEmpty()) {
            EtNoTelp.setError("No Hp tidak boleh kosong");
            isValid = false;
        }
        if (EtPekerjaan.getText().toString().isEmpty()) {
            EtPekerjaan.setError("Pekerjaan tidak boleh kosong");
            isValid = false;
        }
        if (EtNamaInstitusi.getText().toString().isEmpty()) {
            EtNamaInstitusi.setError("Nama Institusi tidak boleh kosong");
            isValid = false;
        }
        if (EtAlamatInstitusi.getText().toString().isEmpty()) {
            EtAlamatInstitusi.setError("Alamat Institusi tidak boleh kosong");
            isValid = false;
        }
        if (ETusername.getText().toString().isEmpty()) {
            ETusername.setError("Username tidak boleh kosong");
            isValid = false;
        }
        if (ETpassword.getText().toString().isEmpty()) {
            ETpassword.setError("Password tidak boleh kosong");
            isValid = false;
        }
        if (ETemail.getText().toString().isEmpty()) {
            ETemail.setError("Email tidak boleh kosong");
            isValid = false;
        }
        if (capturedImage == null){
            Toast.makeText(FormulirActivity.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        String password = ETpassword.getText().toString();
        String passwordbarkonfirm = ETkonfpassword.getText().toString().trim();
        if (!passwordbarkonfirm.equals(password)) {
            Toast.makeText(FormulirActivity.this, "Password dan konfirmasi password tidak sesuai", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void handleForm() {
        if (validateForm()) {
            // Ambil semua data dari form
            _id = id;
            kartuidentitas = dropdownKartuIdentitas.getSelectedItem().toString();
            noidentitas = EtNoIdentitas.getText().toString();
            nama = EtNama.getText().toString();
            jeniskelamin = dropdownJenisKelamin.getSelectedItem().toString();
            tempatlahir = EtTempatLahir.getText().toString();
            tgllahir = EtTgLahir.getText().toString();
            alamat1 = EtAlamat1.getText().toString();
            alamat2 = EtAlamat2.getText().toString();
            notelp = EtNoTelp.getText().toString();
            pekerjaan = EtPekerjaan.getText().toString();
            namatinstitusi = EtNamaInstitusi.getText().toString();
            alamatinstitusi = EtAlamatInstitusi.getText().toString();
            username = ETusername.getText().toString();
            email = ETemail.getText().toString();
            password = ETpassword.getText().toString();

            // Panggil fungsi untuk verifikasi Google reCAPTCHA atau proses data lainnya
            if (containsDigit(nama)){
                Toast.makeText(FormulirActivity.this, "Nama tidak boleh mengandung angka", Toast.LENGTH_SHORT).show();
            } else if (containsDigit(tempatlahir)){
                Toast.makeText(FormulirActivity.this, "Tempat Lahir tidak boleh mengandung angka", Toast.LENGTH_SHORT).show();
            } else {
                verifyGoogleReCAPTCHA();
            }
        }
    }

    public static boolean containsDigit(String input) {
        return input.matches(".*\\d.*"); // Atau gunakan metode loop
    }

    private void handleVolleyError(VolleyError error) {
        String errorMessage = "Gagal terhubung ke server";

        // Cek jenis error
        if (error instanceof TimeoutError) {
            errorMessage = "Koneksi timeout. Coba lagi nanti.";
        } else if (error instanceof NetworkError) {
            errorMessage = "Tidak ada koneksi internet.";
        } else if (error instanceof ServerError) {
            errorMessage = "Server error. Coba lagi nanti.";
        } else if (error instanceof ParseError) {
            errorMessage = "Terjadi kesalahan saat memproses data.";
        } else if (error instanceof AuthFailureError) {
            errorMessage = "Gagal otentikasi. Cek kredensial Anda.";
        }

        // Cek status kode HTTP jika ada respons dari server
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null) {
            switch (networkResponse.statusCode) {
                case 400:
                    errorMessage = "Request tidak valid. Cek data yang dikirim.";
                    break;
                case 401:
                    errorMessage = "Tidak terautentikasi. Login diperlukan.";
                    break;
                case 403:
                    errorMessage = "Akses ditolak. Anda tidak memiliki izin.";
                    break;
                case 404:
                    errorMessage = "Sumber daya tidak ditemukan.";
                    break;
                case 500:
                    errorMessage = "Kesalahan server internal. Coba lagi nanti.";
                    break;
                case 503:
                    errorMessage = "Layanan tidak tersedia. Coba lagi nanti.";
                    break;
                default:
                    errorMessage = "Terjadi kesalahan tidak terduga: " + networkResponse.statusCode;
                    break;
            }
        }

        // Tampilkan pesan kesalahan
        Toast.makeText(FormulirActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6-12
            Dexter.withActivity(FormulirActivity.this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                launchLegacyImagePicker();
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
            // < Android 6
            launchLegacyImagePicker();
        }
    }

    private void launchLegacyImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "pilih gambar"), 1);
    }

    public void onPickImageClick(android.view.View view) {
        pickImage();
    }

    private void setImage(Bitmap bitmap) {
        circleImageprofile.setImageBitmap(bitmap);
    }

    private void setImageFromUri(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            capturedImage = BitmapFactory.decodeStream(inputStream);
            capturedImage = resizeBitmap(capturedImage, 800, 800);

            if (inputStream != null) {
                inputStream.close();
            }
            setImage(capturedImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
}
