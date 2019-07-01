package org.unitec.saludunitec2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by rapid on 08/12/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class EstatusPerfil {
     var success: Boolean = false
     var mensaje: String? = null
    var perfil:Perfil?=null;


}
