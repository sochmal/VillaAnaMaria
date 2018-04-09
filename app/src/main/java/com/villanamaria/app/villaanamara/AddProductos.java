package com.villanamaria.app.villaanamara;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.villanamaria.app.villaanamara.clases.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.villanamaria.app.villaanamara.Adapters.*;
import com.villanamaria.app.villaanamara.data.contracts;
import com.villanamaria.app.villaanamara.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;

public class AddProductos extends AppCompatActivity {
    private productoAdapter madapter;
    private lineaAdapter madapterLinea;
    private sublineaAdapter madapterSLinea;
    private Toolbar toolbar;
    private ListView mlist,mlistLinea;
    private GridView glineas,gslineas;
    String Servidor, sfiltro, pfiltro="",enlace,senlace,penlace,codmesa,fpro_descripcion="",fpro_sublinea="";
    SharedPreferences spLogin;
    private AdapterProductoC mAdapter;
    private Button allproductos;
    private static final String TAG = "Productos";
    String[] abecedario = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    String canpersonas,codpedido,nombrepro,terminopro,m_cantidad="",m_posicion="",m_termino="",m_descripcion="",m_propedido="",m_costo="",m_observaciones="",m_orden;
    int cp=0;
    dbhandler mDbHelper;
    ArrayList<linea> lineas = new ArrayList<linea>();
    ArrayList<sublinea> slineas = new ArrayList<sublinea>();
    ArrayList<producto> productos = new ArrayList<producto>();
    private TextView editTextSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_add_productos);
            toolbar = (Toolbar) findViewById(R.id.toolbar_productos);
            setSupportActionBar(toolbar);

            canpersonas=getIntent().getStringExtra("numpersonas");
            codpedido=getIntent().getStringExtra("codpedido");
            codmesa=getIntent().getStringExtra("mesa");
            Log.i(TAG, "onCreate: codmesa "+codmesa);
            cp=Integer.parseInt(canpersonas);
            spLogin=getSharedPreferences("login",MODE_PRIVATE);
            Servidor =spLogin.getString("servidor",null);
            enlace=Servidor+"linea.php";
            senlace=Servidor+"listaSublinea.php";
            penlace=Servidor+"listaArticulo.php?SUBLI_CODIGO=";
            Log.i(TAG, "onCreate: enlace  "+enlace);
            mAdapter=new AdapterProductoC(this,null);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            glineas=(GridView)findViewById(R.id.gridLineas);
            allproductos=(Button) findViewById(R.id.btnAllProductos);
            gslineas=(GridView)findViewById(R.id.gridSLineas);
            editTextSearch=(TextView)findViewById(R.id.editTextSearch);
            if(width<=320){
                Log.i(TAG, "onCreate: Iphone 3gs 320px");
                glineas.setNumColumns(1);
            }else if(width==480 || width==540){
                Log.i(TAG, "onCreate: Iphone 3gs 480px");
                glineas.setNumColumns(2);
            }else if(width==600 || width==720 || width==750 || width==768 ) {
                Log.i(TAG, "onCreate:  540px");
                glineas.setNumColumns(2);
            }else if(width==800 || width==854 ){
                Log.i(TAG, "onCreate:  800px or 854px");
                glineas.setNumColumns(3);
            }else{
                gslineas.setNumColumns(4);
            }
            mlist=(ListView)findViewById(R.id.mlistproductos);
            new LineasLoadTask().execute();
            mDbHelper = new dbhandler(this);
            mlist.setScrollContainer(false);
            mlist.setClickable(true);
            mlist.setAdapter(mAdapter);
            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor citem =(Cursor) mAdapter.getItem(i);
                  //  producto selectpro=(producto) madapter.getItem(i);
                    //codproducto=citem.getString(citem.getColumnIndex(contracts.productoEntry.Particulo));
                    terminopro=citem.getString(citem.getColumnIndex(contracts.producto.pro_termino));
                    nombrepro=citem.getString(citem.getColumnIndex(contracts.producto.pro_descripcion));
                    m_propedido=citem.getString(citem.getColumnIndex(contracts.producto.pro_codigo));
                    m_costo=citem.getString(citem.getColumnIndex(contracts.producto.pro_precio));
                    Log.i(TAG, "onItemClick: termino prod "+terminopro);
                    ///showCantidadproducto();
                    ShowDialogcantidadObservaciones();
                }
            });
            glineas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //mesa selectmesa= (mesa)mAdaptar.getItem(position);

                    linea selectlinea=(linea) madapterLinea.getItem(i);
                        sfiltro=selectlinea.getCodigo();
                        Log.i(TAG, "onItemClick: linea "+ sfiltro);
                        new SLineasLoadTask().execute();
                }
            });

            gslineas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    sublinea selectslinea=(sublinea)madapterSLinea.getItem(i);
                    pfiltro=selectslinea.getCodigo();
                    Log.i(TAG, "onItemClick: sublinea "+ pfiltro);
                    new ProductosLoadTask().execute();
                    //new ProductosLoadTask().execute();
                }
            });
            allproductos.setOnClickListener(new
                                                    View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            pfiltro="";
                                                            new ProductosLoadTask().execute();
                                                        }
                                                    });
            new ProductosLoadTask().execute();
            editTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        Log.i(TAG, "onTextChanged: "  + s );
                        if(s.equals(null)){
                            Log.i(TAG, "onTextChanged: "  + s );
                           // new ProductoLoadTask().execute(SqlListarProductosLineas(mDbProducto.getclientesLineasCosto(cicliente)),"");

                            fpro_descripcion="";
                        }else{
                         //   new ProductoLoadTask().execute(SqlListarProductosLineas(mDbProducto.getclientesLineasCosto(cicliente)),s.toString());

                        }
                        fpro_descripcion=s.toString();
                            new ProductosLoadTask().execute();
                    }catch (Exception e){
                        Log.e(TAG, "onCreate: ",e );
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }catch (Exception e){
            Log.e(TAG, "onCreate: ", e);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuaddproductos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveP:
                // enviar();
                //ShowDialogSaveFinal();
                Intent m=new Intent(AddProductos.this,Pedido.class);
                m.putExtra("codpedido",codpedido);
                m.putExtra("mesa",codmesa);
                Log.i(TAG, "onOptionsItemSelected: codmesa "+codmesa);
                finish();
                startActivity(m);
                break;
            case R.id.action_cancelP:
                finish();
                break;
        }
        return true;
    }

