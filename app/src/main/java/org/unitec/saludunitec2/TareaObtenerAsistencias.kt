package org.unitec.saludunitec2


import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.android.synthetic.main.perfil.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class TareaObtenerAsistencias(
    private var ctx: Context?,
    private var activity: MenuActivity
) : AsyncTask<Void, Void, Void>() {

  var  asistencias:Array<Practica>?=null

    override fun onPreExecute() {
        super.onPreExecute()
//Toast.makeText(ctx, "El profe es "+Globales.estatusPerfil?.perfil?.id, Toast.LENGTH_LONG).show()


    }

    override fun doInBackground(vararg params: Void?): Void? {

        try {
            var url2 = Globales.url + "/practica/profesor/${Globales.estatusPerfil?.perfil?.id}";
            //  var url2="http://192.168.100.7:8080/api/practica"

            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


            val maper = ObjectMapper()
            //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

            val respuesta = restTemplate.getForObject(url2, String::class.java)

             asistencias = maper.readValue(respuesta, Array<Practica>::class.java)
          Globales.asistencias=asistencias


            println("DESPUES DE REST DE OBTENER ASISTENCIAS ");
        } catch (t: Throwable) {
            //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

            print("HAAAAAGH   " + t.message);
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        val asis=asistencias

    }
}

