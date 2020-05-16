package com.mercadoonline.tiendaclient.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.mapas.ApiString;
import com.mercadoonline.tiendaclient.mapas.MapDialogFragment;
import com.mercadoonline.tiendaclient.models.ApiMaps.AddressComponent;
import com.mercadoonline.tiendaclient.models.ApiMaps.DatosDireccion;
import com.mercadoonline.tiendaclient.models.enviado.ReqRegTienda;
import com.mercadoonline.tiendaclient.models.recibido.ResponseCategorias;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.ApiService2;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.service.RetrofitclienteMaps;
import com.mercadoonline.tiendaclient.utils.Global;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.mercadoonline.tiendaclient.utils.Global.verificar_vacio;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroLocal extends Fragment {

    Uri imagenNegocio; //Imagen q va a ser agregada al registro

    View vista;
    TextInputLayout TINomNegocio, TITelNegocio, TIDescripcionTienda;
    ImageView IconoTienda;
    RoundedImageView ImagenTiendaNueva;
    EditText ETNomNegocio, ETTelNegocio, ETDescripcionTienda;
    LinearLayout ContenedorUbicame, ContenedorVerDir;
    CircularProgressButton BtnRegisNegoio;
    TextView TVPresentDir;


    Spinner spnCategoriasTiendas;
    int posCategoria;
    List<ResponseCategorias> CatTien = new ArrayList<>();
    ArrayList<String> list_categorias = new ArrayList<String>();
    ArrayAdapter<String> spinnerArrayAdapter;
    Retrofit retrofit2;
    ApiService retrofitApi2;
    Boolean continuar = false;
    String mensaje = "";


    MapDialogFragment map;
    //LatLng NuevaUbicacion = null;
    String calle = "Dirección no especifica";

    ApiService2 retrofitApi;
    Retrofit retrofit;
    String direccion="";

    LocationManager locationManager ;
    private Marker marcador;
    public  LatLng NuevaUbicacion =null;

    SweetAlertDialog dialog_permisos;
    SweetAlertDialog dialog_manual;

    ReqRegTienda NuevaTienda = new ReqRegTienda();
    SweetAlertDialog pDialog;
    public int bandera=1;

    String ciudad;

    public registroLocal() {
        // Required empty public constructor
    }

    private void UI(){
        TINomNegocio= vista.findViewById(R.id.TINombreNegocio);
        TITelNegocio=vista.findViewById(R.id.TITelefonoNegocio);
        ETNomNegocio=vista.findViewById(R.id.ETNombreNegocio);
        ETTelNegocio=vista.findViewById(R.id.ETTelefonoNegocio);
        ContenedorUbicame=vista.findViewById(R.id.contenedorUbicame);
        BtnRegisNegoio=vista.findViewById(R.id.BtnRegisNegocio);
        IconoTienda =vista.findViewById(R.id.imageIconoNeg);
        TVPresentDir=vista.findViewById(R.id.TVVerDir);
        ContenedorVerDir=vista.findViewById(R.id.layout_direcciónNegocio);
        spnCategoriasTiendas=vista.findViewById(R.id.spn_catTienda);
        ImagenTiendaNueva=vista.findViewById(R.id.imagenTiendaNueva);
        ETDescripcionTienda=vista.findViewById(R.id.ETDescripTienda);
        TIDescripcionTienda=vista.findViewById(R.id.TIDescripTienda);

/*        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_item2,Unidades);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        SPUnidad.setAdapter(spinnerArrayAdapter);
        SPUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               *//* if (position == spinnerArrayAdapter.getCount()) {
                   ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_gris));
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.col_negrosolida));*//*
                posUnidadMedida=position;

            }*/

        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, list_categorias);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnCategoriasTiendas.setAdapter(spinnerArrayAdapter);



        map = new MapDialogFragment(new MapDialogFragment.OnItemOk() {
            @Override
            public void onItemClickok(LatLng position) {

                NuevaUbicacion = position;

                if (NuevaUbicacion != null) {

                    ContenedorUbicame.setBackground(getResources().getDrawable(R.drawable.border_verde));
                    TVPresentDir.setText(calle);
                    ContenedorVerDir.setVisibility(View.VISIBLE);
                    consulta_ubicacion();
                   /* new AsyncTask<Void, Void, Boolean>() {


                        @Override
                        protected Boolean doInBackground( Void... voids ) {
                            //Do things...
                               direccion();
                               consulta();

                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if(aBoolean){
                                presentacion_dirección.setText(calle);

                            }
                        }
                    }.execute();
*/
                    //  presentacion_dirección.setText(calle);  esto esta en layout direccion

                }
                Log.e("obtengo", "position");
            }
        });

    }

    public void CLick(){
        ContenedorUbicame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagenNegocio != null)
                    Log.e("imgane", imagenNegocio.toString());
                    map.fotito = imagenNegocio;
       // Log.e("Se cargo fotito", map.fotito.toString());
                if (NuevaUbicacion != null) {
                    Log.e("cambio", "ubicacion");
                    map.nuevo = NuevaUbicacion;
                }

                if(validar_permisos()){
                    map.show(getFragmentManager(), null);

                }



            }
        });

        BtnRegisNegoio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar_campos();

                //("boton registar", "se dio clic ");
            }
        });
        //Evento click para cortar o seleccionar imagen
        IconoTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcion_cortar();
            }
        });

        //Evento click para cortar o seleccionar imagen
        ImagenTiendaNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcion_cortar();
            }
        });



        //validaciones para que al seleccionar campo, el texview cambien de color
        ETNomNegocio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {

                    TINomNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TINomNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });
        ETTelNegocio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {

                    TITelNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TITelNegocio.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });

        ETDescripcionTienda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //spn_rolUser
                if (hasFocus) {

                    TIDescripcionTienda.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIDescripcionTienda.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
            //validaciones para que al seleccionar campo, el texview cambien de color


        });

        spnCategoriasTiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posCategoria = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_registro_local, container, false);
        return  vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //animacion_cargando();
        UI();
       peticion_categorias();
        CLick();
    }

    private void validar_campos(){

        if(verificar_vacio(ETNomNegocio.getText().toString())) {
            ETNomNegocio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        }else   if(verificar_vacio(ETTelNegocio.getText().toString())) {
            ETTelNegocio.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        } else       if(verificar_vacio(ETDescripcionTienda.getText().toString())) {
            ETDescripcionTienda.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
        } else if (imagenNegocio==null) {
            mensaje();
        }else if(direccion.length()<2){
            Snackbar.make(vista, "Escoja su direccion en el Mapa", Snackbar.LENGTH_LONG).show();
        }
        else {
            Snackbar.make(vista, "Deberia registrar", Snackbar.LENGTH_LONG).show();
            //llenarDatos();
        }

    }

    public void funcion_cortar() {
        CropImage.activity()
                .setAspectRatio(16, 9)
                .setFixAspectRatio(true)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //("Foto", "Entre a ver foto");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imagenNegocio =result.getUri();
                //("obtuve imagen",""+imagen_perfil);


                llenar_subida();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                //("error imagen",result.getError().toString());
            }


        }
    }

    private void llenar_subida(){


        //Glide.with(this).load(imagenNegocio).apply(RequestOptions.centerCropTransform()).into(ImagenTiendaNueva);
        IconoTienda.setVisibility(View.GONE);
        Glide.with(this).load(imagenNegocio).into(ImagenTiendaNueva);
        ImagenTiendaNueva.setVisibility(View.VISIBLE);

    }
    //mensaje.. de que debe llenar la imagen obligatorio
    private void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¡OH!");
        builder.setMessage("No ha puesto foto de su negocio");
        builder.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void consulta_ubicacion() {
        retrofit = RetrofitclienteMaps.getInstance();
        retrofitApi = retrofit.create(ApiService2.class);
        Disposable disposable;
        String lat = "" + NuevaUbicacion.latitude + "," + NuevaUbicacion.longitude;

        //Logger.addLogAdapter(new AndroidLogAdapter());
        disposable = retrofitApi.traerGeo(true, lat, getString(R.string.google_maps_keyM))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<DatosDireccion>>() {
                    @Override
                    public void onNext(Response<DatosDireccion> respuesta) {

                        if(respuesta.isSuccessful()){
                            try {
                                if(respuesta.body().getResults().size()>0)
                                    direccion = respuesta.body().getResults().get(0).getFormattedAddress();
                                   //todo cambio
                                    for (AddressComponent address : respuesta.body().getResults().get(0).getAddressComponents()) {


                                        for (String s : address.getTypes()) {
                                            //("Carlin:", "---" + s);


                                           datos_ubicacion(s, address.getLongName());
                                        }


                                        if (address.getTypes().contains(ApiString.CIUDADELA3) || address.getTypes().contains(ApiString.CIUDADELA4)) {
                                             ciudad= address.getLongName();
                                        } else if (address.getTypes().contains(ApiString.CIUDADELA) || address.getTypes().contains(ApiString.CIUDADELA2)) {
                                            ciudad=address.getLongName();
                                        }


                                    }

                            }catch (Exception e){

                                direccion();
                            }

                        } else direccion();



                       /* //todo cambio
                        for (AddressComponent address : respuesta.getResults().get(0).getAddressComponents()) {


                            for (String s : address.getTypes()) {
                                //("Carlin:", "---" + s);


                                datos_ubicacion(s, address.getLongName());
                            }


                            if (address.getTypes().contains(ApiString.CIUDADELA3) || address.getTypes().contains(ApiString.CIUDADELA4)) {
                                tienda.setCiudad(address.getLongName());
                            } else if (address.getTypes().contains(ApiString.CIUDADELA) || address.getTypes().contains(ApiString.CIUDADELA2)) {
                                tienda.setCiudad(address.getLongName());
                            }


                        }
*/

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                     //  Log.e("Ciudadela", "-" + tienda.getCiudadela());
                      // Log.e("Calle", "-" + tienda.getCalle());
                       Log.e("Ciudad", ciudad);
                      //  Log.e("Pais", tienda.getPais());
                        if(direccion.length()>3)
                            Log.e("la direc",""+direccion);
                            //UbicaBtnContinuar.setVisibility(View.VISIBLE);
                        TVPresentDir.setText(direccion);

                    }
                });

    }

    private void datos_ubicacion(String tipo, String dato) {

        switch (tipo) {

            case ApiString.PROVINCIA:
               // tienda.setProvincia(dato);
                break;

            case ApiString.CIUDAD:
               // tienda.setCiudad(dato);
                ciudad=dato;
                break;

            case ApiString.CALLE:
                //tienda.setCalle(dato);
                break;

            case ApiString.PAIS:
               // tienda.setPais(dato);

                break;
        }

//    tienda.setCiudad(yourCiudadela);
    }

    private void direccion(){
        Geocoder geocoder= new Geocoder(getActivity());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(NuevaUbicacion.latitude, NuevaUbicacion.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.size()>0){

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String knownName2 = addresses.get(0).getPremises();
                String knownName3 = addresses.get(0).getSubAdminArea();
                String knownName4 = addresses.get(0).getSubLocality();
                String knownName5 = addresses.get(0).getSubThoroughfare();
                //("addess","-"+address);
                //("city","-"+city);
                //("state","-"+state);
                //("country","-"+country);
                //("postalCode","-"+postalCode);
                //("lugar","-"+knownName);
                //("lugar","-"+knownName2);
                //("lugar","-"+knownName3);
                //("lugar","-"+knownName4);
                //("lugar","-"+knownName5);
                direccion=address;
                TVPresentDir.setText(direccion);
            }else{

                direccion="Direccion No Especificada ";
                TVPresentDir.setText(direccion);
            }
            Log.e("la direc 2",""+direccion);


        } catch (IOException e) {
            e.printStackTrace();
            //("lugar","-"+e.toString());

        }


    }

    private void peticion_categorias() {
        //("peticion","mercado");
        retrofit2 = RetrofitCliente.getInstance();
        retrofitApi2 = retrofit2.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi2.TraerCategoriasTiendas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseCategorias>>>() {
                    @Override
                    public void onNext(Response<List<ResponseCategorias>> response) {


                        if (response.isSuccessful()) {

                            //("code VM",""+response.code());
                            //("respuest VM",Global.convertObjToString(response.body()));
                            CatTien = response.body();
                            continuar = true;

                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson = new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje = staff.getMensaje();
                                //("normal-->400",mensaje);

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        //("code VM","error");

                    }

                    @Override
                    public void onComplete() {

                        //("code VM","completado");
                        // adapter.notifyDataSetChanged();
                        if (getActivity() == null || isRemoving() || isDetached()) {
                            //("activity","removido de la actividad mercado");
                            return;
                        } else {


                            if (continuar) {
                                for (ResponseCategorias x : CatTien) {
                                    list_categorias.add(x.getNombre());
                                    spinnerArrayAdapter.notifyDataSetChanged();

                                }

                            } else {
                                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
                            }


                        }


                    }
                });
    }

    private boolean validar_permisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //("tengo permisos", "bien");
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Todo no tiene permisos y le sale para dar
                cargarDialogoRecomendacion();
                //("dialogo", "recomendacion");
            } else {
                //Todo no tiene permisos por que los nego y puso no volver a presentar asi que  mandamos de nuevo  a pedir y entrara
                //Todo a permisos manual
                //  requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                //("dialogo", "else");
            }
            return false;
        }
        return true;
    }
    //Todo un dialog que recomienda por que activar los permisos

    private void cargarDialogoRecomendacion() {

        dialog_permisos = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog_permisos.setTitleText("Permisos Desactivados");
        dialog_permisos.setContentText("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialog_permisos.setConfirmText("OK2");
        dialog_permisos.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    requestPermissions
                            (new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
                dialog_permisos.dismissWithAnimation();
            }
        });


        dialog_permisos.setCancelable(false);
        dialog_permisos.show();
    }

    private void solicitarPermisosManual() {
        dialog_manual = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialog_manual.setTitleText("Permisos Desactivados");
        dialog_manual.setContentText(" Configure los permisos de forma manual para el correcto funcionamiento de la App");
        dialog_manual.setConfirmText("OK");
        dialog_manual.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

                dialog_manual.dismissWithAnimation();
            }
        });


        dialog_manual.setCancelable(false);
        dialog_manual.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //("permisos ", "defecto");
                map.show(getFragmentManager(), null);
               // ir_ubicacion();

            } else {
                //("permisos", "manual");

                solicitarPermisosManual();
            }
        }

    }

    public  void llenarDatos(){
        NuevaTienda.setTipoNegocio(CatTien.get(posCategoria).getId());
        NuevaTienda.setDescripcion(ETDescripcionTienda.toString());
        NuevaTienda.setTelefono(ETTelNegocio.toString());
        NuevaTienda.setNombre(ETNomNegocio.toString());
        NuevaTienda.setIdUsuario(Global.UserGlobal.getId());
        NuevaTienda.setCiudad("");

        //("Llenar Ctg", "los datos llenados son "+ Global.convertObjToString(NuevoProducto));
        //siguiente_paantalla();
        Gson gson = new Gson();
        String JPetProducto= gson.toJson(NuevaTienda);
        //("json",JPetProducto);
        //  animacion_registro();
        //  subir_ProductoConImagen(JPetProducto);
        pDialog.show();
        if(bandera==1){

            //subir_ProductoConImagen(JPetProducto);
        }else{
            //peticion_EdicionProduct(JPetProducto, product.getId().toString());
///
///funcion editar que es un put para los datos
            // y un post para subir imagen
            //tienes que crear esos dos api rest
        }


    }
}
