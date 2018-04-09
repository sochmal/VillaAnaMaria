package com.villanamaria.app.villaanamara.data;

import android.provider.BaseColumns;

import java.util.StringTokenizer;

/**
 * Created by Christian on 18/12/2017.
 */

public class contracts {

    public static abstract  class  cabpedido implements BaseColumns{
        public static final String Table_cab="cabpedido";
        public static final String cab_id="_id";
        public static final String cab_codigoped="codped";
        public static final String cab_cliente="cliente";
        public static final String cab_fecha="fecha";
        public static final String cab_observaciones="observaciones";
        public static final String cab_canpersonar="canpersonas";
        public static final String cab_nomcliente="nomcliente";
        public static final String cab_codmesa="codmesa";

    }
    public static abstract  class  producto implements BaseColumns{
        public static final String Table_pro="producto";
        public static final String pro_id="_id";
        public static final String pro_codigo="codigo";
        public static final String pro_descripcion="descripcion";
        public static final String pro_precio="precio";
        public static final String pro_termino="termino";
        public static final String pro_sublinea="sublinea";
    }


    public static abstract class cliente implements  BaseColumns{
        public static final String Table_cli="cliente";
        public static final String cli_id="_id";
        public static final String cli_ci="ci";
        public static final String cli_nombre="nombre";
        public static final String cli_direccion="direccion";
        public static final String cli_telefono="telefono";
        public static final String cli_email="email";
    }
    public static abstract  class  detpedido implements BaseColumns{
        public static final String Table_det="detpedido";
        public static final String det_id="_id";
        public static final String det_codigoped="codped";
        public static final String det_codproducto="procodigo";
        public static final String det_nomproducto="nomcodigo";
        public static final String det_cantidad="procantidad";
        public static final String det_costo="procosto";
        public static final String det_termino="protermino";
        public static final String det_posicion="proposicion";
        public static final String det_descripcion="descripcion";
        public static final String det_observaciones="observaciones";
        public static final String det_orden="orden";
        public static final String det_subtotal="subtotal";
        public static final String det_promodificar="promodificar";
    }
}
