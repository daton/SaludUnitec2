package org.unitec.saludunitec2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.android.synthetic.main.incidencias.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

import java.util.*

class MenuActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var mylocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null




    var perfil: Perfil? = null
    var practica: Practica?=null
    var estatus = EstatusPerfil()
    var nip: String? = null
    var mensajeError:String?=null


    //Para practica
    var dia:Int?=null
    var diaSemana:Int?=null
    //El siguiete es el momento es sumamente importantes asignarlo para cada
    //uno de los tres check ins de los 3 momentos
    var momento:String?=null
    var diano:Int?=null;
    var miId:String?=null


    var miLati:Double?=null
    var miLongi:Double?=null

    var mensaje= Perfil()
    var incidencia= Incidencia()

    var fecha: Calendar?=null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    //LO hacemos



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        setUpGClient()
        getMyLocation()

        //Checamos el id de acceso
     //   Toast.makeText(applicationContext, "El id de acceso es... " +  Globales.estatusPerfil?.perfil?.id, Toast.LENGTH_SHORT).show()


//Checamos si el rol es de directores, si es asi, pues muy facil!! no mostramos el chequeo
//ni tampoco mos asistencias
        if(Globales.estatusPerfil?.perfil?.rol.equals("director-academico")|| Globales?.estatusPerfil?.perfil?.rol.equals("director-divisional")|| Globales.estatusPerfil?.perfil?.rol.equals("direccion-campos")){

            var item=       nav_view.menu.findItem(R.id.menuchequeo);
            item.setVisible(false)

            var itemMisAsistencias=nav_view.menu.findItem(R.id.menumisasistencias)
            itemMisAsistencias.setVisible(false)
        }
        if(Globales?.estatusPerfil?.perfil?.rol.equals("profesor")){
            var item=       nav_view.menu.findItem(R.id.reportes);
            item.setVisible(false)
        }



        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)






        //Mostramos solo el contenido0 principal y en el se especifica el perfil que ingresó

        mostrarPerfil()




        //
        //Ajustamos el spinner

        var spinner=   findViewById<Spinner>(R.id.spinnerIncidencias)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.planets_array, android.R.layout.simple_spinner_item)
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
// Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var item=        parent?.getItemAtPosition(position)
                //El siguiente funciona bien
                //Toast.makeText(applicationContext,"Este es "+item,Toast.LENGTH_SHORT).show()
            }

        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Guardamos la incidencia
