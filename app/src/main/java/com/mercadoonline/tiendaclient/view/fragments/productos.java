package com.mercadoonline.tiendaclient.view.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mercadoonline.tiendaclient.R;
import com.mercadoonline.tiendaclient.adapter.VistasProductos;
import com.mercadoonline.tiendaclient.adapter.VistasProductosPromos;
import com.mercadoonline.tiendaclient.models.compra.Compra;
import com.mercadoonline.tiendaclient.models.compra.CompraProductos;
import com.mercadoonline.tiendaclient.models.compra.PromosProductos;
import com.mercadoonline.tiendaclient.models.compra.PuestosCompra;
import com.mercadoonline.tiendaclient.models.recibido.Productos;
import com.mercadoonline.tiendaclient.models.recibido.RespPromociones;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerMercado;
import com.mercadoonline.tiendaclient.models.recibido.Vendedor;
import com.mercadoonline.tiendaclient.service.ApiService;
import com.mercadoonline.tiendaclient.service.RetrofitCliente;
import com.mercadoonline.tiendaclient.utils.Global;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mercadoonline.tiendaclient.utils.Global.PrimeraMayusculaNP;

/**
 * A simple {@link Fragment} subclass.
 */
public class productos extends Fragment {

    public  ResponseTiendas tienda = new ResponseTiendas();

    /////
    public List<Productos> ls_listado= new ArrayList<>();
    public Vendedor vendedor= new Vendedor();
    public ResponseVerMercado Mercado =new ResponseVerMercado();

   // public List<Producto> ListProdcutsPorPuesto= new ArrayList();
    public ResponseVerAllPuesto TiendaPorId= new ResponseVerAllPuesto();


    TextView NombreProducto, UnidadesProd, Valorproduct,DescripProduct,Subtotal, CategoriaDelProduct;

    ElegantNumberButton CantidadCar;
    ImageView FotoPuesto;
    ImageView FotoProducto;

    //Elementos de Dialog Ver producto en modo VENDEDOR
    TextView TVProducNombreV, TVCategoriaV, TVCompDescripcionV, TVUnidadMedidaP, TVCompSubtotalV;
    ImageView TVFotoProduct, Carrito;
    Button TVBtnEditar,TVBtnElimiar;

    Button AgregarCarrito;

    ImageView compra;
    EditText buscar;

    Retrofit retrofit;
    ApiService retrofitApi;
    ScaleInRightAnimator animator1 = new ScaleInRightAnimator();

    List<RespPromociones> list_respPromociones= new ArrayList<>();
    List<PromosProductos> lst_normal= new ArrayList<>();




    String mensaje="productos";

    DecimalFormat df = new DecimalFormat("#.00");//formatear  a 2 decimales

    public productos() {
        // Required empty public constructor
    }

    View vista;
    RecyclerView recyclerView;
    VistasProductos adapter;
    VistasProductosPromos adapter2;
    TextView Idpuesto, NombreDueno, DescripcionPuesto, Cantidadpro;
    public String idPuesto="";//PT-001 Base
    public String idPuestoI="";//identificador unico
    public String ImageVendedor="";
    public  String categorias="";
    public int ID=0;
    Dialog myDialog;
    SweetAlertDialog pDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //("modo",""+Global.Modo);

        UI();
        click();
        animacion_cargando();
Log.e("el modo es",""+Global.Modo);
        //iniciar_recycler2();
        if(Global.Modo==1){

            //TODO la lista de productos viende del adaptador para cliente
            //("esta es el del vendedor","------>"+ImageVendedor);
            animacion_compra();
            iniciar_recycler();
            Log.e("El filtro actual es",""+Global.idFiltro);
            if(Global.idFiltro==0)
            llenarDatosClientes();
            if(Global.idFiltro==1){
                llenarDatosTiendero();
               // peticion_ProductosporTienda("TIENDA", tienda.getId());
                peticion_promociones(""+tienda.getId(), false);
            }


        }else if(Global.Modo==2 ){
            //TODO la lista de productos debe consultarse modo vendedor(pertence a un mercado)
            mirar_producto();
            peticion_ProductosPorID();
        }else {
            Log.e("Estoy","Modo 3");
           // peticion_ProductosporTienda("TIENDA", tienda.getId());
            peticion_promociones(""+tienda.getId(), false);
            mirar_producto();

            // tienda.getId()
            //Aqui consumir para la lista de tiendas
            //GET ALL PRODUCTOS

            llenarDatosTiendero();
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =inflater.inflate(R.layout.fragment_productos, container, false);
        return vista;
    }

