package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
class Perfil {
    var id:Int?=null
    var nip:String?=null
    var rol:String?=null
    var nombre:String?=null
    var paterno:String?=null
    var materno:String?=null
    var idCampoClinico:Int?=null
    var campus:String?=null
    var token:String?=null


}