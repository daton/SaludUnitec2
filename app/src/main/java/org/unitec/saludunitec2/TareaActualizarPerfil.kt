package org.unitec.saludunitec2

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.android.synthetic.main.perfil.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class TareaActualizarPerfil(
    private var ctx: Context?,
    private var perfil: Perfil?,
    private var estatusPerfil: EstatusPerfil?,
    private var activity: MenuActivity
) : AsyncTask<Void, Void, Void>() {


    override fun onPreExecute() {
        super.onPreExecute()

        //Generamos el perfil a partir de la varibles globales


        //Cambiamos el nip al nuevo
        perfil?.nip = activity.txtPassword.text.toString()


        Toast.makeText(ctx, "Datos  " + perfil?.rol + " passwd " + perfil?.nip+" id "+Globales.estatusPerfil?.perfil?.id, Toast.LENGTH_SHORT).show();


    }

    override fun doInBackground(vararg params: Void?): Void? {

        try {
            var url2 = Globales.url + "/academico/actualizar";
            //  var url2="http://192.168.100.7:8080/api/practica"

            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


            val maper = ObjectMapper()
            //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

            val respuesta = restTemplate.postForObject(url2, perfil, String::class.java)

            estatusPerfil = maper.readValue(respuesta, EstatusPerfil::class.java)


            println("DESPUES DE REST");
        } catch (t: Throwable) {
            //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

            print("HAAAAAGH   " + t.message);
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Toast.makeText(ctx, estatusPerfil?.mensaje, Toast.LENGTH_LONG).show()
    }
}

