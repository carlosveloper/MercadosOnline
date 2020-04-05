package com.example.tiendaclient.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.tiendaclient.models.compra.Compra;
import com.example.tiendaclient.models.enviado.PeticionRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseCategorias;
import com.example.tiendaclient.models.recibido.ResponseLoginUser;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseUserPorID;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static String IdUser;
    public static PeticionRegistroUser RegisU= new PeticionRegistroUser();
    public static ResponseLoginUser LoginU= new ResponseLoginUser();
    public static ResponseRegistroUser RegisUser= new ResponseRegistroUser();
    //objeto global para los datos q se presentan en el perfil de usuario
    public static ResponseUserPorID UserGlobal=new ResponseUserPorID();
    public static String Url="http://mercados-online.com/public/api/";


    public static  List<ResponseCategorias> categorias = new ArrayList<>();
    public static  List<String> Nombres_Categoria = new ArrayList<>();
    public static List<Compra> VerCompras= new ArrayList<>();

public static void llenarToken(){
    LoginU.setToken("Bearer "+ LoginU.getToken());
}

    public static void limpiar(){
        RegisU= new PeticionRegistroUser();
        LoginU= new ResponseLoginUser();
        RegisUser= new ResponseRegistroUser();
        UserGlobal=new ResponseUserPorID();
        categorias = new ArrayList<>();
        Nombres_Categoria = new ArrayList<>();
        VerCompras= new ArrayList<>();

    }



    public static void llenar_categoria(){
        for (ResponseCategorias x:categorias){
            Nombres_Categoria.add(x.getNombre());
        }


    }

    public static int Modo;





    public static String convertObjToString(Object clsObj) {
        //convert object  to string json
        String jsonSender = new Gson().toJson(clsObj, new TypeToken<Object>() {
        }.getType());
        return jsonSender;
    }

    public static Boolean verificar_vacio(String texto) {
        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            return true;
        }
       return false;
    }



    public static void Agregar_Carrito(Compra nueva){

            Boolean encontre =false;
        for(Compra C: VerCompras){

            if(C.getId()==nueva.getId()){

              C.setCantidad(C.getCantidad()+nueva.getCantidad());
              C.setTotal(formatearDecimales(C.getTotal()+nueva.getTotal(),2));
                //("totalGlobal" , "------------->"+C.getTotal());
                C.agregar_producto(nueva.getPuestos().get(0));
                encontre=true;
            }

        }

        if(!encontre)
        VerCompras.add(nueva);


        //("lista",convertObjToString(VerCompras));

    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }




}
