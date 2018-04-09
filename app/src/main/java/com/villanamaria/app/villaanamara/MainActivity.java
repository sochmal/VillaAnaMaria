package com.villanamaria.app.villaanamara;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.villanamaria.app.villaanamara.Adapters.clienteAdapter;
import com.villanamaria.app.villaanamara.clases.cliente;
import com.villanamaria.app.villaanamara.clases.producto;
import com.villanamaria.app.villaanamara.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main Login";
    String strServiror="http://192.168.6.229/villa/account/prog/tablet/";
    SharedPreferences spLogin;
    private  Toolbar toolbar;
    private EditText mPasswordView,txtServer,musuario;
    String usuario, password,penlace,enlace;
    private AutoCompleteTextView mEmailView;
    private dbhandler mDbHelper;
    ProgressDialog dialog,dialog_login;
    Button mEmailSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Detalle Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spLogin=getSharedPreferences("login",MODE_PRIVATE);
        mPasswordView = (EditText) findViewById(R.id.password);
        musuario = (EditText) findViewById(R.id.email);
        mDbHelper = new dbhandler(this);
        txtServer = (EditText) findViewById(R.id.txtServer);
       // strServiror=txtServer.getText().toString();
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //ingresar();
                dialog=new ProgressDialog(MainActivity.this);
                dialog.setTitle("Conectando");
                dialog.setMessage("Espere por favor...");
                dialog.setCancelable(false);
                dialog.show();
                new SendPostRequest().execute();
                Log.i(TAG, "onClick: strServiror  "+txtServer.getText().toString());
                mEmailSignInButton.setEnabled(false);
            }
        });
        this.deleteDatabase(dbhandler.DATABASE_NAME);
        penlace=strServiror+"productos.php";
        enlace=strServiror+"listaCliente.php";
        dialog=new ProgressDialog(this);
        dialog.setTitle("Configurando");
        dialog.setMessage("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
        new ProductosLoadTask().execute();
    }

    public void ingresar(String usuario, String nombreusuario){
        Intent intent = new Intent(this, MenuLugares.class);
        SharedPreferences.Editor e=spLogin.edit();
        Log.i(TAG, "ingresar: splogin "+strServiror);
        e.putString("servidor",strServiror);
        e.putString("usuario",usuario);
        e.putString("usuarionombre",nombreusuario);
        e.commit();
        //  intent.putExtra(USER_NAME, username);
        dialog.dismiss();
        mEmailSignInButton.setEnabled(true);
        finish();
        startActivity(intent);
    }

    public void invokeLogin(View view){
        usuario = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
    }

    private class ClientesLoadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try{
                JsonArrayRequest jsonArrayReq = new JsonArrayRequest(enlace ,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try
                                {
                                    JSONObject obj=null;
                                    Log.i(TAG, "onResponse generar datos: procesanado");
                                    for (int a=0; a<response.length();a++) {
                                        obj = response.getJSONObject(a);
                                        Log.i(TAG, "onResponse: clientes> "+obj.getString("CMA_CODIGO" +
                                                ""));
                                        mDbHelper.nuevoPcliente(new cliente(obj.getString("CMA_NOMBRE"),
                                                obj.getString("CMA_CODIGO"),
                                                obj.getString("CMA_DIRECCION"),
                                                obj.getString("CMA_TELEFONO1"),
                                                ""));
                                    }
                                    dialog.dismiss();
                                }catch (JSONException e){
                                    Log.e(TAG, "parseJsonCanales ",e );
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.i(TAG, "onErrorResponse Clientes: "+error.getMessage());
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_LONG).show();
                    }
                });
                // Adding request to request queue
                Volley.newRequestQueue(MainActivity.this).add(jsonArrayReq);
            }catch (Exception error){
                Log.i(TAG, "parseJsonCanales Clientes: " + error.toString());
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_LONG).show();
            }

            return "Completo";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //     txt_facturas.setText("Espere Conectando");
        }
        @Override
        protected void onPostExecute(String s) {
            //dialog.dismiss();
            //       txt_facturas.setText("Sincronizado");
            // SyncReceive(posSyncRe);
        }
    }
    private class ProductosLoadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try{

                JsonArrayRequest jsonArrayReq = new JsonArrayRequest(
                        penlace,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try
                                {
                                    JSONObject obj=null;
                                    for (int a=0; a<response.length();a++) {
                                        obj = response.getJSONObject(a);
                                        Log.i(TAG, "onResponse: productos> "+obj.getString("IMA_ARTICULO"));
                                        mDbHelper.nuevoProducto(new producto(
                                                obj.getString("IMA_ARTICULO"),
                                                obj.getString("IMA_DESCRIPCION"),
                                                obj.getString("IMA_PRECIO1"),
                                                obj.getString("IMA_TERMINO"),
                                                obj.getString("SUBLI_CODIGO")
                                        ));
                                    }
                                    //dialog.dismiss();
                                }catch (JSONException e){
                                    Log.e(TAG, "parseJsonProductos ",e );
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.i(TAG, "onErrorResponse productos: "+error.getMessage());
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_LONG).show();
                    }
                });
                // Adding request to request queue
                Volley.newRequestQueue(MainActivity.this).add(jsonArrayReq);
            }catch (Exception error){
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_LONG).show();
                Log.i(TAG, "parseJsonCanales productos: " + error.toString());
                dialog.dismiss();
            }
            return "Completo";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            new ClientesLoadTask().execute();
        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        private static final String TAG = "login";
        private Dialog loadingDialog;
        protected void OnPreExecute(){
            loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
        }
        @Override
        protected String doInBackground(String... params) {
            String result=null;
            try{
                URL url=new URL(strServiror+"login.php");
                JSONObject posdataParams=new JSONObject();
                posdataParams.put("nombre",musuario.getText().toString());
                posdataParams.put("password",password);
                Log.i(TAG, "doInBackground: click sendpost");

                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //milisegundos
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os= conn.getOutputStream();
                BufferedWriter writer=new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
                // writer.write(getPosData(posdataParams));
                writer.write("usuario="+musuario.getText().toString());
                writer.flush();
                os.close();

                int responseCode=conn.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb= new StringBuffer("");
                    String line="";

                    while((line=in.readLine())!=null){
                        sb.append(line);
                        break;
                    }
                    in.close();
                    result= sb.toString();
                }

            }catch (Exception e){
                result= new String("Exception: " + e.getMessage());

            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            try{
                String s = result.trim();
//                loadingDialog.dismiss();

                Log.i(TAG, "onPostExecute: ss  "+s);
                JSONObject jsobj=new JSONObject(s);
                String key= jsobj.getString("success");
                String datos=jsobj.getString("datos");

                JSONArray jadatos=new JSONArray(datos);
                String nombreusuario=jadatos.getJSONObject(0).getString("USU_NOMBRE");
                String user=jadatos.getJSONObject(0).getString("USU_CODIGO");
                Log.i(TAG, "onPostExecute: sresult  user  "+ user + "  nombreuser "+ nombreusuario);
                if(key.equalsIgnoreCase("true")){
                    ingresar(user,nombreusuario);
                    Toast.makeText(getApplicationContext(), "Acceso", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                    mEmailSignInButton.setEnabled(true);
                }
                dialog.dismiss();
           /* Toast.makeText(getApplication(), result,
                    Toast.LENGTH_LONG).show();*/
            }catch (Exception e){
                dialog.dismiss();
                Log.e(TAG, "onPostExecute: ", e );
                Toast.makeText(getApplication(), "Ocurrio un error en la conexión al servidor",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}