//text_lineanombre
private class LineasLoadTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... params) {
        try{

            JsonArrayRequest jsonArrayReq = new JsonArrayRequest(enlace,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try
                            {
                                JSONObject obj=null;
                                Log.i(TAG, "onResponse generar datos: procesanado");
                                for (int a=0; a<response.length();a++) {
                                    obj = response.getJSONObject(a);
                                    Log.i(TAG, "onResponse: generar datos> "+obj.getString("LI_CODIGO" +
                                            ""));
                                    lineas.add(
                                            new linea(obj.getString("LI_CODIGO"),
                                                    obj.getString("LI_DESCRIPCION")));
                                }
                                Log.i(TAG, "onResponse: cantidad lineas "+ lineas.size());
                                ArrayList<linea> data =lineas;
                                madapterLinea=new lineaAdapter(AddProductos.this, data);
                                Log.i(TAG, "onResponse: cantidad mesas luego adapter "+data.size());
                                //mlistLinea.setAdapter(madapterLinea);
                                glineas.setAdapter(madapterLinea);
                                // return mesas;
                            }catch (JSONException e){
                                Log.e(TAG, "parseJsonCanales ",e );
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Log.i(TAG, "onErrorResponse canales: "+error.getMessage());
                }
            });
            // Adding request to request queue
            Volley.newRequestQueue(AddProductos.this).add(jsonArrayReq);
        }catch (Exception error){
            Log.i(TAG, "parseJsonCanales error: " + error.toString());
        }
        Log.i(TAG, "generarDatos: return lineas "+lineas.size());
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

    private class SLineasLoadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try{

                JsonArrayRequest jsonArrayReq = new JsonArrayRequest(
                        senlace+"?LI_CODIGO="+sfiltro,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try
                                {
                                    slineas.clear();
                                    JSONObject obj=null;
                                    Log.i(TAG, "onResponse generar datos: procesanado");
                                    for (int a=0; a<response.length();a++) {
                                        obj = response.getJSONObject(a);
                                        Log.i(TAG, "onResponse: generar datos> "+obj.getString("SUBLI_DESCRIPCION" +
                                                ""));
                                        slineas.add(
                                                new sublinea(obj.getString("SUBLI_CODIGO"),
                                                        obj.getString("SUBLI_DESCRIPCION")));
                                    }
                                    Log.i(TAG, "onResponse: cantidad lineas "+ lineas.size());

                                    ArrayList<sublinea> data =slineas;

                                    madapterSLinea=new sublineaAdapter(AddProductos.this, data);
                                    Log.i(TAG, "onResponse: cantidad mesas luego adapter "+data.size());
                                    //mlistLinea.setAdapter(madapterLinea);
                                    gslineas.setAdapter(madapterSLinea);
                                    // return mesas;
                                }catch (JSONException e){
                                    Log.e(TAG, "parseJsonCanales ",e );
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.i(TAG, "onErrorResponse canales: "+error.getMessage());
                    }
                });
                // Adding request to request queue
                Volley.newRequestQueue(AddProductos.this).add(jsonArrayReq);
            }catch (Exception error){
                Log.i(TAG, "parseJsonCanales error: " + error.toString());
            }
            Log.i(TAG, "generarDatos: return lineas "+lineas.size());
            return "Completo";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //     txt_facturas.setText("Espere Conectando");
        }
        @Override
        protected void onPostExecute(String s) {
        }
    }

    private class ProductosLoadTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            Log.i(TAG, "doInBackground: fpro " + fpro_descripcion+ "  filtro descripcion, filtro " + pfiltro);

            /*if(fpro_descripcion.equals("") || fpro_descripcion.length()<0){
                resultado= mDbHelper.getAllProductos("");
            }else if (fpro_descripcion.length()>0){
                resultado= mDbHelper.getAllProductosDescr(fpro_descripcion);
                Log.i(TAG, "doInBackground: fpro " + fpro_descripcion+ "  filtro descripcion ");
            } else if(pfiltro.length()>0){
                resultado=mDbHelper.getAllProductosDescrSublinea(fpro_descripcion,pfiltro);
                Log.i(TAG, "doInBackground: fpro " + fpro_descripcion+ "  filtro descripcion, filtro " + pfiltro);
            }*/
            return mDbHelper.getAllProductosDescrSublinea(fpro_descripcion,pfiltro);

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //     txt_facturas.setText("Espere Conectando");
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mAdapter.swapCursor(cursor);
                Log.i(TAG, "onPostExecute: " + cursor.getCount()+"");
            } else {
                Log.i(TAG, "onPostExecute: " + cursor.getCount()+"");
            }
        }
    }


    private void ShowDialogcantidadObservaciones(){
        try{

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductos.this,R.style.DialogTheme);
            LayoutInflater inflater =AddProductos.this.getLayoutInflater();
            View dialogview = inflater.inflate(R.layout.itemcanobser,null);
            dialogBuilder.setView(dialogview);

            final Button btnguardar=(Button)dialogview.findViewById(R.id.btn_canobpro);
            final EditText cantidad=(EditText)dialogview.findViewById(R.id.editT_canobspro);
            final EditText observaciones=(EditText)dialogview.findViewById(R.id.editT_obspro);

            final AlertDialog alertDialog = dialogBuilder.create();
            Window window = alertDialog.getWindow();

            btnguardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //alertDialog.hide();
                    //m_cantidad = input.getText().toString();
                    m_cantidad=cantidad.getText().toString();
                    m_observaciones=observaciones.getText().toString();
                    Log.i(TAG, "onClick: cantidad: "+m_cantidad +" , observaciones:"+m_observaciones);
                    String[] valuespos= new String[cp];
                    for(int a =0; a<cp; a++){
                        valuespos[a]=abecedario[a];
                    }
                    Log.i(TAG,
                            "onCreate: values pos "+ valuespos.toString());

                    for (int b=0; b<valuespos.length;b++){
                        Log.i(TAG, "onCreate: values "+ b +"   -  "+valuespos[b]
                        );
                    }
                    if(m_cantidad.length()<=0 || m_cantidad.equals("0")){
                        Toast.makeText(AddProductos.this,
                                "Es necesario ingresar una cantidad del producto", Toast.LENGTH_SHORT).show();
                                cantidad.findFocus();

                    }else {
                        alertDialog.hide();
                       // ShowCustomDialogwithList(valuespos);
                        m_posicion="A";
                        ShowCustomOrdenpedido();
                    }
                }
            });
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            alertDialog.show();

        }catch (Exception e){
            Log.e(TAG, "ShowDialogcantidadObservaciones: ",e );
        }
    }
    // Custom Dialog with List
    private void ShowCustomDialogwithList(String[] values) {
            try{
                String[] valuespos= new String[cp];
                for(int a =0; a<cp; a++){
                    valuespos[a]=abecedario[a];
                }
                Log.i(TAG,
                        "onCreate: values pos "+ valuespos.toString());

                for (int b=0; b<valuespos.length;b++){
                    Log.i(TAG, "onCreate: values "+ b +"   -  "+valuespos[b]
                    );
                }
                //ShowCustomDialogwithList(valuespos);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductos.this, R.style.DialogTheme);
                LayoutInflater inflater = AddProductos.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertpospersonar, null);
                dialogBuilder.setView(dialogView);

                final ListView listView = (ListView) dialogView.findViewById(R.id.listview);
                final TextView titulo=(TextView)dialogView.findViewById(R.id.title);
                titulo.setText("Seleccione la posición");
            // Defined Array values to show in ListView
                //String[] values = new String[20];


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, values);

                listView.setAdapter(adapter);
                final AlertDialog alertDialog = dialogBuilder.create();
                Window window = alertDialog.getWindow();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // ListView Clicked item index
                        int itemPosition     = position;
                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);
                        // Show Alert
                       /* Toast.makeText(getApplicationContext(),
                                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                                .show();*/
                        m_posicion=itemValue;
                        alertDialog.dismiss();
                        Log.i(TAG, "onItemClick: Itemvalue "+ m_posicion);
                        ShowCustomOrdenpedido();
                    }
                });

                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER); // set alert dialog in center
                // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom

                // Cancel Button
                Button cancel_btn = (Button) dialogView.findViewById(R.id.buttoncancellist);
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                    }
                });

                alertDialog.show();
            }catch (Exception e){
                Log.e(TAG, "ShowCustomDialogwithList: ",e );
            }

    }
    private void ShowCustomOrdenpedido() {
        try{
           //ShowCustomDialogwithList(valuespos);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductos.this, R.style.DialogTheme);
            LayoutInflater inflater = AddProductos.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alertpospersonar, null);
            dialogBuilder.setView(dialogView);

            final ListView listView = (ListView) dialogView.findViewById(R.id.listview);
            final TextView titulo=(TextView)dialogView.findViewById(R.id.title);
            titulo.setText("Seleccione la Orden del pedido");
            // Defined Array values to show in ListView
            //String[] values = new String[20];
            String[] ordenes = new String[] { "Bebida", "Primer Servicio", "Segundo Servicio", "Tercer Servicio", "Cuarto Servicio",
                    "Bajativo"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, ordenes);

            listView.setAdapter(adapter);
            final AlertDialog alertDialog = dialogBuilder.create();
            Window window = alertDialog.getWindow();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition     = position;
                    // ListView Clicked item value
                    String  itemValue    = (String) listView.getItemAtPosition(position);
                    // Show Alert
                       /* Toast.makeText(getApplicationContext(),
                                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                                .show();*/
                    m_orden=itemValue;
                       Log.i(TAG, "onItemClick: Item Orden Pedido "+m_orden);

                    alertDialog.dismiss();
                    Showterminoproducto();
                }
            });

            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom

            // Cancel Button
            Button cancel_btn = (Button) dialogView.findViewById(R.id.buttoncancellist);
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.hide();
                }
            });

            alertDialog.show();
        }catch (Exception e){
            Log.e(TAG, "ShowCustomDialogwithList: ",e );
        }

    }
    private void Showterminoproducto(){

        try {

            if(!terminopro.equals("NO APLICA")){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductos.this, R.style.DialogTheme);
                LayoutInflater inflater = AddProductos.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertpospersonar, null);
                dialogBuilder.setView(dialogView);

                final ListView listView = (ListView) dialogView.findViewById(R.id.listview);
                final TextView titulo=(TextView)dialogView.findViewById(R.id.title);
                titulo.setText("Seleccione el termino");
                // Defined Array values to show in ListView
                //String[] values = new String[20];

                String[] result = terminopro.split(",");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, result);

                listView.setAdapter(adapter);
                final AlertDialog alertDialog = dialogBuilder.create();
                Window window = alertDialog.getWindow();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // ListView Clicked item index
                        int itemPosition     = position;
                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);
                        // Show Alert
                        Toast.makeText(getApplicationContext(),
                                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                                .show();
                        m_termino=itemValue;
                        Log.i(TAG, "onItemClick: Termino producto "+m_termino );
                        alertDialog.dismiss();
                        ShowDialogSave();

                    }
                });

                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER); // set alert dialog in center
                // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom

                // Cancel Button
                Button cancel_btn = (Button) dialogView.findViewById(R.id.buttoncancellist);
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        ShowDialogSave();
                    }
                });

                alertDialog.show();
            }else{
                ShowDialogSave();
            }
        }catch (Exception e){
            Log.e(TAG, "Showterminoproducto: ", e);
        }
    }

    private void ShowDialogSave(){
        try {
            //alertguardarped
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductos.this,R.style.DialogTheme);
            LayoutInflater inflater =AddProductos.this.getLayoutInflater();
            View dialogview = inflater.inflate(R.layout.alertguardarped,null);
            dialogBuilder.setView(dialogview);

            final Button btnguardar=(Button)dialogview.findViewById(R.id.btn_save);
            final Button btncancelar=(Button)dialogview.findViewById(R.id.btn_cancel);
         /*    final EditText observaciones=(EditText)dialogview.findViewById(R.id.editT_obspro);*/
            final AlertDialog alertDialog = dialogBuilder.create();
            Window window = alertDialog.getWindow();
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: no guardar producto");
                    alertDialog.dismiss();
                }
            });
            btnguardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  long res=mDbHelper.nuevoDetPedido(new detpedido(
                            codpedido,"aa","2","1.2","","A","","sin sal"
                    ));*/
                    m_costo=m_costo.replace(",",".");
                    double stotal=Integer.parseInt(m_cantidad)*Double.parseDouble(m_costo);
                    /*
                    String codpedido, String propedido, String procantidad,
                    String prodescripcion, String procosto, String protermino, String proposicion, String proobservaciones, String pronombre, String subtotal,String proorden
                     */
                    String datosPedido="codpedido "+codpedido+" propedido "+m_propedido+" cantidad "+m_cantidad+" descripcion "+ m_descripcion+" costo "+m_costo+" termino "+m_termino+" posicion "+m_posicion+" observaciones "+m_observaciones+" nombrepro "+nombrepro+" subtotal "+
                    String.valueOf(stotal)+" orden "+m_orden;
                    Log.i(TAG, "onClick: saveproducto "+ datosPedido);
                    long res=mDbHelper.nuevoDetPedido(new detpedido(
                            codpedido,m_propedido,m_cantidad,nombrepro,m_costo,m_termino,m_posicion,m_observaciones,nombrepro,
                            String.valueOf(stotal),m_orden,"1"));
                    if(res>0){
                        alertDialog.dismiss();
                    }
                    Log.i(TAG, "onClick: res  "+res);
                    Log.i(TAG, "onClick: guardar producto");
                }
            });

            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // set alert dialog in center
            alertDialog.show();
        }catch (Exception e){
            Log.e(TAG, "ShowDialogSave: ",e );
        }
    }

}


