package org.unitec.saludunitec2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_semaforo.*

class SemaforoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semaforo)
        textoNombreInci.text=Globales.incidencia?.nombre
        botonSemaforo.setOnClickListener {
            var i= Intent(this, MainActivity::class.java)

       var idSeleccionado=     radios.checkedRadioButtonId
      var textoSemaforo=      findViewById<RadioButton>(idSeleccionado).text.toString();
            Globales.incidencia?.semaforo=textoSemaforo;
           // Toast.makeText(this, textoSemaforo,Toast.LENGTH_LONG).show()
            TareaActualizarIncidencia(this,this).execute()
           startActivity(i)
        }
    }
}
