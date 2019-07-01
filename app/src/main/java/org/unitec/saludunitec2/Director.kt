package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class Director {


    var nip:Int?=null

    var nombre:String?=null
    var paterno:String?=null
    var materno:String?=null
    var perfil:Array<Perfil>? = null
}