package org.unitec.saludunitec2

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlinx.android.synthetic.main.activity_menu.*




class TareaGuardarToken(private var ctx: Context?, private var token:String?,
                            private var activity:MenuActivity?)
    : AsyncTask<Void, Void, Void>() {

    var estatus = Estatus()



    override fun onPostExecute(result: Void?) {

        //Invocamos nuestra visita del MainActivity
        //  activity?.findViewById<TextView>(R.id.txtActual)?.text=estacion?.temp_c
        Toast.makeText(ctx, estatus.mensaje, Toast.LENGTH_LONG).show()


    }

    override fun onPreExecute() {
        super.onPreExecute()
        //GENERAMOS UN USUARIO quitarlos de aqui y ponerlo en menuactivity previo a invocar el excute
        Globales.estatusPerfil?.perfil?.token=token

      //  Toast.makeText(ctx,"antes   "+Globales.estatusPerfil?.perfil?.token , Toast.LENGTH_LONG).show()


    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {

            var retrofit = Retrofit.Builder()
                .baseUrl(Globales.url+"/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
            var servicioToken = retrofit.create(ServicioToken::class.java)


            var envio = servicioToken.guardarToken(Globales.estatusPerfil?.perfil!!)

            estatus = envio.execute().body()!!

        } catch (t: Throwable) {
            println("ERROR" + t.message)
        }
        return null

    }
}
