package com.mercadoonline.tiendaclient.service;


import com.mercadoonline.tiendaclient.models.recibido.Productos;
import com.mercadoonline.tiendaclient.models.recibido.RespNewPromo;
import com.mercadoonline.tiendaclient.models.recibido.RespPromociones;
import com.mercadoonline.tiendaclient.models.recibido.ResponseCategorias;
import com.mercadoonline.tiendaclient.models.recibido.ResponseDetallesPedidos;
import com.mercadoonline.tiendaclient.models.recibido.ResponseError;
import com.mercadoonline.tiendaclient.models.recibido.ResponseNombresMercado;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistarProducto;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistrarPedido;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUserPorID;
import com.mercadoonline.tiendaclient.models.recibido.ResponseLoginUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseRegistroUser;
import com.mercadoonline.tiendaclient.models.recibido.ResponseUpdateImagen;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerAllPuesto;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerMercado;
import com.mercadoonline.tiendaclient.models.recibido.ResponseVerPedido;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("usuarios")
    Observable<Response<ResponseRegistroUser>>RegistroUser(@Body JsonObject object);


    @Headers("Content-Type: application/json")
    @GET("usuarios")
    Observable<Response<List<ResponseUserPorID>>>ObtenerTransportistas(@Query("tipo") String ValueChild ,@Header("Authorization") String authorization);







    @Headers("Content-Type: application/json")
    @POST("auth")
    Observable<Response<ResponseLoginUser>>LoginUser(@Body JsonObject object);


    @Multipart
    @POST("usuarios/{user_id}/foto")
    Observable<Response<ResponseUpdateImagen>>UploadImage(@Path(value = "user_id", encoded = true) String userId,
                                                          @Part MultipartBody.Part imageFile,
                                                          @Header("Authorization") String authorization
                                                          );



    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("mercados?childs=si")
    Observable<Response<List<ResponseVerMercado>>>VerMercados(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("puestos")
    Observable<Response<List<ResponseVerAllPuesto>>>VerAllPuestos(@Query("childs") String ValueChild ,
                                                                  @Query("id_mercado") String ValueIdPuesto,
                                                                  @Header("Authorization") String authorization

    );

    /*
    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
   Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
}
//puestos?childs=si&id_mercado=1
@GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
     */
//////////////////////
    //


    @Headers("Content-Type: application/json")
    @GET("usuarios/{user_id}")
    Observable<Response<ResponseUserPorID>>ObtenerUsuarioporID(@Path(value = "user_id", encoded = true  ) String userId,@Header("Authorization") String authorization);



    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("pedidos")
    Observable<Response<List<ResponseVerPedido>>>VerPedidosClientes(@Query("type") String type ,
                                                              @Query("id") String id,
                                                                    @Header("Authorization") String authorization
                                                                    );




    @Headers("Content-Type: application/json")//FULL
    @GET("pedidos/{user_id}")
    Observable<Response<ResponseDetallesPedidos>>VerDetallePedidos(@Path(value = "user_id", encoded = true) String userId, @Query("type") String type,
                                                                   @Header("Authorization") String authorization

    );



    @Headers("Content-Type: application/json")
    @POST("pedidos")
    Observable<Response<ResponseRegistrarPedido>>RegistrarPedidos(@Body JsonObject object,
                                                                  @Header("Authorization") String authorization
    );

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("productos/categorias")
    Observable<Response<List<ResponseCategorias>>>VerCategorias(
            @Header("Authorization") String authorization
    );


    //@Headers({"Content-Type: application/json", "multipart/form-data"})
    @Multipart
    @POST("productos")
    Observable<Response<ResponseRegistarProducto>>RegistrarProducto(@Part MultipartBody.Part imageFile,@Part ("payload") RequestBody ObjectJson,
                                                                    @Header("Authorization") String authorization
                                                                    );

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("puestos/{idPuesto}")
    Observable<Response<ResponseVerAllPuesto>>VerProductosPorPuesto(@Path(value = "idPuesto", encoded = true) String idPuest,
                                                                       @Query("childs") String Hijos,
                                                                    @Header("Authorization") String authorization
                                                                    );


///puestos/3?childs=si


    @Headers("Content-Type: application/json")
    @DELETE("productos/{user_id}")
    Observable<Response<ResponseError>>EliminarProducto( @Path("user_id") String x,
                                                         @Header("Authorization") String authorization
                                                         );


    @Headers("Content-Type: application/json")
    @PUT("productos/{user_id}")
    Observable<Response<ResponseRegistarProducto>>EditarProducto( @Path("user_id") String x , @Body JsonObject object,
                                                                  @Header("Authorization") String authorization
                                                                  );

    @Multipart
    @POST("productos/{user_id}/foto")
    Observable<Response<ResponseRegistarProducto>>CambiarFoto( @Path(value = "user_id", encoded = true) String userId,
                                                                  @Part MultipartBody.Part imageFile,
                                                               @Header("Authorization") String authorization
                                                               );

//


    @Headers("Content-Type: application/json")
    @PUT("pedidos/{id}/estado/{estado}")
    Observable<Response<ResponseError>>ActualizarPedido( @Path("id") String id,
                                                         @Path("estado") String estado,
                                                         @Header("Authorization") String authorization
                                                         );

    @Headers("Content-Type: application/json")
    @POST("auth/gc_token")
    Observable<Response<ResponseUpdateImagen>>RegistrarGCToken(@Body JsonObject object,
                                                               @Header("Authorization") String authorization
                                                               );



    @Headers("Content-Type: application/json")
    @GET("mercados/nombres")
    Observable<Response<List<ResponseNombresMercado>>>TraerNombresMercado();

    @Headers("Content-Type: application/json")
    @GET("tiendas/categorias")
    Observable<Response<List<ResponseCategorias>>>TraerCategoriasTiendas(@Header("Authorization") String authorization);




    @Headers("Content-Type: application/json")
    @GET("tiendas")
    Observable<Response<List<ResponseTiendas>>>VerMisTiendas(@Query("id") String id,
                                                       @Query("type") String type,
                                                       @Header("Authorization") String authorization
    );


    @Headers("Content-Type: application/json")
    @GET("tiendas")
    Observable<Response<List<ResponseTiendas>>>VerTiendas(  @Query("lat") String lat ,
                                                            @Query("lng") String lng,
                                                            @Query("categoria") String id_catg_buscar,
                                                             @Header("Authorization") String authorization
    );

    //@Headers({"Content-Type: application/json", "multipart/form-data"})
    //Mismo response pq es igual al de registrar Producto
    @Multipart
    @POST("tiendas")
    Observable<Response<ResponseRegistarProducto>>RegistrarTienda(@Part MultipartBody.Part imageFile,@Part ("payload") RequestBody ObjectJson,
                                                                    @Header("Authorization") String authorization
    );

    @Headers("Content-Type: application/json")
    //cambiar y pasar por parametros, en list poner la clase q cojo de pojo
    @GET("productos")
    Observable<Response<List<Productos>>>VerProductosPorTienda(@Query("type") String tipoNegocio,
                                                               @Query("id") String idTienda,
                                                               @Header("Authorization") String authorization
    );

    @Headers("Content-Type: application/json")//FULL
    @GET("tiendas/{id_tienda}/promociones")
    Observable<Response<List<RespPromociones>>>verPromocionesTiendas(@Path(value = "id_tienda", encoded = true) String idTienda,
                                                                    @Header("Authorization") String authorization

    );

    @Headers("Content-Type: application/json")//FULL
    @POST("tiendas/{id_tienda}/promociones")
    Observable<Response<RespNewPromo>>nuevaPromoTienda(@Path(value = "id_tienda", encoded = true) String idTienda,
                                                       @Body JsonObject object,
                                                       @Header("Authorization") String authorization

    );


}
