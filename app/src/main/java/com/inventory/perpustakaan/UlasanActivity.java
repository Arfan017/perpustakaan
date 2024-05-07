package com.inventory.perpustakaan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private String id_buku;
    FloatingActionButton FABUlasan;
    RatingBar RBRating;
    EditText ETUlasan;
    Button BTNSimpan, BTNBatal, BTNPinjam;
    Boolean isAllFabsVisible;
    SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_USER = "id_user";
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ulasan);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString(ID_USER, null);

        isAllFabsVisible = false;

        id_buku = getIntent().getStringExtra("id_buku");

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
        Toast.makeText(this, id_buku.toString(), Toast.LENGTH_SHORT).show();

        GetDataUlasan(id_buku);

        BTNPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FABUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible) {
                    LVUlasan.setVisibility(View.VISIBLE);
                    isAllFabsVisible = true;
                } else {
                    LVUlasan.setVisibility(View.GONE);
                    isAllFabsVisible = false;
                }
            }
        });

        BTNSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id_buku = id_buku;
                String id_member = id_user;
                String rating = String.valueOf(RBRating.getRating());
                String ulasan = ETUlasan.getText().toString();
                TambahUlasan(Id_buku, id_member, rating, ulasan);

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

    private void GetDataUlasan(String id_buku) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfig.UrlGetUlasan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject ulasan = array.getJSONObject(i);

                                //adding the product to product list
                                modelUlasanArrayList.add(new ModelUlasan(
                                        ulasan.getInt("id_rating"),
                                        ulasan.getInt("id_buku"),
                                        ulasan.getInt("rating"),
                                        ulasan.getString("ulasan"),
                                        ulasan.getString("gambar_buku"),
                                        ulasan.getString("nama")
                                ));
                            }
                            //creating adapter object and setting it to recyclerview
                            AdapterUlasan adapter = new AdapterUlasan(modelUlasanArrayList, UlasanActivity.this);
                            recyclerView.setAdapter(adapter);
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
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_buku", id_buku);
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
                // Mengirim data username dan password ke server
                Map<String, String> params = new HashMap<>();
                params.put("id_buku", id_buku);
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
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }

}