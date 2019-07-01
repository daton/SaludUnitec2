package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalTime

@JsonIgnoreProperties(ignoreUnknown = true)
class Practica {

  var id:String?=null;
    var idProfesor:Int?=null
    var momento:String?=null
     var turno: String? = null
     var semana: Int? = null
     var dia: Int? = null
    var mes:Int?=null
    var diaMes:Int?=null
    var diaSemana:Int?=null
    var lat: Double?=null
    var lon: Double ?=null
    var distancia:Double?=null
    var horario:String?=null

    var incidencias: List<Incidencia>? = null

}