    private void  iniciar_recycler(){
        Log.e("recycler 1", "estoy aqui");
        recyclerView= vista.findViewById(R.id.Recycler_productos);
        adapter=new VistasProductos(ls_listado, new VistasProductos.OnItemClicListener() {
            @Override
            public void onItemClick(final Productos product, int position) {

               Log.e("el producto es",Global.convertObjToString(product));

                if(Global.Modo==1){comprar_productos(product);}
                else if(Global.Modo==2 || Global.Modo==3){seleccionar_producto(product,position);}
            }
        });
       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);*/
      //  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void  iniciar_recycler2(){
        Log.e("recycler 2", "estoy aqui");
        recyclerView= vista.findViewById(R.id.Recycler_productos);
        adapter2=new VistasProductosPromos(lst_normal, getFragmentManager(), getActivity(), new VistasProductosPromos.OnItemClicListener() {
            @Override
            public void onItemClick(Productos product, int position) {
                if(Global.Modo==1){comprar_productos(product);}
                else if(Global.Modo==2 || Global.Modo==3){seleccionar_producto(product,position);}
            }
        });
        //  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter2);
    }

    private void seleccionar_producto(Productos product, int position){

        //("selccionar","estoy aqui cambiar foto");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TVProducNombreV.setText(""+product.getNombre());
        TVCategoriaV.setText(""+product.getNombreCategoria());
        TVCompDescripcionV.setText(""+product.getDescripcion());
        TVUnidadMedidaP.setText(""+product.getUnidades());
        DecimalFormat f = new DecimalFormat("##.00");
        if(Double.parseDouble(""+product.getPrecio())<1.0) TVCompSubtotalV.setText("$0"+f.format(Double.parseDouble(""+product.getPrecio())));
            else TVCompSubtotalV.setText("$"+f.format(Double.parseDouble(""+product.getPrecio())));
        //TVCompSubtotalV.setText("$"+f.format(Double.parseDouble(product.getPrecio())));

        //("el producto",""+product.getId());
        //cargar foto

        String url=Global.UrlImagen+product.getUrlImagen();

        Glide
                .with(TVFotoProduct.getContext())
                .load(url)
                .placeholder(R.drawable.ic_place_productos)
                .error(R.drawable.ic_place_productos)
                .diskCacheStrategy(DiskCacheStrategy.NONE )
                .skipMemoryCache(true)
                .into(TVFotoProduct);
        //("la url de foto",url);
        TVBtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar_productos prod = new agregar_productos();

                prod.product=product;
                prod.bandera=2;

                if(Global.Modo==3){
                    prod.idNegocio=tienda.getId();
                }

                Log.e("mi negocio es ",""+tienda.getId());
                Log.e("mi negocio es ",""+prod.idNegocio);

                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, prod).addToBackStack(null);
                fragmentTransaction.commit();
                //llenarCarrito(product);
                myDialog.dismiss();

            }
        });
        TVBtnElimiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llenarCarrito(product);
                myDialog.dismiss();
                pDialog.show();
                EliminarProducto(product,position);

            }
        });

        myDialog.show();

    }


    private void comprar_productos(Productos product){

        //("hey producto","click");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //

        NombreProducto.setText(product.getNombre());
        UnidadesProd.setText("Precio por "+product.getUnidades());
        DecimalFormat f = new DecimalFormat("##.00");
        if(Double.parseDouble(""+product.getPrecio())<1.0)  Valorproduct.setText("$0"+f.format(Double.parseDouble(""+product.getPrecio())));
            else Valorproduct.setText("$"+f.format(Double.parseDouble(""+product.getPrecio())));

        //Valorproduct.setText("$"+f.format(Double.parseDouble(product.getPrecio())));
        if(Double.parseDouble(""+product.getPrecio())<1.0) Subtotal.setText("$0"+f.format(Double.parseDouble(""+product.getPrecio())));
            else Subtotal.setText("$"+f.format(Double.parseDouble(""+product.getPrecio())));
      //  Subtotal.setText("$"+f.format(Double.parseDouble(product.getPrecio())));
        if(product.getNombreCategoria()=="null") CategoriaDelProduct.setText("");
            else CategoriaDelProduct.setText(product.getNombreCategoria());

        DescripProduct.setText(product.getDescripcion());
        CantidadCar.setNumber("1");

        Glide
                .with(getActivity())
                .load(Global.UrlImagen+product.getUrlImagen())
                .placeholder(R.drawable.ic_place_productos)
                .error(R.drawable.ic_place_productos)
                . skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE )

                .into(FotoProducto);

        final double precio=Double.parseDouble(""+product.getPrecio());
        //final double subtotal=0;
        CantidadCar.setRange( 1,  1000);
        CantidadCar.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //("Elegante btn", String.format("oldValue: %d   newValue: %d", oldValue, newValue));


                Subtotal.setText("$"+df.format(precio*newValue));
            }
        });

        AgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getActivity(), "Se agrego al Carrito", Toast.LENGTH_SHORT).show();
                llenarCarrito(product);
                myDialog.dismiss();

            }
        });

        // FotoProducto=myDialog.findViewById(R.id.TVPuestoFotoV);
        //AgregarCarrito=myDialog.findViewById(R.id.TVCompBtnAgregarCar);
        myDialog.show();


    }

