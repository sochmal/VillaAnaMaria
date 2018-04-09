package com.villanamaria.app.villaanamara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.villanamaria.app.villaanamara.data.dbhandler;

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
import java.util.Iterator;

public class nuevoClienet extends AppCompatActivity {
    private EditText nc_ci,nc_nombres,nc_direccion,nc_telefono,nc_email;
    private Button btnGuardar, btnCancelar;
    SharedPreferences spLogin;
    String servidor,codpedido,usuario;
    private static final String TAG = "NuevoCliente";
    ProgressDialog dialogbtn;
    dbhandler mdbdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_clienet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewCliente);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spLogin=getSharedPreferences("login",MODE_PRIVATE);
        servidor =spLogin.getString("servidor",null);
        usuario =spLogin.getString("username",null);
        codpedido=getIntent().getStringExtra("codpedido");

        mdbdata=new dbhandler(this);
        nc_ci=(EditText)findViewById(R.id.editT_ciruc);
        nc_nombres=(EditText)findViewById(R.id.editT_nombres);
        nc_direccion=(EditText)findViewById(R.id.editT_direccion);
        nc_telefono=(EditText)findViewById(R.id.editT_telefono);
        nc_email=(EditText)findViewById(R.id.editT_email);

        btnCancelar=(Button) findViewById(R.id.btn_nc_cancelar);
        btnGuardar=(Button) findViewById(R.id.btn_nc_guardar);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nc_nombres.length()<=0){
                    showSuccessMessage("Ingrese Nombres");
                }else if(nc_ci.length()<=0){
                    showSuccessMessage("Ingrese Cedula / RUC");
                }else if(nc_direccion.length()<=0){
                    showSuccessMessage("Ingrese direccion");
                }else if(nc_telefono.length()<=0){
                    showSuccessMessage("Ingrese telefono");
                }else if(nc_email.length()<=0){
                    showSuccessMessage("Ingrese email");
                }else{
                    dialogbtn=new ProgressDialog(nuevoClienet.this);
                    dialogbtn.setTitle("Sincronizando... Espere");
                    dialogbtn.setMessage("Procesando...");
                    dialogbtn.show();
                    dialogbtn.setCancelable(false);
                  String res= Enviar(usuario);
                    new SendPostRequest().execute();
                    showSuccessMessage("Enviando");
                  /*if(res.equals("d")){

                      mdbdata.UpdateCabPedidoCliente(codpedido,nc_ci.getText().toString(),nc_nombres.getText().toString());
                      dialogbtn.hide();
                  }*/

                   /* Intent m=new Intent(nuevoClienet.this,Pedido.class);
                    m.putExtra("codpedido",codpedido);
                    startActivity(m);
                    finish();*/
                }
            }
        });
    }

    private String Enviar(String user){
        String result="";
        try{
            URL url=new URL(servidor+"clientenuevo.php");
            Log.i(TAG, "Enviar: servidor "+servidor+"clientenuevo.php");

           /* JSONObject posdataParams=new JSONObject();
            posdataParams.put("nombre",nc_nombres.getText().toString());
            posdataParams.put("ci",nc_ci.getText().toString());
            posdataParams.put("direccion",nc_direccion.getText().toString());
            posdataParams.put("telefono",nc_telefono.getText().toString());
            posdataParams.put("email",nc_email.getText().toString());
*/
            //Log.e(TAG, "doInBackground: ",posdataParams.toString());
int a=0;
if(a==0){

}
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
            //writer.write(getPosData(posdataParams));
            writer.write("usuario="+user+"&nombre="+nc_nombres.getText().toString()+"&ci="+nc_ci.getText().toString()+
                    "&direccion="+nc_direccion.getText().toString()+"&telefono="+nc_telefono+"&email="+nc_email);
            Log.i(TAG, "Enviar: cadeno envio "+"nombre="+nc_nombres.getText().toString()+"&ci="+nc_ci.getText().toString()+
                    "&direccion="+nc_direccion.getText().toString()+"&telefono="+nc_telefono+"&email="+nc_email);
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
                String s=result.trim();
                JSONObject jsobj=new JSONObject(result);
                String key=jsobj.getString("key");
                String error=jsobj.getString("error");
                Log.i(TAG, "Enviar: ker "+key + " error "+error);
                if(key.equals("true")){
                    Log.i(TAG, "Enviar: Cliente registrado");
                }else{
                    Log.i(TAG, "Enviar: cliente no registrado");
                }
                /*modificar dependiendo el envio


                */
                Log.i(TAG, "doInBackground: "+result);
                dialogbtn.hide();
            }
        }catch (Exception e){
            Log.e(TAG, "Enviar: ", e);
            result= new String("Exception: " + e.getMessage());
        }
        return result;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void OnPreExecute(){
        }
        @Override
        protected String doInBackground(String... params) {
            String result=null;
            try{
                URL url=new URL(servidor+"clientenuevo.php");

                /*JSONObject posdataParams=new JSONObject();
                posdataParams.put("nombre",username);
                posdataParams.put("password",password);*/
                //Log.e(TAG, "doInBackground: ",posdataParams.toString());

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
                writer.write("usuario="+usuario+"&nombre="+nc_nombres.getText().toString()+"&ci="+nc_ci.getText().toString()+
                        "&direccion="+nc_direccion.getText().toString()+"&telefono="+nc_telefono.getText().toString()+"&email="+nc_email.getText().toString());
                Log.i(TAG, "Enviar: cadeno envio "+"nombre="+nc_nombres.getText().toString()+"&ci="+nc_ci.getText().toString()+
                        "&direccion="+nc_direccion.getText().toString()+"&telefono="+nc_telefono+"&email="+nc_email);
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
                    Log.i(TAG, "doInBackground: "+result);
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
                dialogbtn.hide();
                //JSONArray   jsresponse=new JSONArray(result);
                JSONObject jsobj=new JSONObject(result);
                //JSONArray jsalogin=jsobj.getJSONArray("datalogin");
                Log.i(TAG, "onPostExecute: "+jsobj.length());
                String key= jsobj.getString("key");
                Log.i(TAG, "onPostExecute: key "+key);
                if(key.equalsIgnoreCase("true")){
                    //asignarusuario(username,jsobj.getString("objdiario"),jsobj.getString("tipo"));
                    Toast.makeText(getApplication(), "Acceso Exitoso", Toast.LENGTH_LONG).show();
                    regresar();
                    //     finish();
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
    public void regresar(){
        mdbdata.UpdateCabPedidoCliente(codpedido,nc_ci.getText().toString(),nc_nombres.getText().toString());
        Intent m=new Intent(nuevoClienet.this,Pedido.class);
        m.putExtra("codpedido",codpedido);
        startActivity(m);
        finish();
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
        return  result.toString();
    }

    private void showSuccessMessage(String mensaje) {
        Toast.makeText(this,
                mensaje, Toast.LENGTH_SHORT).show();
    }
}
