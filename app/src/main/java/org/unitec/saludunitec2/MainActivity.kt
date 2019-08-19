package org.unitec.saludunitec2

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MainActivity : AppCompatActivity() {

    var perfil= Perfil()

    var estatus = EstatusPerfil()
    var nip: String? = null
    var mensajeError: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();
        //Ajustamos el spinner del pefil
        //Ajustamos el spinner

        var spinner = findViewById<Spinner>(R.id.spinnerPerfil)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.perfil, android.R.layout.simple_spinner_item
        )
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
// Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var item = parent?.getItemAtPosition(position)
                //El siguiente funciona bien
                //Toast.makeText(applicationContext,"Este es "+item,Toast.LENGTH_SHORT).show()
            }
        }


        //   findViewById<Button>(R.id.botonRegistrarse).setOnClickListener {
        //       var i = Intent(application, MainActivity::class.java);
        //        startActivity(i);
        //   }


        findViewById<Button>(R.id.botonIngresar).setOnClickListener {
            var item = spinner?.selectedItem
            Toast.makeText(applicationContext, "el perfil es " + item, Toast.LENGTH_LONG).show()
            if (item!!.equals("Coordinador")) {
                // TareaAutenticar().execute(null, null, null);
                perfil?.rol="coordinador"
            }
            if (item!!.equals("Profesor")) {
                perfil?.rol="profesor"

            }
            if(item!!.equals("Director Académico")){
                perfil?.rol="director-academico"
            }
            if(item!!.equals("Director Divisional")){
                perfil?.rol="director-divisional"
            }
            if(item!!.equals("Dirección de Campos")){
                perfil?.rol="direccion-campos"
            }

            TareaAutenticar().execute(null, null, null);

        }
    }

    inner class TareaAutenticar : AsyncTask<Void, Void, Void>() {
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            nip = null
            if (!estatus.success) {


                var txtError = findViewById<TextView>(R.id.txtErrorNip)
                txtError.visibility = View.VISIBLE
                txtError.text = "NIP incorrecto"
            } else {
                var txtError = findViewById<TextView>(R.id.txtErrorNip)
                txtError.visibility = View.INVISIBLE



                Toast.makeText(applicationContext, "El id de acceso es " + estatus.perfil?.campus+"", Toast.LENGTH_SHORT).show()

                Globales.campus=estatus.perfil?.campus
                var i = Intent(applicationContext, MenuActivity::class.java)
                //ASIGNAMOS EL ID DE ACCESO ESTE ES SUMAMENTE IMPORTANTE
                //YA QUE CON EL HAREMOS LAS ACTUALIZACIONES ETC.
                perfil.id=estatus.perfil?.id
                //Ajustamos el perfil para de esta manera, tener acceso a cada perfil
                Globales.estatusPerfil?.perfil=perfil



                startActivity(i)
            }
            var progreso = findViewById<ProgressBar>(R.id.progressBar1)
            progreso.visibility = View.GONE


        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                var url2 = Globales.url + "/academico"
                //  var url2="http://192.168.100.12/api/academico/"+33868

                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


                val maper = ObjectMapper()
                //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

                val respuesta = restTemplate.postForObject(url2,perfil, String::class.java)
                if (respuesta != null) {
                    estatus = maper.readValue(respuesta, EstatusPerfil::class.java)

                } else {
                    // perfil = null;
                }
                print("El rol es" + perfil?.rol)
                Globales.estatusPerfil=estatus
                Globales.estatusPerfil?.perfil?.nip = nip;


                println("DESPUES DE REST " + Globales?.estatusPerfil?.perfil?.rol);
                println("DESPUES DE REST " + Globales?.estatusPerfil?.perfil?.nip);
                println("DESPUES DE REST " + Globales?.estatusPerfil?.perfil?.campus);

            } catch (t: Exception) {
                println("Otra cosa paso" + t.localizedMessage);
            }
            return null

        }


        override fun onPreExecute() {
            super.onPreExecute()
            estatus=EstatusPerfil();
            var progreso = findViewById<ProgressBar>(R.id.progressBar1)
            progreso.visibility = View.VISIBLE

            try {
                nip = findViewById<EditText>(R.id.textonip).text.toString();
                perfil.nip=nip;
                estatus.success=false

                //Toast.makeText(applicationContext, "NIP de acceso " + nip +" y el rol es "+perfil?.rol, Toast.LENGTH_LONG).show()
            } catch (e: NumberFormatException) {

                mensajeError = "Introduce tu NIP"

            }
        }
    }




    /*
    PARA LA GEOLOCALIZACION
     */
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */

}
