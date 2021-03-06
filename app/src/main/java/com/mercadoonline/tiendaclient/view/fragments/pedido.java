package com.mercadoonline.tiendaclient.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistaPedidos;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerPedido;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class pedido extends Fragment {
    View vista;

    RecyclerView recyclerView;
    VistaPedidos adapter;
    List<ResponseVerPedido> listado= new ArrayList<>();
    Retrofit retrofit;
    ApiService retrofitApi;
    Boolean continuar=false;
    String mensaje="pedidos";
    RelativeLayout RelativeVacio;

    String tipoNegocio="";

    public pedido() {
        // Required empty public constructor
    }

 /*   String SelectTipo(int modo){
        String tp="";
        if(modo==1) tp="";
        return tp;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_pedido, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       recyclerView=vista.findViewById(R.id.Recycler_pedidos);
        RelativeVacio=vista.findViewById(R.id.RelativeVacio);
        peticion_pedidos();

    }

    private void  iniciar_recycler(){
        adapter= new VistaPedidos(listado, getFragmentManager(),getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void peticion_pedidos(){

        String tipo="";
        if(Global.Modo==1)tipo=Global.ROLCLIENTE;
         if(Global.Modo==2)tipo=Global.ROLMERCADO;
        if(Global.Modo==3)tipo="TIENDERO";

        //("Pedidos","Se consumio");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerPedidosClientes( tipo, ""+Global.LoginU.getid(),Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerPedido>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerPedido>> response) {


                        if(response.isSuccessful()){
                            listado.clear();
                            //("Pedido",""+response.code());
                            //("Resp Pedido", Global.convertObjToString(response.body()));
                            listado.addAll(response.body());
                            mensaje=""+response.code();
                            continuar=true;
                        }else if(response.code()==500){
                           mensaje="Ocurrio un error Insesperado";
                        }else{
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
                        //("CodPetiPEdidos","error"+ e.toString());

                    }

                    @Override
                    public void onComplete() {

                        //("CodPetiEroor","completado");
                        // adapter.notifyDataSetChanged();
                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido ");
                            return;
                        }else{



                            if(continuar){
                                if(listado.size()<1){
                                    RelativeVacio.setVisibility(View.VISIBLE);
                                    ///  getFragmentManager().popBackStack();
                                }else{
                                    RelativeVacio.setVisibility(View.GONE);
                                    iniciar_recycler();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                            }





                        }



                    }
                });
    }





}
