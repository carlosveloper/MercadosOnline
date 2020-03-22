package com.example.tiendaclient.view.fragments;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tiendaclient.R;
import com.example.tiendaclient.adapter.VistasMercado;
import com.example.tiendaclient.models.recibido.ResponseError;
import com.example.tiendaclient.models.recibido.ResponseRegistroUser;
import com.example.tiendaclient.models.recibido.ResponseVerMercado;
import com.example.tiendaclient.service.ApiService;
import com.example.tiendaclient.service.RetrofitCliente;
import com.example.tiendaclient.utils.Global;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

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
public class mercado extends Fragment {

    public mercado() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    RoundedImageView busqueda;
    VistasMercado adapter;
    List<ResponseVerMercado> listado= new ArrayList<>();
    Retrofit retrofit;
    ApiService retrofitApi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        vista= inflater.inflate(R.layout.fragment_mercado, container, false);

return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=vista.findViewById(R.id.Recycler_mercados);
        busqueda=vista.findViewById(R.id.icono_buscar);
        iniciar_recycler();
        click();

    }

    private void  iniciar_recycler(){
        adapter= new VistasMercado(listado,getFragmentManager());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        peticion_mercado();
    }

    private void peticion_mercado(){
        Log.e("peticion","mercado");
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.VerMercados()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<ResponseVerMercado>>>() {
                    @Override
                    public void onNext(Response<List<ResponseVerMercado>> response) {

                        Log.e("code VM",""+response.code());
                        Log.e("respuest VM",Global.convertObjToString(response.body()));
                        listado.addAll(response.body());


                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("code VM","error");

                    }

                    @Override
                    public void onComplete() {

                        Log.e("code VM","completado");
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    private void click(){

        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"La busqueda esta opcional pero esta puesto el prototipo si pide un update $ ",Toast.LENGTH_LONG).show();
            }
        });
    }

}