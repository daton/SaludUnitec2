package org.unitec.saludunitec2


import retrofit2.Call
import retrofit2.http.*

interface ServicioIncidencia {


    @POST("mensajeria/incidencia/actualizar")
    fun actualizarIncidencia(@Body incidencia: Incidencia): Call<Estatus>

    //Servicio REST para actualizar un cliente por su ident
    //  @PUT("api/red-social")
//    fun actualizarRedSocial(@Body cliente: RedSocial): Call<Estatus>

    // Servicio REST para borrar un cliente por su ident
    //  @DELETE("api/red-social/{token}")
    //  fun borrarRedSocial(@Path("token") ident: Int?): Call<Estatus>

    //Servicio REST para buscar un cliente por su ident
    // @GET("api/red-social/{token}")
    //  fun buscarRedSocial(@Path("token") token:Int?): Call<RedSocial>

    //Servicio REST para buscar un cliente por su ident
    // @GET("api/red-social/")
    //  fun buscarRedSocialTodos(): Call<ArrayList<RedSocial>>

    //   @GET("api/mensajeria/{nickname}/{mensaje}")
    // fun enviarMensaje(@Path("nickname")nickname:String?,@Path("mensaje") mensaje:String? ):Call<Estatus>

}
