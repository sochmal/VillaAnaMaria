package com.villanamaria.app.villaanamara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.villanamaria.app.villaanamara.Adapters.productoAdapter;
import com.villanamaria.app.villaanamara.clases.producto;
import com.villanamaria.app.villaanamara.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuLugares extends AppCompatActivity {
    private static final String TAG = "MenuLugares";
    private CardView cv1,cd_grill, cv_pizerria, cv_patio, cv_cafeteria;
    SharedPreferences spLogin;
    String Servidor,penlace="",pfiltro="";
    private ListView mlistV;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lugares);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarLugares);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Detalle Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spLogin=getSharedPreferences("login",MODE_PRIVATE);
        Servidor =spLogin.getString("servidor",null);
        Log.i(TAG, "onCreate: Servidor "+Servidor);

        cd_grill =(CardView)findViewById(R.id.cv1ug);
        cv_pizerria =(CardView)findViewById(R.id.cv2ugb);
        cv_patio =(CardView)findViewById(R.id.cv2uga);
        cv_cafeteria =(CardView)findViewById(R.id.cv2ug);


        cd_grill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        irmesas("grill");
            }
        });

        cv_pizerria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irmesas("pizzeria");
            }
        });
        cv_patio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irmesas("patio");
            }
        });
        cv_cafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irmesas("chocafeteria");
            }
        });

    }
    void irmesas(String filtro){
        Intent m=new Intent(MenuLugares.this,Mesas.class);
        m.putExtra("filtro", filtro);
        startActivity(m);
    }


}
