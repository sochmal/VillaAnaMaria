package com.villanamaria.app.villaanamara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.internal.Internal;
import com.villanamaria.app.villaanamara.Adapters.mesaAdapter;
import com.villanamaria.app.villaanamara.clases.*;
import com.villanamaria.app.villaanamara.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.SelectableChannel;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Mesas extends AppCompatActivity {
    private static final String TAG = "MEsas";
    GridView gridView;
    private mesaAdapter mAdaptar;
    SharedPreferences spLogin;
    String Servidor, filtro;
    String categoriaMesas;
    private dbhandler mdbdata;
    List<mesa> mesas = new ArrayList<mesa>();
    List<mesa> elementosm= new ArrayList<mesa>();
    List<String>posicionesele=new ArrayList<String>(10);
    TableLayout mtabla;
    int columnas   =5;
    Tabla tabla;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_mesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            mtabla=(TableLayout)findViewById(R.id.tabla);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            Log.i(TAG, "onCreate: w: "+width );
            int columnas=5,elementos=30;
            tabla = new Tabla(this, mtabla,width,columnas);
            //List<mesa> elementosm= new ArrayList<mesa>();


            spLogin=getSharedPreferences("login",MODE_PRIVATE);
            Servidor =spLogin.getString("servidor",null);
            Log.i(TAG, "onCreate: Servidor "+Servidor);
            filtro=getIntent().getStringExtra("filtro");
            categoriaMesas=Servidor+"listaMesa.php?ubicacion="+filtro;
            //categoriaMesas="http://tucuenca.com/anam/mesasgrill.php";
            Log.i(TAG, "onCreate: categoriamesa "+categoriaMesas);
            gridView = (GridView) findViewById(R.id.gridview1);
            dialog=new ProgressDialog(this);
            dialog.setTitle("Generando Mesas");
            dialog.setMessage("Espere por favor...");
            dialog.setCancelable(false);
            dialog.show();
            new MesasLoadTask().execute();
            mdbdata =new dbhandler(this);
            Log.i(TAG, "onCreate: mesas adapter"+ mesas.size() );
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
             mesa selectmesa= (mesa)mAdaptar.getItem(position);
                String mesaocpada= selectmesa.getEstado();
                String codmesa= selectmesa.getCodigo();
                String items= selectmesa.getItems();

                String ncliente= selectmesa.getNombrecli();
                String canper= selectmesa.getNpersonas();
                Log.i(TAG, "onItemClick: numpersonas " +  canper);
                String cicli= selectmesa.getCicli();
                Log.i(TAG, "onItemClick: " + selectmesa.getCodigo());

                       Intent m=new Intent(Mesas.this,Pedido.class);
                       String codpedido=cod_transaccion();
                       String fecha=fecha_actual();

                        m.putExtra("mesa", codmesa );
                        m.putExtra("codpedido", codpedido);
                        m.putExtra("fecha", fecha  );
                        m.putExtra("estado",mesaocpada);
                        m.putExtra("items",items);
                        m.putExtra("nombrecli",ncliente);
                        m.putExtra("canper",canper);
                        m.putExtra("cicli",cicli);

                        Log.i(TAG, "onItemClick: mesaocupada "+mesaocpada);
                        if(mesaocpada.equals("LIBRE")){
                            mdbdata.nuevoCabPedido(new cabpedido(codpedido,"9999999999","Consumidor Final",fecha,"", codmesa,"1" ));
                        }else if(mesaocpada.equals("OCUPADO")){
                            mdbdata.nuevoCabPedido(new cabpedido(codpedido,cicli,ncliente,fecha,"", codmesa,canper ));
                        }
                        finish();
                       startActivity(m);
                      /* if(selectmesa.getEstado().equals("LIBRE")){

                       }else{
                           Toast.makeText(Mesas.this,
                                   "Mesa en estado Ocupada", Toast.LENGTH_SHORT).show();
                       /*/
            }
        });
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
    }
    public String fecha_actual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        return dateString;
    }
    public String cod_transaccion(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar calendario= Calendar.getInstance();
        return sdf.format(calendario.getTime());
    }

    private class MesasLoadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try{
                JsonArrayRequest jsonArrayReq = new JsonArrayRequest(categoriaMesas,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try
                                {

                                    JSONObject obj=null;
                                    Log.i(TAG, "onResponse generar datos: procesanado");
                                    int totalelementos=response.length();
                                    obj = response.getJSONObject(0);
                                    String[] tamm = obj.getString("MES_TAMANO").split("/");
                                    Log.i(TAG, "onResponse: MATRIZ fila "+tamm[0] + " colum"+tamm[1]);
                                    gridView.setNumColumns(Integer.parseInt(tamm[1]));
                                    int dimenArray=Integer.parseInt(tamm[0])*Integer.parseInt(tamm[1]);
                                    Log.i(TAG, "onResponse: dimenArray " + dimenArray);
                                    List<mesa> ArrayMesaf=new ArrayList<mesa>(60);
                                    for(int j=0;j<dimenArray;j++){
                                        ArrayMesaf.add(
                                                new mesa(
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "0",
                                                        ""
                                                ));
                                    }
                                    int filas = Integer.parseInt(tamm[0]);
                                    int columnas = Integer.parseInt(tamm[1]);
                                    String inv="0";
                                    for (int a=0; a<totalelementos;a++) {
                                       // for(int j=0; j<columnas;j++){
                                            if(a==5){
                                                inv="1";
                                            }else{
                                                inv="0";
                                            }
                                                obj = response.getJSONObject(a);
                                                //Log.i(TAG, "onResponse: mes_fc datos> "+obj.getString("MES_FC"));
                                                String nombre=obj.getString("MES_CODIGO");
                                                //String fc=nombre.substring(1,nombre.length());
                                                String[] posm = obj.getString("MES_FC").split("/");

                                                Log.i(TAG, "onResponse: fila "+posm[0] + " colum"+posm[1]);

                                                //Log.i(TAG, "onResponse: filacol : "+fc);
                                                int posMesaarray=(Integer.parseInt(tamm[1])*Integer.parseInt(posm[1]))+ Integer.parseInt(posm[0]);
                                        Log.i(TAG, "onResponse: calposi codigo="+obj.getString("MES_NOMBRE")+ "   ("+ tamm[1]+" * "+posm[0]+") + "+ posm[1]+ "="+posMesaarray);
                                                posMesaarray--;
                                        Log.i(TAG, "onResponse: arraymesaF "+ ArrayMesaf.size());

                                                Log.i(TAG, "onResponse: posMesaArray "+ posMesaarray);
                                        //Log.i(TAG, "onResponse: numero personas "+obj.getString("PEC_NUMERO_PERSONAS") );
                                                mesas.add(
                                                        new mesa(nombre,
                                                                obj.getString("PEC_NUMERO_PERSONAS"),
                                                                obj.getString("MES_NOMBRE"),
                                                                obj.getString("MES_DESCRIPCION"),
                                                                obj.getString("PEC_ARTICULO"),
                                                                obj.getString("ESTADOMESA"),
                                                                obj.getString("USU_NOMBRE"),
                                                                obj.getString("PEC_CEDULA_RUC"),
                                                                inv,
                                                                String.valueOf(posMesaarray)));
                                    }
                                    for(int i=0;i<mesas.size();i++){
                                        int posi=Integer.parseInt( mesas.get(i).getPosicion());
                                        String nombremesa=mesas.get(i).getNombre();
                                        ArrayMesaf.set(posi,
                                                new mesa(
                                                        mesas.get(i).getCodigo(),
                                                        mesas.get(i).getNpersonas(),
                                                        nombremesa,
                                                        mesas.get(i).getDescripcion(),
                                                        mesas.get(i).getItems(),
                                                        mesas.get(i).getEstado(),
                                                        mesas.get(i).getNombrecli(),
                                                        mesas.get(i).getCicli(),
                                                        "0",
                                                        String.valueOf(posi)
                                                )
                                        );
                                        posicionesele.add(String.valueOf(posi));
                                        Log.i(TAG, "onResponse: posimfinal "+i +"="+posi + "size mesa "+ mesas.size()+" posiciones "+ posicionesele.size()+ "mesa nombre "+nombremesa);
                                    }
                                    for(int k=0;k<ArrayMesaf.size();k++){
                                        Log.i(TAG, "onResponse arrayfinal: "+ArrayMesaf.get(k).getCodigo());
                                        String codigo=ArrayMesaf.get(k).getEstado();
                                        if(codigo.length()<=0){
                                            Log.i(TAG, "onResponse:  arrayfinal seoculta");
                                        }else{
                                            Log.i(TAG, "onResponse:  arrayfinal noseoculta");
                                        }
                                    }
                                    dialog.dismiss();
                                    Log.i(TAG, "onResponse: cantidad mesas "+ mesas.size());
                                    List<mesa> data =ArrayMesaf;
                                   // Collections.sort(data);
                                    mAdaptar=new mesaAdapter(Mesas.this, R.layout.itemmesa,data);
                                    Log.i(TAG, "onResponse: cantidad mesas luego adapter "+data.size());
                                    gridView.setAdapter(mAdaptar);
                                    // return mesas;
                                }catch (JSONException e){
                                    Log.e(TAG, "parseJsonMesas ",e );
                                }catch (Exception e){
                                    Log.e(TAG, "onResponse: expcion",e );
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.i(TAG, "onErrorResponse Mesas: "+error.getMessage());
                    }
                });
                // Adding request to request queue
                Volley.newRequestQueue(Mesas.this).add(jsonArrayReq);
            }catch (Exception error){
                Log.i(TAG, "parseJsonMesas error: " + error.toString());
            }
            Log.i(TAG, "generarDatos: return mesas "+mesas.size());
            return "Completo";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
       //     txt_facturas.setText("Espere Conectando");
        }
        @Override
        protected void onPostExecute(String s) {


     //       txt_facturas.setText("Sincronizado");
            // SyncReceive(posSyncRe);
        }
    }
}
