package com.villanamaria.app.villaanamara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.villanamaria.app.villaanamara.Adapters.mesaAdapter;
import com.villanamaria.app.villaanamara.Adapters.propedidoAdapter;
import com.villanamaria.app.villaanamara.clases.detpedido;
import com.villanamaria.app.villaanamara.clases.mesa;
import com.villanamaria.app.villaanamara.data.contracts;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Pedido extends AppCompatActivity {
    private Button btnAddPro,btnSearchCliente,btnNewCliente;
    private String usuario,servidor,codmesa,codpedido,fecha,procodigo="";
    private String pronombre,jsonproductos="",idproducto="",urlmesaocupada,mesaocupada="",productospedidos="";
    private String items="",nombrecli="",canper="",cicli="";
    private TextView lv_codMesa,cicliente,dircliente,nomcliente,tvstotal;
    private EditText numpersonas,observaciones;
    private static final String TAG = "Pedido";
    private propedidoAdapter mAdapter;
    private ListView mlist;
    private dbhandler mDbHelper;
    JSONArray jarrproductos= new JSONArray();
    SharedPreferences spLogin;
    ProgressDialog dialogSend;
    JSONObject json_items= null;
    private JSONArray a_json_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spLogin=getSharedPreferences("login",MODE_PRIVATE);
        servidor =spLogin.getString("servidor",null);
        usuario =spLogin.getString("usuario",null);
        btnAddPro=(Button)findViewById(R.id.btnAddproducto);
        btnSearchCliente=(Button)findViewById(R.id.btnBuscarCliente);
        btnNewCliente=(Button)findViewById(R.id.btnNewCliente);
        lv_codMesa=(TextView) findViewById(R.id.textV_codmesa);
        numpersonas =(EditText)findViewById(R.id.editT_numPersonas);


        mlist =(ListView) findViewById(R.id.listproped);
        cicliente=(TextView)findViewById(R.id.textV_ciCliente);
        dircliente=(TextView)findViewById(R.id.textV_dirCliente);
        nomcliente=(TextView)findViewById(R.id.textVnomcliente);
        tvstotal=(TextView)findViewById(R.id.textV_stotal);


        mAdapter=new propedidoAdapter(this,null);
        codmesa=getIntent().getStringExtra("mesa");
        codpedido=getIntent().getStringExtra("codpedido");
        mesaocupada=getIntent().getStringExtra("estado");
        items=getIntent().getStringExtra("items");
        fecha=getIntent().getStringExtra("fecha");
        nombrecli=getIntent().getStringExtra("nombrecli");
        canper=getIntent().getStringExtra("canper");
        cicli=getIntent().getStringExtra("cicli");
        numpersonas.setText(" nada  "+canper);
        mDbHelper = new dbhandler(this);
        Log.i(TAG, "onCreate: codpedido"+codpedido + " codmesa "+codmesa);
        mlist.setAdapter(mAdapter);
            try {
                if(mesaocupada.equals("LIBRE")){
                    Log.i(TAG, "onCreate: libre");
                }else{
                    Log.i(TAG, "onCreate: ocupada");
                    urlmesaocupada="";
                    btnSearchCliente.setEnabled(false);
                    Log.i(TAG, "onCreate: numpersonas  "+canper);

                    nomcliente.setText(nombrecli);
                    cicliente.setText(cicli);
                    if(items.length()>0){
                    //    json_items=new JSONObject(items);
                        a_json_items=new JSONArray(items);
                        cargar_productos_pedidos(a_json_items);
                    }else{
                        Log.i(TAG, "onCreate: no se pudo cargar los items del pedido");
                    }
                    //MesaOcupadaLoadTask();
                }
            }catch (Exception e){
                Log.e(TAG, "onCreate: mesaocupada", e);
            }


        lv_codMesa.setText(canper);
       // mDbHelper = new dbhandler(this);

        btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numpersonas.length()>0){
                    int rup=mDbHelper.UpdateCabPedido(codpedido,cicliente.getText().toString(),numpersonas.getText().toString(),nomcliente.getText().toString());
                   Intent m=new Intent(Pedido.this,AddProductos.class);
                    m.putExtra("numpersonas",numpersonas.getText().toString());
                    m.putExtra("codpedido",codpedido);
                    m.putExtra("mesa",lv_codMesa.getText().toString());
                    Log.i(TAG, "onClick: mesa "+codmesa);
                    finish();
                    startActivity(m);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Ingrese cantidad de personas", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnSearchCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rup=mDbHelper.UpdateCabPedido(codpedido,cicliente.getText().toString(),numpersonas.getText().toString(),nomcliente.getText().toString());
                Intent m=new Intent(Pedido.this,Clientes.class);
                m.putExtra("codpedido",codpedido);
                m.putExtra("mesa",lv_codMesa.getText().toString());
                finish();
                startActivity(m);
            }
        });

        btnNewCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m=new Intent(Pedido.this, nuevoClienet.class);
                m.putExtra("codpedido",codpedido);
                finish();
                startActivity(m);
            }
        });

        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor citem =(Cursor) mAdapter.getItem(i);
                procodigo=citem.getString(citem.getColumnIndex(contracts.detpedido.det_codproducto));
                idproducto=citem.getString(citem.getColumnIndex(contracts.detpedido.det_id));
                String modificarpro=citem.getString(citem.getColumnIndex(contracts.detpedido.det_promodificar));
                Log.i(TAG, "onItemClick: procodigo "+procodigo +" id " +idproducto);
                if(modificarpro.equals("1")){
                    showOpcionespproducto();
                }else if(modificarpro.equals( "2")){
                    Log.i(TAG, "onItemClick: no se puede modificar el producto");
                }
                new ProductoPedidoLoadTask().execute();
            }
        });
        new CabPedidoLoadTask().execute();
        //new ProductoPedidoLoadTask().execute();
    }

    public void cargar_productos_pedidos(JSONArray jsitems) throws JSONException {
        try{
        for(int a=0; a<jsitems.length();a++){
            //Log.i(TAG, "cargar_productos_pedidos: descripcion "+  jsitems.getJSONObject(a).getString("Descripcion"));
            //jsobj.getString("key");
            /*
             long res=mDbHelper.nuevoDetPedido(new detpedido(
                            codpedido,m_propedido,m_cantidad,m_descripcion,m_costo,m_termino,m_posicion,m_observaciones,nombrepro,
                            String.valueOf(stotal),m_orden,"1"));
             */
            long resul=mDbHelper.nuevoDetPedido(new detpedido(
                    /*codpedido*/
                    codpedido,
                    /* propedido*/
                    "",
                    /* procantidad*/
                    jsitems.getJSONObject(a).getString("cantidad"),
                    /* prodescripcion*/
                    jsitems.getJSONObject(a).getString("Descripcion"),
                    /* procosto*/
                    "",
                    /* protermino*/
                    jsitems.getJSONObject(a).getString("termino"),
                    /* proposcion*/
                    jsitems.getJSONObject(a).getString("posicion"),
                    /* proobservaciones*/
                    "",
                    /* pronombre*/
                    "",
                    /* subtotal*/
                    "",
                    /* proorden*/
                    "",
                    /* promodificar*/
                    "2"
            ));
            // obj_comdet.getJSONObject(b).getString("PRO_CODIGO")
        }
        }catch (JSONException e){
            Log.e(TAG, "cargar_productos_pedidos: ",e );
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
               // enviar();
                ShowDialogSaveFinal();
                break;
            case R.id.action_cancel:
                ShowDialogCancelarFinal();
                break;
            case R.id.homeAsUp:
               ShowDialogCancelarFinal();
                break;
        }
        return true;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void OnPreExecute(){
        }
        @Override
        protected String doInBackground(String... params) {
            String result=null;
            //getproductos("");
            try{
                Log.i(TAG, "doInBackground: "+servidor+"guardarOrden.php");
                URL url=new URL(servidor+"guardarOrden.php");
                //URL url=new URL("http://tucuenca.com/anam/guardarOrden.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                getproductos(usuario,codpedido,lv_codMesa.getText().toString(),cicliente.getText().toString(),numpersonas.getText().toString());
                Log.i(TAG, "doInBackground: productospedido "+productospedidos);
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                OutputStream os= conn.getOutputStream();
                BufferedWriter writer=new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
                writer.write("productospedido="+productospedidos);
                writer.flush();
                writer.close();
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
                    Log.i(TAG, "doInBackground: resultado"+result);
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
                // loadingDialog.dismiss();
               // dialogbtn.hide();
                //JSONArray   jsresponse=new JSONArray(result);
                JSONObject jsobj=new JSONObject(result);
                //JSONArray jsalogin=jsobj.getJSONArray("datalogin");
                Log.i(TAG, "onPostExecute: "+jsobj.length());
                String key= jsobj.getString("key");
                Log.i(TAG, "onPostExecute: key "+key);
                if(key.equalsIgnoreCase("true")){
                    //asignarusuario(username,jsobj.getString("objdiario"),jsobj.getString("tipo"));
                    dialogSend.dismiss();
                    Toast.makeText(getApplication(), "Acceso Exitoso", Toast.LENGTH_LONG).show();
                    Intent m=new Intent(Pedido.this,MenuLugares.class);
                    finish();
                    startActivity(m);
                }else {
                    Toast.makeText(getApplication(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                Log.e(TAG, "onPostExecute: ", e);
                Toast.makeText(getApplication(), "ERROR: No se pudo conectarse al servidor", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.e(TAG, "onPostExecute: ",e );
            }
           /* Toast.makeText(getApplication(), result,
                    Toast.LENGTH_LONG).show();*/
        }
    }
    public String getPosData(JSONObject params) throws Exception{
        StringBuilder result = new StringBuilder();
        boolean first=true;
        Iterator<String> itr=params.keys();
        while(itr.hasNext()){
            String key=itr.next();
            Object value=params.get(key);
            if(first)
                first=false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }
        Log.i(TAG, "getPosData: result "+result.toString());
        return  result.toString();
    }

    private void showSuccessMessage(String mensaje) {
        Toast.makeText(this,
                mensaje, Toast.LENGTH_SHORT).show();
    }
    public void getproductos(String user, String codpedido, String codmesa, String codcliente, String canpersonas){
        //Cursor cur=mDbHelper.getcoddetpedido(codpedido);
        Cursor cur=mDbHelper.getCoddetPedidoestado(codpedido);
        JSONObject objectcabpedido;
        productospedidos="";
        try {
            if(cur.getCount()>0){
                cur.moveToFirst();
                do{
                    productospedidos=productospedidos+"|"+user+ "|"+codpedido+"|"+codmesa+"|"+codcliente+"|"+ canpersonas+
                            "|"+ cur.getString(cur.getColumnIndex(contracts.detpedido.det_codproducto))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_descripcion))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_cantidad))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_observaciones))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_orden))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_posicion))+
                            "|"+cur.getString(cur.getColumnIndex(contracts.detpedido.det_termino))+"$*$";
                }while (cur.moveToNext());
                cur.close();
                Log.i(TAG, "pedidos: obj ,"+jarrproductos.toString());
            }
        }
        catch (Exception e){
            Log.e(TAG, "enviar: ",e );
        }
    }
    public void showOpcionespproducto(){
        //btn_modificar
        //btn_delete
        //btn_regresar
        //textV_nombreProducto

        try {
            //alertguardarped
            int j=0;
            if(j==2){
            }
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Pedido.this,R.style.DialogTheme);
            LayoutInflater inflater =Pedido.this.getLayoutInflater();
            View dialogview = inflater.inflate(R.layout.alertborrarpro,null);
            dialogBuilder.setView(dialogview);

            final Button btnmodificar=(Button)dialogview.findViewById(R.id.btn_modificar);
            final Button btneliminar=(Button)dialogview.findViewById(R.id.btn_delete);
            final Button btncancelar=(Button)dialogview.findViewById(R.id.btn_regresar);
         /* final EditText observaciones=(EditText)dialogview.findViewById(R.id.editT_obspro);*/
            final AlertDialog alertDialog = dialogBuilder.create();
            Window window = alertDialog.getWindow();

            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: no guardar producto");
                    new ProductoPedidoLoadTask().execute();
                    alertDialog.dismiss();
                }
            });
            btneliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long res=mDbHelper.DeleteProPedido(codpedido,procodigo,idproducto);
                    Log.i(TAG, "onClick: resdelete "+res);
                    if(res>0){
                        alertDialog.dismiss();
                        new ProductoPedidoLoadTask().execute();
                        Log.i(TAG, "onClick: Borramos  "+res);
                    }else{
                        new ProductoPedidoLoadTask().execute();
                    }
                    Log.i(TAG, "onClick: guardar producto");
                }
            });
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            alertDialog.show();
        }catch (Exception e){
            Log.e(TAG, "ShowDialogSave: ",e );
            new ProductoPedidoLoadTask().execute();
        }
    }

    private void ShowDialogSaveFinal(){
        try {
            //alertguardarped
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Pedido.this,R.style.DialogTheme);
            LayoutInflater inflater =Pedido.this.getLayoutInflater();
            View dialogview = inflater.inflate(R.layout.alertguardarped,null);
            dialogBuilder.setView(dialogview);

            final Button btnguardar=(Button)dialogview.findViewById(R.id.btn_save);
            final Button btncancelar=(Button)dialogview.findViewById(R.id.btn_cancel);
            final TextView TVtitulo=(TextView)dialogview.findViewById(R.id.textViewPed);
            final AlertDialog alertDialog = dialogBuilder.create();
            TVtitulo.setText("Desea enviar el pedido a cocina");
            Window window = alertDialog.getWindow();
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: no guardar pedido");
                    alertDialog.dismiss();
                }
            });
            btnguardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    dialogSend=new ProgressDialog(Pedido.this);
                    dialogSend.setTitle("Generando Mesas");
                    dialogSend.setMessage("Espere por favor...");
                    dialogSend.setCancelable(false);
                    dialogSend.show();
                    new SendPostRequest().execute();
                    Log.i(TAG, "onClick: guardar pedido");
                }
            });
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            alertDialog.show();
        }catch (Exception e){
            Log.e(TAG, "ShowDialogSave: ",e );
        }
    }
    private void ShowDialogCancelarFinal(){
        try {
            //alertguardarped
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Pedido.this,R.style.DialogTheme);
            LayoutInflater inflater =Pedido.this.getLayoutInflater();
            View dialogview = inflater.inflate(R.layout.alertguardarped,null);
            dialogBuilder.setView(dialogview);

            final Button btnguardar=(Button)dialogview.findViewById(R.id.btn_save);
            final Button btncancelar=(Button)dialogview.findViewById(R.id.btn_cancel);
            final TextView TVtitulo=(TextView)dialogview.findViewById(R.id.textViewPed);
            final AlertDialog alertDialog = dialogBuilder.create();
            TVtitulo.setText("Desea CANCELAR el pedido a cocina");
            Window window = alertDialog.getWindow();
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: no guardar pedido");
                    alertDialog.dismiss();
                }
            });
            btnguardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    Intent m=new Intent(Pedido.this, MenuLugares.class);
                    startActivity(m);
                    Log.i(TAG, "onClick: guardar pedido");
                }
            });
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            alertDialog.show();
        }catch (Exception e){
            Log.e(TAG, "ShowDialogSave: ",e );
        }
    }
    private class CabPedidoLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Log.i("loadProductos", "codCabecera "+codpedido);
            return  mDbHelper.getcodcabpedido(codpedido);
            //return mDbHelper.getcoddetpedido(codcabecera);

            //return mDbHelper.getAllpedidoProductos();
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            Log.i(TAG, "onPostExecute: CabPedidoLoadTask "+cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
               // mAdapter.swapCursor(cursor);
                    cursor.moveToFirst();
                    numpersonas.setText(cursor.getString(cursor.getColumnIndex(contracts.cabpedido.cab_canpersonar)));
                    cicliente.setText(cursor.getString(cursor.getColumnIndex(contracts.cabpedido.cab_cliente)));
                    nomcliente.setText(cursor.getString(cursor.getColumnIndex(contracts.cabpedido.cab_nomcliente)));
                    lv_codMesa.setText(cursor.getString(cursor.getColumnIndex(contracts.cabpedido.cab_codmesa)));
                //  showSuccessMessage("can: "+cursor.getCount());
            } else {
                //  showSuccessMessage("Vacio");
            }
            totalPedido();
            new ProductoPedidoLoadTask().execute();
        }
    }
    private class ProductoPedidoLoadTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            Log.i("loadProductos", "codCabecera "+codpedido);
            return  mDbHelper.getcoddetpedido(codpedido);
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            Log.i(TAG, "onPostExecute: cantidadProductosPedidos "+cursor.getCount());
            if (cursor != null && cursor.getCount() >= 0) {
                mAdapter.swapCursor(cursor);
                //  showSuccessMessage("can: "+cursor.getCount());
            } else {
                //  showSuccessMessage("Vacio");
            }
            //Log.i(TAG, "onPostExecute: Cantidadproductospedidos "+cursor.getCount());
        }
    }
    private void totalPedido(){
        try{
            Cursor r= mDbHelper.gettotalPedido(codpedido);
            Double sum, stotal,iva;
            if (r.moveToFirst()){
                sum=r.getDouble(0);
                iva=0.12*sum;
                stotal=iva+sum;
                tvstotal.setText(String.valueOf(sum));
                Log.i(TAG, "totalPedido: "+sum);
            }
        }catch (Exception e){
            Log.e(TAG, "totalPedido: ",e );
        }
    }


}
