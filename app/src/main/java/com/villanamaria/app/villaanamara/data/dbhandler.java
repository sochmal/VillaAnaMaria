package com.villanamaria.app.villaanamara.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.villanamaria.app.villaanamara.clases.*;

import static android.content.ContentValues.TAG;

/**
 * Created by Christian on 18/12/2017.
 */

public class dbhandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "villa.db";
    Cursor cur_resultado;
    long resul=0;
    private static final String table_cabPedido = "CREATE TABLE IF NOT EXISTS " +
            contracts.cabpedido.Table_cab + " ( " +
            contracts.cabpedido.cab_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            contracts.cabpedido.cab_codigoped + " TEXT, " +
            contracts.cabpedido.cab_fecha + " TEXT, " +
            contracts.cabpedido.cab_cliente + " TEXT, " +
            contracts.cabpedido.cab_nomcliente + " TEXT, " +
            contracts.cabpedido.cab_canpersonar + " TEXT, " +
            contracts.cabpedido.cab_codmesa + " TEXT, " +
            contracts.cabpedido.cab_observaciones +" TEXT )" ;
/*
 public static abstract  class  producto implements BaseColumns{
        public static final String Table_pro="producto";
        public static final String pro_id="_id";
        public static final String pro_codigoped="codigo";
        public static final String pro_descripcion="descripcion";
        public static final String pro_precio="precio";
        public static final String pro_termino="termino";
        public static final String pro_sublinea="sublinea";
    }
 */
private static final String table_clientes = "CREATE TABLE IF NOT EXISTS " +
        contracts.cliente.Table_cli + " ( " +
        contracts.cliente.cli_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        contracts.cliente.cli_ci + " TEXT, " +
        contracts.cliente.cli_nombre + " TEXT, " +
        contracts.cliente.cli_direccion + " TEXT, " +
        contracts.cliente.cli_telefono + " TEXT, " +
        contracts.cliente.cli_email +" TEXT )";
private static final String table_producto = "CREATE TABLE IF NOT EXISTS " +
        contracts.producto.Table_pro + " ( " +
        contracts.producto.pro_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        contracts.producto.pro_codigo + " TEXT, " +
        contracts.producto.pro_descripcion + " TEXT, " +
        contracts.producto.pro_precio + " TEXT, " +
        contracts.producto.pro_termino + " TEXT, " +
        contracts.producto.pro_sublinea +" TEXT )";

    private static final String table_detPedido = "CREATE TABLE IF NOT EXISTS " +
            contracts.detpedido.Table_det + " ( " +
            contracts.detpedido.det_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            contracts.detpedido.det_codigoped + " TEXT, " +
            contracts.detpedido.det_codproducto + " TEXT, " +
            contracts.detpedido.det_cantidad + " TEXT, " +
            contracts.detpedido.det_costo + " TEXT, " +
            contracts.detpedido.det_posicion + " TEXT, " +
            contracts.detpedido.det_descripcion + " TEXT, " +
            contracts.detpedido.det_observaciones + " TEXT, " +
            contracts.detpedido.det_nomproducto + " TEXT, " +
            contracts.detpedido.det_subtotal + " TEXT, " +
            contracts.detpedido.det_orden + " TEXT, " +
            contracts.detpedido.det_promodificar + " TEXT, " +
            contracts.detpedido.det_termino +" TEXT )" ;

    public dbhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_cabPedido);
        Log.d("CREATE TABLE","Create Table Successfully cabecera pedido");
        db.execSQL(table_detPedido);
        Log.d("CREATE TABLE","Create Table Successfully detalle pedido");
        db.execSQL(table_producto);
        Log.d("CREATE TABLE","Create Table Successfully productos");
        db.execSQL(table_clientes);
        Log.d("CREATE TABLE","Create Table Successfully productos");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(table_cabPedido);
        Log.d("CREATE TABLE","Create Table Successfully cabecera pedido");
        db.execSQL(table_detPedido);
        Log.d("CREATE TABLE","Create Table Successfully detalle pedido");
        db.execSQL(table_producto);
        Log.d("CREATE TABLE","Create Table Successfully productos");
        db.execSQL(table_clientes);
        Log.d("CREATE TABLE","Create Table Successfully productos");
    }

