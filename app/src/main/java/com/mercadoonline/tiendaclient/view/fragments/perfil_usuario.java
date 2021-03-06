package com.mercadoonline.tiendaclient.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUserPorID;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;
import com.mercadoonline.tiendaclient.view.Login;
import com.google.gson.Gson;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class perfil_usuario extends Fragment {

    public perfil_usuario() {
        // Required empty public constructor
    }
    View vista;
    TextView PerfilNombresCompletos, PerfilUsuario,PerfilDireccion, PerfilCelular, PerfilCorreo, PerfilRol;
    ImageView PerfilFoto;

    String imagen="";
    String LinkImagenP=Global.UrlImagen;
    String Nombrecompleto="";
    Boolean continuar=false;
    String mensaje="";
    CircleImageView sesionoff;
   // ResponseUserPorID Usua= new ResponseUserPorID();

    Retrofit retrofit;
    ApiService retrofitApi;
//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_perfil_usuario, container, false);
        return  vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UI();
        llenaDts();
    }

    public void UI(){
        PerfilNombresCompletos=vista.findViewById(R.id.TVPerfilNombres);
        PerfilUsuario=vista.findViewById(R.id.TVPerfilUser);
       // PerfilDireccion=vista.findViewById(R.id.TVPerfilDireccion);
        PerfilCelular=vista.findViewById(R.id.TVPerfilCelular);
        PerfilCorreo=vista.findViewById(R.id.TVPerfilCorreo);
        PerfilRol=vista.findViewById(R.id.TVPerfilRol);
        PerfilFoto=vista.findViewById(R.id.PerfilFoto);
        sesionoff=vista.findViewById(R.id.sesionoff);
        sesionoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences DtsAlmacenados= getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                DtsAlmacenados.edit().clear().commit();
                Global.limpiar();
                Intent intent = new Intent (getActivity().getApplicationContext(), Login.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        peticion_usuario(Global.LoginU.getid());
    }

    private void peticion_usuario(int id){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        //JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.ObtenerUsuarioporID(""+id,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseUserPorID>>() {
                    @Override
                    public void onNext(Response<ResponseUserPorID> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            Global.UserGlobal=response.body();
                            LinkImagenP+=response.body().getImagenPerfil();
                            mensaje="Bienvenido "+Global.UserGlobal.getNombres();
                            continuar=true;
                        } else  if (response.code()==500) {
                            mensaje = "Internal Server Error";
                        } else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();
                                //("normal-->400",mensaje);

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }

                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //("Completado","Se cargo usuario");
                        if(continuar){
                            llenaDts();

                        }else{
                            Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                        }



                    }
                });
    }

    private  void llenaDts(){
        if(getActivity()==null || isRemoving() || isDetached()){
            //("activity","removido de la actividad ");
            return;
        }


        Log.e("objeto",Global.convertObjToString(Global.UserGlobal));
        Log.e("imagen",LinkImagenP);
        llenar_subida();



        PerfilNombresCompletos.setText(Global.PrimeraMayusculaNP(Global.LoginU.getNombres()+" "+Global.LoginU.getApellidos()));
        PerfilUsuario.setText("@"+Global.UserGlobal.getUsuario());
        //PerfilDireccion.setText(""+Global.UserGlobal.getDireccion());
        PerfilCelular.setText("+"+Global.UserGlobal.getCelular());
        PerfilCorreo.setText(""+Global.UserGlobal.getEmail());
        PerfilRol.setText(""+Global.ucFirst(Global.UserGlobal.getRol().toLowerCase()));
    }

    private void llenar_subida(){


        Glide.with(this).load(LinkImagenP).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder_perfil)
                .diskCacheStrategy(DiskCacheStrategy.NONE )
                .error(R.drawable.placeholder_perfil).apply(RequestOptions.circleCropTransform()).into(PerfilFoto);



    }



}
