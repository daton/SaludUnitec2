package org.unitec.saludunitec2

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import kotlinx.android.synthetic.main.activity_main.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MainActivity : AppCompatActivity(), Validator.ValidationListener {
    @NotEmpty(message = "este campo es requerido")
    @Email(message = "correo inválido")
    private var email: TextInputEditText? = null

    @NotEmpty(message = "es necesario el password")
    @Password(min = 4, scheme = Password.Scheme.ANY, message = "NIP no válido")
    private var password: TextInputEditText? = null

    var perfil= Perfil()

    var estatus = EstatusPerfil()
    var nip: String? = null
    var mensajeError: String? = null


    //Probar validaciones
    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        //Aqui van las acciones  a tomar en caso de validaciones erroneas
        //Malo
        var mensa = "men"

        for (error in errors!!) {
            val view = error.view

            mensa = error.getCollatedErrorMessage(applicationContext)
            Toast.makeText(applicationContext, mensa, Toast.LENGTH_LONG).show()
        }
    }

    override fun onValidationSucceeded() {
        //Aqui van acciones a tomar si la validación fue exitosa, por ejemplo navegacion a otro activity
        //Ya quitamos todo del presenter solo invocamos la Tarea autenticar
        //Un pequeño cambio

        TareaAutenticar().execute(null, null, null);

    }

    ///////////////////////////////////////
    //Acaba la validacion y comienza el oncreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();






        var validator = Validator(this);
        validator.setValidationListener(this);
        email = textoemail
        password = textonip

        findViewById<Button>(R.id.botonIngresar).setOnClickListener {

            validator.validate();



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
              var  email=findViewById<EditText>(R.id.textoemail).text.toString()
                perfil.nip=nip;
                perfil.email=email
                estatus.success=false

                //Toast.makeText(applicationContext, "NIP de acceso " + nip +" y el rol es "+perfil?.rol, Toast.LENGTH_LONG).show()
            } catch (e: NumberFormatException) {

                mensajeError = "Introduce tu NIP"

            }
        }
    }

    /********************************************************************************
     * TERMINA TAREA AUTENTICACION
     *************************************************************************************/


    /*
    PARA LA GEOLOCALIZACION
     */
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */

}
