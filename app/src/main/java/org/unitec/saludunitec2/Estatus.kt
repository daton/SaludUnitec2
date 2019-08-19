package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Estatus{
   var  success:Boolean?=null
    var mensaje:String?=null
}
