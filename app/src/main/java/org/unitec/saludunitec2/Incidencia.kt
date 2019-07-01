package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Incidencia {

     var campoClinico:String?=null
     var idProfesor:Int?=null
     var rol:String?=null
     var campus:String?=null
     var nombre: String? = null
     var fecha:String?=null
}
