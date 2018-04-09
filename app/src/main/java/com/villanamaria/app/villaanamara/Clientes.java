package com.villanamaria.app.villaanamara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.villanamaria.app.villaanamara.Adapters.AdapterClienteC;
import com.villanamaria.app.villaanamara.Adapters.clienteAdapter;
import com.villanamaria.app.villaanamara.clases.cliente;
import com.villanamaria.app.villaanamara.clases.mesa;
import com.villanamaria.app.villaanamara.data.contracts;
import com.villanamaria.app.villaanamara.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Clientes extends AppCompatActivity {

    private ListView mlist;
    private clienteAdapter madapter;
    private Button btnCrearCliente;
    private EditText etsearch;
    private ListView list;
    private String[] moviewList;
    String Servidor, filtro,enlace,codpedido,codmesa,cadena="";
    SharedPreferences spLogin;
    dbhandler mdbdata;
    private ProgressDialog dialog;
    private AdapterClienteC mAdapter;
    dbhandler mDbHelper;
   /* public static ArrayList<MovieNames> movieNamesArrayList;
    public static ArrayList<MovieNames> array_sort;*/
    int textlength = 0;
    private static final String TAG = "Clientes";
    ArrayList<cliente> clientes = new ArrayList<cliente>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarClientes);
            setSupportActionBar(toolbar);
            dialog=new ProgressDialog(this);
            //ArrayList<cliente> data= generarDatos();
            spLogin=getSharedPreferences("login",MODE_PRIVATE);
            Servidor =spLogin.getString("servidor",null);
            enlace=Servidor+"listaCliente.php";
            Log.i(TAG, "onCreate: enlace "+enlace);
            codpedido=getIntent().getStringExtra("codpedido");
            codmesa=getIntent().getStringExtra("mesa");
            Log.i(TAG, "onCreate: codpedido "+codpedido);
            btnCrearCliente=(Button)findViewById(R.id.btnBuscarCliente);
            etsearch=(EditText)findViewById(R.id.editT_buscarCliente);
            mdbdata=new dbhandler(this);
            mAdapter=new AdapterClienteC(this, null);

            //madapter=new clienteAdapter(this, data);
            mlist=(ListView)findViewById(R.id.listVClientes);
            mlist.setClickable(true);
            mDbHelper = new dbhandler(this);
            mlist.setScrollContainer(false);
            mlist.setClickable(true);
            mlist.setAdapter(mAdapter);
//            mlist.setAdapter(madapter);
            dialog.setTitle("Conectando");
            dialog.setMessage("Espere por favor...");
            dialog.setCancelable(false);
            dialog.show();
            new ClientesLoadTask().execute();

            etsearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String text = etsearch.getText().toString();
                    cadena=text;
                    new ClientesLoadTask().execute();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor citem =(Cursor) mAdapter.getItem(i);
                    //cliente selectcliente=(cliente)madapter.getItem(i);
                    //codproducto=citem.getString(citem.getColumnIndex(contracts.productoEntry.Particulo));
                    mdbdata.UpdateCabPedidoCliente(codpedido,citem.getString(citem.getColumnIndex(contracts.cliente.cli_ci)),citem.getString(citem.getColumnIndex(contracts.cliente.cli_nombre)));
                    Intent m=new Intent(Clientes.this,Pedido.class);
                    m.putExtra("codpedido",codpedido);
                    m.putExtra("mesa",codmesa);
                    startActivity(m);
                    finish();
                }
            });

        }catch (Exception e){
            Log.e(TAG, "onCreate: ", e);
        }
    }

    private ArrayList<cliente> generarDatos(){

        for (int a=0; a<10;a++) {
            cliente ncliente=
                    new cliente("Juan "+a, "0101010"+a,"Cuenca","01020304","cliente");
            clientes.add(ncliente);
        }
        return clientes;
    }


    private class ClientesLoadTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
                Cursor resultado=null;
                if(cadena.equals("") || cadena.length()<0){
                    resultado= mDbHelper.getAllClientes("");
                }else if (cadena.length()>0){
                    resultado= mDbHelper.getAllClientes(cadena);
                    Log.i(TAG, "doInBackground: fpro " + cadena+ "  filtro descripcion ");
                }
                return resultado;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //     txt_facturas.setText("Espere Conectando");
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            dialog.dismiss();
            if (cursor != null && cursor.getCount() > 0) {
                mAdapter.swapCursor(cursor);
                Log.i(TAG, "onPostExecute: " + cursor.getCount()+"");
            } else {
                Log.i(TAG, "onPostExecute: " + cursor.getCount()+"");
            }
        }
    }
}
