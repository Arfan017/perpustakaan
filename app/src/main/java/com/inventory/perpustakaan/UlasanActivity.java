package com.inventory.perpustakaan;

import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.ID_USER;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.SHARED_PREFS;
import static com.inventory.perpustakaan.SharedPreferences.SharedPreferences.STATUS_MEMBER;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inventory.perpustakaan.Adapter.AdapterUlasan;
import com.inventory.perpustakaan.Api.konfig;
import com.inventory.perpustakaan.Model.ModelUlasan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UlasanActivity extends AppCompatActivity {
    private LinearLayout LVUlasan;
    private RecyclerView recyclerView;
    private ArrayList<ModelUlasan> modelUlasanArrayList;
    private String id_buku, Nisn_isbn;
    FloatingActionButton FABUlasan;
    RatingBar RBRating;
    EditText ETUlasan;
    Button BTNSimpan, BTNBatal, BTNPinjam;
    Boolean isAllFabsVisible;
    SharedPreferences sharedpreferences;
    String Id_member;
    String status_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ulasan);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Id_member = sharedpreferences.getString(ID_USER, null);
        status_member = sharedpreferences.getString(STATUS_MEMBER, null);


        isAllFabsVisible = false;

        Nisn_isbn = getIntent().getStringExtra("nisn_isbn");

        FABUlasan = findViewById(R.id.FABUlasan);
        RBRating = findViewById(R.id.RBRating);
        ETUlasan = findViewById(R.id.ETUlasan);
        LVUlasan = findViewById(R.id.LLUlasan);
        BTNSimpan = findViewById(R.id.BTNSimpan);
        BTNBatal = findViewById(R.id.BTNBatal);
        BTNPinjam = findViewById(R.id.BTNPinjam);

        LVUlasan.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.RVUlasan);
        modelUlasanArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, Nisn_isbn.toString(), Toast.LENGTH_SHORT).show();

        GetDataUlasan(Nisn_isbn);

        BTNPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FABUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(status_member) == 0){
                    PesanAlert();
                } else if (Integer.parseInt(status_member) == 2) {
                    PesanAlert1();
                } else {
                    if (!isAllFabsVisible) {
                        LVUlasan.setVisibility(View.VISIBLE);
                        isAllFabsVisible = true;
                    } else {
                        LVUlasan.setVisibility(View.GONE);
                        isAllFabsVisible = false;
                    }
                }

            }
        });

        BTNSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nisn_isbn = Nisn_isbn;
                String id_member = Id_member;
                String rating = String.valueOf(RBRating.getRating());
                String ulasan = ETUlasan.getText().toString();
                TambahUlasan(nisn_isbn, id_member, rating, ulasan);

                ETUlasan.setText("");
                RBRating.setRating(0.0f);
                LVUlasan.setVisibility(View.GONE);
                isAllFabsVisible = false;

                finish();
                startActivity(getIntent());
            }
        });

        BTNBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETUlasan.setText("");
                RBRating.setRating(0.0f);
                LVUlasan.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });
    }

    private void GetDataUlasan(String nisn_isbn) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        asyncDialog.setMessage("Mengambil Data Ulasan...");
        asyncDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlGetUlasan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            if (array.length() != 0) {
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject ulasan = array.getJSONObject(i);

                                    //adding the product to product list
                                    modelUlasanArrayList.add(new ModelUlasan(
                                            ulasan.getInt("id_rating"),
                                            ulasan.getString("nisn_isbn"),
                                            ulasan.getInt("rating"),
                                            ulasan.getString("ulasan"),
                                            ulasan.getString("gambar_buku"),
                                            ulasan.getString("nama")
                                    ));
                                }
                                //creating adapter object and setting it to recyclerview
                                AdapterUlasan adapter = new AdapterUlasan(modelUlasanArrayList, UlasanActivity.this);
                                recyclerView.setAdapter(adapter);
                                asyncDialog.dismiss();
                            } else {
                                Toast.makeText(UlasanActivity.this, "Tidak Ada Data Ulasan", Toast.LENGTH_SHORT).show();
                                asyncDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UlasanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data membername dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("nisn_isbn", nisn_isbn);
                return params;
            }
        };
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void TambahUlasan(String id_buku, String id_member, String rating, String ulasan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlAddUlasan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                Toast.makeText(UlasanActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                                LVUlasan.setVisibility(View.GONE);
                                isAllFabsVisible = false;
                                GetDataUlasan(id_buku);
                            }

                            //creating adapter object and setting it to recyclerview
                            AdapterUlasan adapter = new AdapterUlasan(modelUlasanArrayList, UlasanActivity.this);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UlasanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error", e.getMessage());
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UlasanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirim data membername dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("nisn_isbn", Nisn_isbn);
                params.put("id_member", id_member);
                params.put("rating", rating);
                params.put("ulasan", ulasan);
                return params;
            }
        };
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the member presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
    
    private void PesanAlert() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(UlasanActivity.this);

        // Set Alert Title
        builder.setTitle("Peringatan !");

        // Set the message show for the Alert time
        builder.setMessage("Anda belum menjadi member. \nAnda tidak dapat memberikan ulasan");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Daftar Member", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            Intent intent = new Intent(UlasanActivity.this, FormulirActivity.class);
            startActivity(intent);
            finish();
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

    private void PesanAlert1() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(UlasanActivity.this);

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