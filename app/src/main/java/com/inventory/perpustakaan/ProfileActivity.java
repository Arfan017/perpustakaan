package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.Api.konfig.UrlImageProfile;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.ID_USER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.PASSWORD_KEY;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.SHARED_PREFS;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.STATUS_MEMBER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.USERNAME_KEY;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inventory.perpustakaan.Api.konfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton FABAdd, FABLogout, FABEdit, FABSimpan;
    TextView TVlogout, TVedit, TVsimpan, TVnomember, TVnama;
    Boolean isAllFabsVisible;
    EditText ETnama, ETjenkel, ETnohp, ETpekerjaan, ETemail, ETttllahir, ETalamat,
            EditNoIdentitas, EditNama, EditJenkel, EditTtl, EditEmail, EditAlamatIdentitas,
            EditAlamatSekarang, EditNohp, EditPekerjaan, EditNamaInstitusi, EditAlamatInstitusi, EtTempatLahir, EtTgLahir;
    ImageView IVprofile;
    ScrollView SVedit;
    Button BtnSimpan, BtnBatal;
    SharedPreferences sharedpreferences;
    String id_member;
    TableRow TRRiwayat;
    CheckBox checkBox;
    Spinner dropdownJenisKelamin;
    String[] jenisKelamin = {"Laki-laki", "Perempuan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_member = sharedpreferences.getString(ID_USER, null);


        IVprofile = findViewById(R.id.IVProfile);
        TRRiwayat = findViewById(R.id.row_riwayat);

        // inisialisasi EditText
        ETnama = findViewById(R.id.ETNama);
        ETnohp = findViewById(R.id.ETNohp);
        ETemail = findViewById(R.id.ETEmail);
        ETalamat = findViewById(R.id.ETAlamat);
        ETttllahir = findViewById(R.id.ETTtl);

//        EditNoIdentitas = findViewById(R.id.EditNoIdentitas);
        EditNama = findViewById(R.id.EditNama);
//        EditJenkel = findViewById(R.id.EditJenkel);
        dropdownJenisKelamin = findViewById(R.id.dropdown_JK);
        EtTempatLahir = findViewById(R.id.ETtempatlahir);
        EtTgLahir = findViewById(R.id.ETtgllahir);
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

        EtTgLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        checkBox = findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    EditAlamatSekarang.setEnabled(false);
                    EditAlamatSekarang.setText(EditAlamatSekarang.getText().toString());
                } else {
                    EditAlamatSekarang.setEnabled(true);
                    EditAlamatSekarang.setText("");
                }
            }
        });

        ArrayAdapter<String> adapterJenisKelamin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisKelamin);
        dropdownJenisKelamin.setAdapter(adapterJenisKelamin);

        if (id_member != null) {
            SelectProfile(id_member);
        } else {
            PesanAlert();
        }

        ETnama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Tidak perlu diisi jika tidak ada aksi sebelum teks berubah
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Mengecek apakah inputan mengandung angka
                if (charSequence.toString().matches(".*\\d.*")) {
                    ETnama.setError("Tempat lahir tidak boleh berisi angka");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Tidak perlu diisi jika tidak ada aksi setelah teks berubah
            }
        });

        TRRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DaftarPeminjamanActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String NoIdentitas = EditNoIdentitas.getText().toString();
                String Nama = EditNama.getText().toString();
                String Jenkel = dropdownJenisKelamin.getSelectedItem().toString();
                String TempatLahir = EtTempatLahir.getText().toString();
                String TanggalLahir = EtTgLahir.getText().toString();
                String Email = EditEmail.getText().toString();
                String AlamatIdentitas = EditAlamatIdentitas.getText().toString();
                String AlamatSekarang = EditAlamatSekarang.getText().toString();
                String Nohp = EditNohp.getText().toString();
                String Pekerjaan = EditPekerjaan.getText().toString();
                String NamaInstitusi = EditNamaInstitusi.getText().toString();
                String AlamatInstitusi = EditAlamatInstitusi.getText().toString();

                if (EditNoIdentitas.getText().toString().isEmpty()){
                    EditNoIdentitas.setError("No Identitas tidak boleh kosong");
                } else if (EditNama.getText().toString().isEmpty()) {
                    EditNama.setError("Nama tidak boleh kosong");
                } else if (EditJenkel.getText().toString().isEmpty()) {
                    EditJenkel.setError("Jenis Jekamin tidak boleh kosong");
                } else if (EditTtl.getText().toString().isEmpty()) {
                    EditTtl.setError("Tempat, tanggal lahir tidak boleh kosong");
                } else if (EditAlamatIdentitas.getText().toString().isEmpty()) {
                    EditAlamatIdentitas.setError("Alamat tidak boleh kosong");
                } else if (EditAlamatSekarang.getText().toString().isEmpty()) {
                    EditAlamatSekarang.setError("Alamat tidak boleh kosong");
                } else if (EditNohp.getText().toString().isEmpty()) {
                    EditNohp.setError("No Hp tidak boleh kosong");
                } else if (EditPekerjaan.getText().toString().isEmpty()) {
                    EditPekerjaan.setError("Pekerjaan tidak boleh kosong");
                } else if (EditNamaInstitusi.getText().toString().isEmpty()) {
                    EditNamaInstitusi.setError("Nama Institusi tidak boleh kosong");
                } else if (EditAlamatInstitusi.getText().toString().isEmpty()) {
                    EditAlamatInstitusi.setError("Alamat Institusi tidak boleh kosong");
                } else {
                    EditProfile(NoIdentitas,
                            Nama,
                            Jenkel,
                            TempatLahir, TanggalLahir,
                            Email,
                            AlamatIdentitas,
                            AlamatSekarang,
                            Nohp,
                            Pekerjaan,
                            NamaInstitusi,
                            AlamatInstitusi);
                }
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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(USERNAME_KEY);
                editor.remove(PASSWORD_KEY);
                editor.remove(ID_USER);
                editor.remove(STATUS_MEMBER);

                editor.apply();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class );
                startActivity(intent);
                finish();
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
    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ProfileActivity.this,
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

    private void SelectProfile(String id_member) {
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
                                String tgl_lahir = jsonObject.getString("tgl_lahir");
                                String alamat = jsonObject.getString("alamat");
                                String gambar = jsonObject.getString("gambar");

                                Glide.with(ProfileActivity.this).load(UrlImageProfile + gambar).into(IVprofile);
                                TVnama.setText(nama);
                                TVnomember.setText(no_member);
                                ETnama.setText(nama);
                                ETnohp.setText(no_hp);
                                ETemail.setText(email);
                                ETttllahir.setText(tgl_lahir);
                                ETalamat.setText(alamat);

                                SVedit.setVisibility(View.GONE);
                                asyncDialog.dismiss();

                            } else {
                                // Login gagal
                                asyncDialog.dismiss();
                                PesanAlert();
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
                params.put("id_member", id_member);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void EditProfile(String NoIdentitas, String Nama, String Jenkel, String Ttl, String Email, String AlamatIdentitas, String AlamatSekarang, String Nohp, String Pekerjaan, String NamaInstitusi, String AlamatInstitusi,
                             String alamatInstitusi) {
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
                                SelectProfile(id_member);

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
                params.put("id_member", id_member);
                params.put("NoIdentitas", NoIdentitas.toString());
                params.put("Nama", Nama.toString());
                params.put("Jenkel", Jenkel.toString());
                params.put("TempatLahir", Ttl.toString());
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

    private void PesanAlert() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        // Set Alert Title
        builder.setTitle("Peringatan !");

        // Set the message show for the Alert time
        builder.setMessage("Anda belum menjadi member. \nData diri masih kosong");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Daftar Member", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            Intent intent = new Intent(ProfileActivity.this, FormulirActivity.class);
            startActivity(intent);
//            finish();
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
}