private void llenarDatosClientes(){

    NombreDueno.setText(PrimeraMayusculaNP(vendedor.getNombres()+" "+vendedor.getApellidos()));


    Cantidadpro.setText(""+(ls_listado.size()) + " Productos");

    Idpuesto.setText(idPuesto);
    Glide
            .with(getActivity())
            .load(ImageVendedor)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.placeholder_perfil)
            .error(R.drawable.placeholder_perfil)
            .skipMemoryCache(true)
            .fitCenter()

            .into(FotoPuesto);
}




    private void UI(){
        Idpuesto= vista.findViewById(R.id.TVProPuesto);
        NombreDueno= vista.findViewById(R.id.TVPuestoNombre);
        DescripcionPuesto= vista.findViewById(R.id.TVPuestoDescripcion);
        Cantidadpro= vista.findViewById(R.id.TVProCantidad);
        FotoPuesto= vista.findViewById(R.id.TVPuestoFotoV);
        if(categorias.length()<1 || categorias==null || categorias.equals("null")) DescripcionPuesto.setText("");
            else  DescripcionPuesto.setText(categorias);

        compra=vista.findViewById(R.id.icono_buscar);
        buscar=vista.findViewById(R.id.escribir_busqueda);
        buscar.clearFocus();


        //iniciar_recycler2();

        if(Global.Modo==2 || Global.Modo==3) {
            Resources resources = getResources();
            compra.setImageDrawable(resources.getDrawable(R.drawable.ic_plus));
        }

    }

    private void animacion_compra(){
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialog_compra);
        myDialog.setCancelable(true);

        NombreProducto=myDialog.findViewById(R.id.TVProducNombre);
        UnidadesProd=myDialog.findViewById(R.id.TVProducUnidades);
        Valorproduct=myDialog.findViewById(R.id.TVProducValor);
        DescripProduct=myDialog.findViewById(R.id.TVCompDescripcion);
        CategoriaDelProduct=myDialog.findViewById(R.id.TVCategoria);
        CantidadCar=myDialog.findViewById(R.id.TVCompCantidad);
        Subtotal=myDialog.findViewById(R.id.TVCompSubtotal);
        FotoProducto=myDialog.findViewById(R.id.TVPuestoFotoV);
        AgregarCarrito=myDialog.findViewById(R.id.TVCompBtnAgregarCar);


    }

    private void mirar_producto(){
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialog_ver_producto);
        myDialog.setCancelable(true);
        //TextView TVProducNombreV, TVCategoriaV, TVCompDescripcionV, TVUnidadMedidaP, TVCompSubtotalV;
        TVProducNombreV=myDialog.findViewById(R.id.TVProducNombreV);
        TVCategoriaV=myDialog.findViewById(R.id.TVCategoriaV);
        TVCompDescripcionV=myDialog.findViewById(R.id.TVCompDescripcionV);
        TVUnidadMedidaP=myDialog.findViewById(R.id.TVUnidadMedidaP);
        TVCompSubtotalV=myDialog.findViewById(R.id.TVCompSubtotalV);

        TVFotoProduct =myDialog.findViewById(R.id.TVFotoProductVer);

        TVBtnEditar=myDialog.findViewById(R.id.TVBtnEditar);
        TVBtnElimiar=myDialog.findViewById(R.id.TVBtnElimiar);


    }