//////////////////////////////////////////////////////////////////////////////////////////////////
        findViewById<Button>(R.id.guardarIncidencia).setOnClickListener{


            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@MenuActivity)
                .setTitle("Segundo Momento")
                .setMessage("¿Deseas guardar esta incidencia?")
                .setPositiveButton("SI"){dialog, which ->

                    TareaRegistrarIncidencia().execute(null,null,null)

                }.setNegativeButton("No"){dialog,which ->
                    Toast.makeText(applicationContext,"Incidencia no guardado.",Toast.LENGTH_SHORT).show()
                }

            val dialog: AlertDialog = builder.create()

            dialog.show()



            }








        /***************************************************************************************************************************************
        Inicia boton registrar ingreso

         ************************************************************************ ***************************************************************/

       //Segundo momento
        findViewById<Button>(R.id.check).setOnClickListener {



            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@MenuActivity)
                .setTitle("Segundo Momento")
                .setMessage("¿Deseas guardar tu registro de ingreso?")
                .setPositiveButton("SI"){dialog, which ->

                    TareaRegistrarseCheck().execute(null,null,null)

                }.setNegativeButton("No"){dialog,which ->
                    Toast.makeText(applicationContext,"Momento no guardado.",Toast.LENGTH_SHORT).show()
                }

            val dialog: AlertDialog = builder.create()

            dialog.show()



        }

        //Primer momento
        findViewById<Button>(R.id.checkIn).setOnClickListener {

            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@MenuActivity)
                .setTitle("Primer Momento")
                .setMessage("¿Deseas guardar tu registro de ingreso?")
                .setPositiveButton("SI"){dialog, which ->

                    TareaRegistrarseCheckIn().execute(null,null,null)

                     }.setNegativeButton("No"){dialog,which ->
                Toast.makeText(applicationContext,"Momento no guardado.",Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()


        }

        findViewById<Button>(R.id.checkOut).setOnClickListener {


            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@MenuActivity)
                .setTitle("Tercer Momento")
                .setMessage("¿Deseas guardar tu salida?")
                .setPositiveButton("SI"){dialog, which ->

                    TareaRegistrarseCheckOut().execute(null,null,null)

                }.setNegativeButton("No"){dialog,which ->
                    Toast.makeText(applicationContext,"Momento no guardado.",Toast.LENGTH_SHORT).show()
                }

            val dialog: AlertDialog = builder.create()

            dialog.show()






        }

        findViewById<Button>(R.id.actualizarPerfil).setOnClickListener {
            TareaActualizarPerfil(applicationContext, Globales.estatusPerfil?.perfil,estatus,this).execute(null,null,null)
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.menuchequeo -> {
                // Handle the camera action
                ocultarTodo()
                val principal = findViewById(R.id.chequeo) as ConstraintLayout
                principal.visibility = View.VISIBLE
                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


            }
            R.id.menuincidencias -> {
                ocultarTodo()
                val incidencias = findViewById(R.id.incidencias) as ConstraintLayout
                incidencias.visibility = View.VISIBLE
                //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)




            }
            R.id.menumisasistencias->{
                ocultarTodo()


                TareaObtenerAsistencias(this,this).execute(null,null,null).get()
              //Ahora si
                val asistencias=Globales.asistencias

                Toast.makeText(this, "Se cargaron correctamente las asistencisas"+asistencias?.size, Toast.LENGTH_LONG).show()

                val lv = findViewById<ListView>(R.id.colocar_asistencias)
                val practicaAdapter = PracticaAdapter(asistencias,this)
                lv.adapter = practicaAdapter
                lv.dividerHeight = 2


                lv.onItemClickListener = AdapterView.OnItemClickListener {
                        adapterView, view, i, l ->
                    Toast.makeText(this@MenuActivity,
                        "Seleccionaste " + (i + 1),
                        Toast.LENGTH_LONG).show()
                }

                val misasistencias=findViewById<LinearLayout>(R.id.misasistencias)
                misasistencias.visibility=View.VISIBLE



            }

            R.id.reportes->{
                ocultarTodo()
                val reportes = findViewById(R.id.reportes) as ConstraintLayout
                reportes.visibility = View.VISIBLE
                //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            R.id.menuperfil->{
                ocultarTodo()
                val perfil = findViewById(R.id.perfil) as ConstraintLayout
                perfil.visibility = View.VISIBLE
                //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun ocultarTodo(){
        val principal = findViewById(R.id.principal) as ConstraintLayout
        val incidencias = findViewById(R.id.incidencias) as ConstraintLayout
        val misaistencias=findViewById(R.id.misasistencias) as LinearLayout
        val chequeo = findViewById(R.id.chequeo) as ConstraintLayout
        val directores = findViewById(R.id.directores) as ConstraintLayout
        val coordinadores = findViewById(R.id.coordinadores) as ConstraintLayout
        val reportes=findViewById(R.id.reportes) as ConstraintLayout
        val perfil=findViewById(R.id.perfil) as ConstraintLayout


        principal.visibility = View.INVISIBLE
        incidencias.visibility = View.INVISIBLE
        misaistencias.visibility=View.INVISIBLE
        chequeo.visibility= View.INVISIBLE
        directores.visibility= View.INVISIBLE;
        coordinadores.visibility= View.INVISIBLE
        reportes.visibility= View.INVISIBLE
        perfil.visibility= View.INVISIBLE
    }

    inner class TareaMensaje : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {




            var url2= Globales.url+"/mensaje"

            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


            val maper = ObjectMapper()
            //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

            val respuesta = restTemplate.postForObject(url2, mensaje, String::class.java)
            estatus = maper.readValue(respuesta, EstatusPerfil::class.java )
            print(estatus.mensaje)

            println("DESPUES DE REST");
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            //Se piden las componentes
            // var eCuerpo=     findViewById<EditText>(R.id.textoCuerpo)
            //  mensaje.cuerpo=    eCuerpo.text.toString();
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext,estatus.mensaje, Toast.LENGTH_LONG).show();
            //  findViewById<EditText>(R.id.textoCuerpo).text=null
        }
    }
    /*****************************************************************************************************************
    REGISRTO DE PRACTICA-PROFESOR
     ***************************************************************************************************************/
    inner class  TareaRegistrarseCheckIn : AsyncTask<Void, Void, Void>(){

        override fun onPreExecute() {
            super.onPreExecute()

            //Generamos o llenamos la practica



            obtenerUbicacion("checkin")




        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                var url2 = Globales.url+"/practica";
                //  var url2="http://192.168.100.7:8080/api/practica"

                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                print("ZZZZZZZZZZZZZZZZZZZZZZZ " + practica?.id);

                val maper = ObjectMapper()
                //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

                val respuesta = restTemplate.postForObject(url2, practica, String::class.java)

                estatus = maper.readValue(respuesta, EstatusPerfil::class.java)
                // else estatus = null
                print("El rol es" + perfil?.rol)

                println("DESPUES DE REST");
            }catch(t:Throwable){
                //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

                print("HAAAAAGH   "+t.message);
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, "mensaje:"+estatus.mensaje, Toast.LENGTH_LONG).show()
        }
    }
    inner class  TareaRegistrarseCheck : AsyncTask<Void, Void, Void>(){

        override fun onPreExecute() {
            super.onPreExecute()

            //Generamos o llenamos la practica



            obtenerUbicacion("checkstay")




        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                var url2 = Globales.url+"/practica";
                //  var url2="http://192.168.100.7:8080/api/practica"

                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                print("ZZZZZZZZZZZZZZZZZZZZZZZ " + practica?.id);

                val maper = ObjectMapper()
                //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

                val respuesta = restTemplate.postForObject(url2, practica, String::class.java)

                estatus = maper.readValue(respuesta, EstatusPerfil::class.java)
                // else estatus = null
                print("El rol es" + perfil?.rol)

                println("DESPUES DE REST");
            }catch(t:Throwable){
                //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

                print("HAAAAAGH   "+t.message);
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, "mensaje:"+estatus.mensaje, Toast.LENGTH_LONG).show()
        }
    }
    inner class  TareaRegistrarseCheckOut : AsyncTask<Void, Void, Void>(){

        override fun onPreExecute() {
            super.onPreExecute()

            //Generamos o llenamos la practica


            obtenerUbicacion("checkout")




        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                var url2 = Globales.url+"/practica";
                //  var url2="http://192.168.100.7:8080/api/practica"

                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                print("ZZZZZZZZZZZZZZZZZZZZZZZ " + practica?.id);

                val maper = ObjectMapper()
                //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

                val respuesta = restTemplate.postForObject(url2, practica, String::class.java)

                estatus = maper.readValue(respuesta, EstatusPerfil::class.java)
                // else estatus = null
                print("El rol es" + perfil?.rol)

                println("DESPUES DE REST");
            }catch(t:Throwable){
                //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

                print("HAAAAAGH   "+t.message);
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, "mensaje:"+estatus.mensaje, Toast.LENGTH_LONG).show()
        }
    }
    inner class  TareaRegistrarIncidencia : AsyncTask<Void, Void, Void>(){

        override fun onPreExecute() {
            super.onPreExecute()

            //Generamos o llenamos la incidencia
            incidencia= Incidencia();

            var item=   spinnerIncidencias?.selectedItem
            incidencia.nombre=item.toString()
            //    Toast.makeText(applicationContext,"Seleccionaste "+incidencia.nombre, Toast.LENGTH_SHORT).show();




            incidencia.idProfesor= Globales.estatusPerfil?.perfil?.id
            incidencia.rol= Globales.estatusPerfil?.perfil?.rol
            incidencia.campus= Globales.campus

            //  print("El campus es "+Globales.estatusPerfil?.perfil?.campus)
            //   Toast.makeText(applicationContext,"El id del profesor es "+incidencia.idProfesor,Toast.LENGTH_LONG).show()

        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                var url2 = Globales.url+"/mensajeria/incidencia";
                //  var url2="http://192.168.100.7:8080/api/practica"

                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


                val maper = ObjectMapper()
                //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

                val respuesta = restTemplate.postForObject(url2, incidencia, String::class.java)

                estatus = maper.readValue(respuesta, EstatusPerfil::class.java)
                // else estatus = null
                print("El rol es" + perfil?.rol)

                println("DESPUES DE REST");
            }catch(t:Throwable){
                //  Toast.makeText(applicationContext,"No tienes internet", Toast.LENGTH_LONG).show();

                print("HAAAAAGH   "+t.message);
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
             Toast.makeText(applicationContext, "mensaje:"+estatus.mensaje, Toast.LENGTH_LONG).show()
        }
    }

    /***
     * ESTE METODO DE OBTENER UBICACION ES SUMAMEMTE IMPORTANTE, ES EL QUE NOS VA  Y SE INVOCA
     * EN LA CLASE INTERNA DE REGISTRARSE PARA QUE NOS DE EN AUTOMÁTICO LA GEOLOCALIZACION
     * CON EL THREAD QUE ESTA CONSTANTEMENTE ACTUALIZANDOSE
     */
    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(momento:String){
        getMyLocation()



//termina e4ste es otro codigo




        println("PEEEEERRRRRROOOO");
        fecha= Calendar.getInstance();
        diaSemana=     fecha?.get(Calendar.DAY_OF_WEEK);

        diano= fecha?.get(Calendar.DAY_OF_YEAR);
        var hora=fecha?.get(Calendar.HOUR_OF_DAY);
        var minuto=fecha?.get(Calendar.MINUTE);
        var diaMes=fecha?.get(Calendar.DAY_OF_MONTH)
        var mes=fecha?.get(Calendar.MONTH);

        Globales.diaano =diano!!
        Globales.registrados =true
        miId= ""+ Globales.diaano+ Globales.estatusPerfil?.perfil?.id

        //Probamos guardar en shared preferences
        /*
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return@addOnSuccessListener
        with (sharedPref.edit()) {
            putInt("diasemana", dia!!)

            commit()
        }



        val highScore = sharedPref.getInt("dia",0);*/

        //   Toast.makeText(applicationContext,"Loca "+location?.latitude+" Longi"+location?.longitude+" alti "+location?.altitude+ " Dia es "+diano
        //        ,Toast.LENGTH_LONG).show()




        practica= Practica()
        practica?.momento=momento
        //ESte es el truco!!! se deben de agregar como id ademas el momento
        //en un mismo dia se pueden tener los 3 momentos
        practica?.id=momento+"-"+ diano!! +""+ Globales.estatusPerfil?.perfil?.id
        practica?.dia=diano!!
        practica?.diaSemana=diaSemana!!
        practica?.idProfesor= Globales.estatusPerfil?.perfil?.id
        practica?.lat= miLati
        practica?.lon=miLongi
        practica?.diaMes=diaMes
        practica?.mes=mes

        println("XXXXXXXXXXXXXXXXXXXXXXXX "+practica?.id);
        //  Toast.makeText(applicationContext, "Valor horario"+practica?.horario, Toast.LENGTH_SHORT).show();


        println("AAAAAAASSSSSSSAAA");

    }

    fun mostrarPerfil(){
        ocultarTodo()

        if(Globales.estatusPerfil?.perfil?.rol.equals("profesor")){
            val principal = findViewById(R.id.chequeo) as ConstraintLayout
            principal.visibility = View.VISIBLE
        }
        if(Globales.estatusPerfil?.perfil?.rol.equals("director-academico")|| Globales.estatusPerfil?.perfil?.rol.equals("director-divisional")|| Globales.estatusPerfil?.perfil?.rol.equals("direccion-campos")){
            val principal = findViewById(R.id.directores) as ConstraintLayout
            principal.visibility = View.VISIBLE
            //Ocultamos el menuchequeo, los directores no checan registro


        }
        if(Globales.estatusPerfil?.perfil?.rol.equals("coordinador")){
            val principal = findViewById(R.id.coordinadores) as ConstraintLayout
            principal.visibility = View.VISIBLE
        }

    }
    /*************************************************************************
     * Nueva geolocalizacion

     **************************************************************************/
    @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, 0, this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mylocation = location
        if (mylocation != null) {
            miLati = mylocation!!.latitude
            miLongi= mylocation!!.longitude

            //Or Do whatever you want with your location
        }
    }

    override fun onConnected(bundle: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(i: Int) {
        //Do whatever you need
        //You can display a message here
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        //You can display a message here
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient!!.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(this@MenuActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    val locationRequest = LocationRequest()
                    locationRequest.interval = 3000
                    locationRequest.fastestInterval = 3000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                    builder.setAlwaysShow(true)
                    LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this)
                    val result = LocationServices.SettingsApi
                        .checkLocationSettings(googleApiClient, builder.build())
                    result.setResultCallback { result ->
                        val status = result.status
                        when (status.statusCode) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                val permissionLocation = ContextCompat
                                    .checkSelfPermission(this@MenuActivity,
                                        Manifest.permission.ACCESS_FINE_LOCATION)
                                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                    mylocation = LocationServices.FusedLocationApi
                                        .getLastLocation(googleApiClient)
                                }
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(this@MenuActivity,
                                        REQUEST_CHECK_SETTINGS_GPS)
                                } catch (e: IntentSender.SendIntentException) {
                                    // Ignore the error.
                                }
                            }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            }
                        }// Location settings are not satisfied.
                        // However, we have no way
                        // to fix the
                        // settings so we won't show the dialog.
                        // finish();
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS_GPS -> when (resultCode) {
                AppCompatActivity.RESULT_OK -> getMyLocation()
                AppCompatActivity.RESULT_CANCELED -> finish()
            }
        }
    }

    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(this@MenuActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            }
        } else {
            getMyLocation()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(this@MenuActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation()
        }
    }

    companion object {
        private val REQUEST_CHECK_SETTINGS_GPS = 0x1
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2
    }
}