/*
cabeceras pedidos
 */
    public long nuevoCabPedido(cabpedido objeto) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            resul= sqLiteDatabase.insert(
                    contracts.cabpedido.Table_cab,
                    null,
                    objeto.toContentValues());
            Log.i(TAG, "nuevoCabPedido: resul "+resul);
        }catch (SQLiteException e){
            Log.e(TAG, "nuevoCabPedido: ",e );
        }
        return resul;
    }

    public int UpdateCabPedido(String cod,  String cliente,String personas,String nomcliente){
        ContentValues values = new ContentValues();
        values.put(contracts.cabpedido.cab_cliente,cliente);
        values.put(contracts.cabpedido.cab_nomcliente,nomcliente);
        values.put(contracts.cabpedido.cab_canpersonar,personas);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int resul=0;
        try{
            resul=sqLiteDatabase.update(contracts.cabpedido.Table_cab,values,contracts.cabpedido.cab_codigoped +" LIKE '" +cod+"'",null);
        }catch (SQLiteException e){
            Log.e(TAG, "cabpedido update cliente: ", e);
        }
        return resul;
    }
    public int UpdateCabPedidoCliente(String cod,  String cliente,String nomcliente){
        ContentValues values = new ContentValues();
        values.put(contracts.cabpedido.cab_cliente,cliente);
        values.put(contracts.cabpedido.cab_nomcliente,nomcliente);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Log.i(TAG, "UpdateCabPedidoCliente: cod "+cod + "  cliente "+ cliente + " nomcliente "+nomcliente);
        int resul=0;
        try{
            resul=sqLiteDatabase.update(contracts.cabpedido.Table_cab,values,contracts.cabpedido.cab_codigoped +" LIKE '" +cod+"'",null);
        }catch (SQLiteException e){
            Log.e(TAG, "cabpedido update cliente: ", e);
        }
        return resul;
    }
    /*
    detalle pedido
     */
    public long nuevoDetPedido(detpedido objeto) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long resul=0;
        try {
            resul= sqLiteDatabase.insert(
                    contracts.detpedido.Table_det,
                    null,
                    objeto.toContentValues());
            Log.i(TAG, "nuevoDetPedido: resul + "+resul);
        } catch (SQLiteException e){
            Log.e(TAG, "nuevoDetPedido:sql ",e);
        }


        return resul;
    }

    public Cursor getCoddetPedidoestado(String codigo){
        try{
            String sql="select * from " + contracts.detpedido.Table_det +
                    " where "+ contracts.detpedido.det_codigoped+" = '"+codigo+ "'  and "+contracts.detpedido.det_promodificar + "='1'";
            cur_resultado = getReadableDatabase().
                    rawQuery(sql, null);
        }catch (Exception e){
            Log.e(TAG, "getCoddetPedidoestado: ",e );
        }
        return cur_resultado;
    }
    public Cursor getcoddetpedido(String codigo) {
        try {
            cur_resultado = getReadableDatabase()
                    .query(
                            contracts.detpedido.Table_det,
                            null,
                            contracts.detpedido.det_codigoped+" LIKE ?",
                            new String[]{codigo},
                            null,
                            null,
                            null);
        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro
    public Cursor getcodcabpedido(String codigo) {
        try {
            cur_resultado = getReadableDatabase()
                    .query(
                            contracts.cabpedido.Table_cab,
                            null,
                            contracts.cabpedido.cab_codigoped+" LIKE ?",
                            new String[]{codigo},
                            null,
                            null,
                            null);
        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro

    public int DeleteProPedido(String codped, String Codprod, String id){
        ContentValues values = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int resul=0;
        try{
            //resul=sqLiteDatabase.update(contracts.cabeceraPedido.TABLE_cabpedido,values,contracts.cabeceraPedido.CPcodpedido +" = '" +codped+"'",null);
            resul=sqLiteDatabase.delete(contracts.detpedido.Table_det,contracts.detpedido.det_codigoped +
                    " = '" +codped+"' and "+contracts.detpedido.det_codproducto+" = '"+Codprod +"' and  "+contracts.detpedido.det_id+ "= '"+id+"'",null);
        }catch (SQLiteException e){
            Log.e(TAG, "delete pro pedido: ", e);
        }
        return resul;
    }


    public Cursor gettotalPedido(String codtransaccion){
        try{
            cur_resultado=getReadableDatabase().
                    rawQuery("select SUM("+contracts.detpedido.det_subtotal+") from "+contracts.detpedido.Table_det +" where "+contracts.detpedido.det_codigoped +
                            " = '"+ codtransaccion+"'",null);
            Log.i(TAG, "getCobrosTransaccion: codtransaccion "+ codtransaccion);
        }catch (SQLiteException e) {
            Log.i("ERROR_SQL_cliente", e.toString());
        }
        return cur_resultado;
    }

    /*
    productos
     */
    public long nuevoProducto(producto objeto) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            resul= sqLiteDatabase.insert(
                    contracts.producto.Table_pro,
                    null,
                    objeto.toContentValues());
            Log.i(TAG, "nuevo producto: resul "+resul);
        }catch (SQLiteException e){
            Log.e(TAG, "nuevo producto: ",e );
        }
        return resul;
    }
    public Cursor getAllProductos(String codigo) {
        try {
            cur_resultado = getReadableDatabase()
                    .query(
                            contracts.producto.Table_pro,
                            null,
                            contracts.producto.pro_sublinea+" LIKE ?",
                            new String[]{codigo},
                            null,
                            null,
                            contracts.producto.pro_descripcion);
        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro
    public Cursor getAllProductosDescr(String codigo) {
        try {
            codigo=codigo.trim();
            String sql="Select * from " +contracts.producto.Table_pro +" where "+contracts.producto.pro_descripcion +
                    " like '%"+codigo +"%' or "+ contracts.producto.pro_codigo+" like '%"+codigo+"%'";
            cur_resultado=getReadableDatabase().rawQuery(sql,null);

        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro
    public Cursor getAllProductosDescrSublinea(String codigo, String Sublinea) {
        try {
            String sql="";
            codigo=codigo.trim();
            Sublinea=Sublinea.trim();

            if(Sublinea.length()>0){
                sql="Select * from " +contracts.producto.Table_pro +" where "+ contracts.producto.pro_sublinea +"= '"+ Sublinea +"'  and  ("+contracts.producto.pro_descripcion +
                        " like '%"+codigo +"%' or "+ contracts.producto.pro_codigo+" like '%"+codigo+"%')";
            }else{
                sql="Select * from " +contracts.producto.Table_pro +" where ("+contracts.producto.pro_descripcion +
                        " like '%"+codigo +"%' or "+ contracts.producto.pro_codigo+" like '%"+codigo+"%')";
            }

            Log.i(TAG, "getAllProductosDescrSublinea: sql  " + sql + "codigo " + codigo + "  sublinea  "+ Sublinea);
            cur_resultado=getReadableDatabase().rawQuery(sql,null);

        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro

    /*

    clientes
     */
    public long nuevoPcliente(cliente objeto) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            resul= sqLiteDatabase.insert(
                    contracts.cliente.Table_cli,
                    null,
                    objeto.toContentValues());
            Log.i(TAG, "nuevo producto: resul "+resul);
        }catch (SQLiteException e){
            Log.e(TAG, "nuevo producto: ",e );
        }
        return resul;
    }
    public Cursor getAllClientes(String codigo) {
        try {
            codigo=codigo.trim();
            String sql="Select * from " +contracts.cliente.Table_cli +" where "+contracts.cliente.cli_nombre +
                    " like '%"+codigo +"%' or "+ contracts.cliente.cli_ci+" like '%"+codigo+"%'";
            cur_resultado=getReadableDatabase().rawQuery(sql,null);
        } catch (SQLiteException e) {
            Log.i("ERROR_SQL_RegVisita", e.toString());
        }
        return cur_resultado;
    }//fin cursor getAllregistro
}