private void llenarCarrito(Productos product){
    Log.e("producto agregado",Global.convertObjToString(product)) ;

    Compra nuevoC = new Compra();
    List<PuestosCompra> pues = new ArrayList<>();
    PuestosCompra PuestComp = new PuestosCompra();
    List<CompraProductos> lstprod=new ArrayList<>();
    CompraProductos prod= new CompraProductos();
    prod.setNombre(product.getNombre());
    prod.setDescripcion(product.getDescripcion());
    prod.setId_cantidad(Integer.parseInt(CantidadCar.getNumber()));
    prod.setIdCategoria(Integer.parseInt(""+product.getIdCategoria()));
    prod.setIdProducto(product.getId());

    if(Global.idFiltro==0){
        prod.setIdPuesto(Integer.parseInt(idPuestoI));
        prod.setIdVendedor(vendedor.getId().toString());
    }

    if(Global.idFiltro==1){
        prod.setIdPuesto(tienda.getId());
        prod.setIdVendedor(""+tienda.getIdUsuario());
    }

    prod.setPrecio(Double.parseDouble(""+product.getPrecio()));
    prod.setUnidades(""+product.getUnidades());

    Double multi=prod.getId_cantidad()*prod.getPrecio();

    Double total= Global.formatearDecimales(multi,2);

    String f=df.format(total);
    try {
        total=DecimalFormat.getNumberInstance().parse(f).doubleValue();

        //("total" , "------------->"+total);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    prod.setTotal(total);

    lstprod.add(prod);


    if(Global.idFiltro==0){
        Log.e("el id del puesto es",""+ID);
        PuestComp.setId(ID);
        PuestComp.setCodigoPuesto(idPuesto);
        PuestComp.setVendedor(vendedor);

        Log.e("puesto ",Global.convertObjToString(PuestComp)) ;

    }
    if(Global.idFiltro==1){
        PuestComp.setId(tienda.getId());
    }






    PuestComp.setProductos(lstprod);
    pues.add(PuestComp);


    nuevoC.setPuestos(pues);
    nuevoC.setCantidad(Integer.parseInt(CantidadCar.getNumber()));


    if(Global.idFiltro==0){
        nuevoC.setCodigoMercado(Mercado.getCodigoMercado());
        nuevoC.setId(Mercado.getId());
        nuevoC.setNombre(Mercado.getNombre());
        nuevoC.setDireccion(Mercado.getDireccion());
        if(Mercado.getLatitud()!=null){
            nuevoC.setLatitud(Mercado.getLatitud().toString());
            nuevoC.setLongitud(Mercado.getLatitud().toString());
        }
    }

    if(Global.idFiltro==1){
        nuevoC.setId(tienda.getId());
        nuevoC.setNombre(tienda.getNombre());
        nuevoC.setIdUsuario(tienda.getIdUsuario());

    }

    nuevoC.setTipoCarro(Global.idFiltro);//Tipo 0 es Mercado Tipo 1 es Tiendas
    // nuevoC.setLatitud(""+Mercado.getLatitud().toString());
    // nuevoC.setLongitud(""+Mercado.getLatitud().toString());
    nuevoC.setTotal(total);

    Log.e("mercado-puesto",Global.convertObjToString(nuevoC)) ;
    Global.Agregar_Carrito(nuevoC);

}

    private void click(){

        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"La busqueda esta opcional pero esta puesto el prototipo si pide un update $ ",Toast.LENGTH_LONG).show();



                if(Global.Modo==1){
                    carrito car = new carrito();
                    car.id_del_fragment="frag_car";
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Contenedor_Fragments, car).addToBackStack("frag_car");
                    fragmentTransaction.commit();
                }else if(Global.Modo==2 || Global.Modo==3){
                   // add_pro.id_del_fragment="frag_car";

                    agregar_productos prod = new agregar_productos();
                    if(Global.Modo==3)
                    prod.idNegocio=tienda.getId();

                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Contenedor_Fragments, prod).addToBackStack("frag_agg");
                    fragmentTransaction.commit();

                }



            }
        });



       buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filtro(editable.toString());
                //  filter(editable.toString());
            }
        });


    }

    public void filtro(String S){
        if(adapter!=null)
            adapter.getFilter().filter(S);
        if(adapter2!=null)
            adapter2.getFilter().filter(S);
    }

    private void peticion_ProductosPorID(){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;

        disposable = (Disposable) retrofitApi.VerProductosPorPuesto(""+Global.LoginU.getId_puesto(), "si",Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseVerAllPuesto>>() {
                    @Override
                    public void onNext(Response<ResponseVerAllPuesto> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            TiendaPorId=response.body();
                           // cambio_pantalla=true;
                           // Global.RegisUser=response.body();
                          //  Global.LoginU.setid(response.body().getId());
                            mensaje="Productos obtenidos";
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);

                                mensaje=staff.getMensaje();

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                      //  pDialog.dismiss();
                        //("ProductosTienda", "fallo");
                    }

                    @Override
                    public void onComplete() {
                        //("Completado",Global.convertObjToString(TiendaPorId));


                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido ");
                            return;
                        }else{

                        ls_listado.clear();

                       for (Productos x:TiendaPorId.getProductos()){
                           if(x.getEstado()==1){
                               ls_listado.add(x);
                           }


                          // ListProdcutsPorPuesto.add(x.getProductos());
                           // adapter.notifyDataSetChanged();

                        }
                       iniciar_recycler();
                       adapter.notifyDataSetChanged();

                       if(Global.Modo==2){
                            llenarDatosVendedor();

                       }

/*                        if(!cambio_pantalla){

                            Snackbar.make(vista,""+mensaje, Snackbar.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }else{
                            subir_foto();
                        }*/


                        }

                    }
                });
    }



    private void peticion_ProductosporTienda(String tipoNegocio, int IdTien){

        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;

        disposable = (Disposable) retrofitApi.VerProductosPorTienda( tipoNegocio,""+IdTien, Global.LoginU.getToken() )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<Productos>>>() {
                    @Override
                    public void onNext(Response<List<Productos>> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            ls_listado=response.body();
                            Log.e("Estoy en if","");
                            Log.e("productos",Global.convertObjToString(response.body()));
                            // cambio_pantalla=true;
                            // Global.RegisUser=response.body();
                            //  Global.LoginU.setid(response.body().getId());
                            mensaje="Productos obtenidos";
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);

                                mensaje=staff.getMensaje();

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        //  pDialog.dismiss();
                        //("ProductosTienda", "fallo");
                    }

                    @Override
                    public void onComplete() {
                        //("Completado",Global.convertObjToString(TiendaPorId));


                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido ");
                            return;
                        }else{

                            List<Productos> muestreo =new ArrayList<>();
                            muestreo.addAll(ls_listado);

                            Log.e("productos",Global.convertObjToString(muestreo));

                            ls_listado.clear();
                            for (Productos x:muestreo){
                                if(x.getEstado()==1){
                                    ls_listado.add(x);
                                }

                                // ListProdcutsPorPuesto.add(x.getProductos());
                                // adapter.notifyDataSetChanged();

                            }

                            for(Productos p : ls_listado){


                                for(PromosProductos pro:lst_normal){
                                    if(p.getPromocionNombre().equals(pro.getNombrePromo())){

                                       lst_normal.get( lst_normal.indexOf(pro)).getLst_products().add(p);
                                    }
                                }

                            }



                            List<PromosProductos> nuevo =new ArrayList<>();
                            for(PromosProductos pro :lst_normal){

                                if(pro.getLst_products().size()>0){
                                    nuevo.add(pro);
                                }
                            }


                            lst_normal.clear();
                            lst_normal.addAll(nuevo);
                            Log.e("todo",""+ Global.convertObjToString(lst_normal));



                            //iniciar_recycler();
                            //adapter.notifyDataSetChanged();
                            iniciar_recycler2();
                            llenarDatosTiendero();

                        }

                    }
                });
    }

