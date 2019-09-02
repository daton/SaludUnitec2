package org.unitec.saludunitec2

object Globales {
 var url = "http://192.168.100.85:8080/api"
  //var url="https://daton.herokuapp.com/api"
    var diaano=0
    var registrados=false
    var estatusPerfil:EstatusPerfil?=null
    var director:Director?=null
    var campus:String?=null
    var asistencias:Array<Practica>?=null
    var incidencia:Incidencia?=null

}
