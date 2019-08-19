package org.unitec.saludunitec2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_semaforo.*

class SemaforoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semaforo)

        botonSemaforo.setOnClickListener {
            var i= Intent(this, MainActivity::class.java)
           startActivity(i)
        }
    }
}