private void llenarDatosVendedor(){

    NombreDueno.setText(PrimeraMayusculaNP(Global.LoginU.getNombres()+" "+Global.LoginU.getApellidos()));

    Cantidadpro.setText(""+(ls_listado.size()) +" Productos");


    Idpuesto.setText(TiendaPorId.getCodigo());
    if(Global.Modo==3) Idpuesto.setText("");

   // String url= Global.UrlImagen+" images/profile-"+lst_normal.get(position).getIdVendedor()+".jpg";

    String LinkImagenP= Global.UrlImagen+"images/profile-"+Global.LoginU.getid()+".jpg";

    Glide
            .with(this)
            .load(LinkImagenP)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.placeholder_perfil)
            .error(R.drawable.placeholder_perfil)
            .fitCenter()
            .into(FotoPuesto);
    String cate=""+TiendaPorId.getMaxCategorias();
    if(ls_listado.size()<1)
   DescripcionPuesto.setText("");

    else
    DescripcionPuesto.setText(""+cate);

}



private void llenarDatosTiendero(){
    NombreDueno.setText(PrimeraMayusculaNP(tienda.getNombre()));
    String LinkImagenP= Global.UrlImagen+tienda.getUrlImagen();
    Glide
            .with(this)
            .load(LinkImagenP)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.placeholder_perfil)
            .error(R.drawable.placeholder_perfil)
            .fitCenter()
            .into(FotoPuesto);
   // Cantidadpro.setText(""+(ls_listado.size()) +" Productos");
    // String cate=""+TiendaPorId.getMaxCategorias();
   //Idpuesto.setText(TiendaPorId.getCodigo());

    Cantidadpro.setText(""+(ls_listado.size()) +" Productos");
    DescripcionPuesto.setText(tienda.getDescripcion());
    }


    Boolean confirmacion=false;
    private void EliminarProducto(Productos produc, int position){
        mensaje="Servidor";
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        disposable = (Disposable) retrofitApi.EliminarProducto(""+produc.getId(),Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseError>>() {
                    @Override
                    public void onNext(Response<ResponseError> response) {

                        if (response.isSuccessful()) {
                            // cambio_pantalla =true;
                            mensaje=response.body().getMensaje();
                            confirmacion=true;
                            //("normal",mensaje);
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


                        if(confirmacion){

                            borrar_producto(position);

                        }
                            pDialog.dismiss();

                        Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Eliminando");
        pDialog.setCancelable(false);



    }

    private void borrar_producto(int position){
        animator1.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator1);
        // eliminar("http://learn4win.com/WebServices/eliminar_alarma.php",malarma.get(position));
        ls_listado.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void peticion_promociones( String id_tienda, boolean band){
        retrofit = RetrofitCliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;
        // JsonObject convertedObject = new Gson().fromJson(jsonConf, JsonObject.class);

        disposable = (Disposable) retrofitApi.verPromocionesTiendas(id_tienda,Global.LoginU.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<RespPromociones>>>() {
                    @Override
                    public void onNext(Response<List<RespPromociones>> response) {

                        //("code PU",""+response.code());
                        if (response.isSuccessful()) {
                            // cambio_pantalla=true;
                            // Global.RegisUser=response.body();
                            // Global.LoginU.setid(response.body().getId());
                            //mensaje=response.body().getMensaje();
                            // listPromociones.addAll(response.body());
                            list_respPromociones.clear();
                            list_respPromociones.addAll(response.body());
                            lst_normal.clear();
                           // listPromociones.clear();
                            for(RespPromociones r: response.body()){
                                PromosProductos promspro= new PromosProductos();
                                promspro.setNombrePromo(r.getNombre());
                                lst_normal.add(promspro);

                            }
                            //spinnerArrayAdapter3.notifyDataSetChanged();
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson =new Gson();
                                ResponseError staff = gson.fromJson(jObjError.toString(), ResponseError.class);
                                mensaje=staff.getMensaje();

                            } catch (Exception e) {
                                //("error conversion json",""+e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        pDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                        if(getActivity()==null || isRemoving() || isDetached()){
                            //("activity","removido ");
                            return;
                        }
                        Log.e("complete", ""+Global.convertObjToString(lst_normal));
                        peticion_ProductosporTienda("TIENDA", tienda.getId());

                        //("edicion","exito");
                       // Log.e("list de promociones", Global.convertObjToString(listPromociones));
                      /*  if(band){
                            Log.e("on complete", "estoy en el if band");
                            int auxPromo=0;
                            Log.e("size list", ""+list_respPromociones.size());
                            Log.e("nombre promo",product.getPromocionNombre() );
                            for(RespPromociones list: list_respPromociones){
                                if (list.getNombre().equals(product.getPromocionNombre()) ){
                                    auxPromo=list_respPromociones.indexOf(list);
                                    Log.e("compara promos", "si");
                                }
                            }
                           // SpPromociones.setSelection(auxPromo);
                        }*/

                    }
                });
    }
}
