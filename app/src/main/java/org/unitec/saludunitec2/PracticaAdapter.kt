package org.unitec.saludunitec2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PracticaAdapter(items: Array<Practica>?, ctx: Context) :
    ArrayAdapter<Practica>(ctx, R.layout.lista_simple_asistencia, items) {

    //view holder is used to prevent findViewById calls
    private class AsistenciaItemViewHolder {

        internal var idProfesor: TextView? = null
        internal var momento:TextView?=null
        internal  var horario:TextView?=null

    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view

        val viewHolder: AsistenciaItemViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.lista_simple_asistencia, viewGroup, false)

            viewHolder = AsistenciaItemViewHolder()
            viewHolder.idProfesor = view!!.findViewById<View>(R.id.idProfesor) as TextView
            viewHolder.momento = view!!.findViewById<View>(R.id.momento) as TextView
            viewHolder.horario=view!!.findViewById<View>(R.id.horario) as TextView


        } else {
            //no need to call findViewById, can use existing ones from saved view holder
            viewHolder = view.tag as AsistenciaItemViewHolder
        }

            val asistencia = getItem(i)
            viewHolder.idProfesor!!.text = asistencia!!.idProfesor.toString()
           viewHolder.momento!!.text = asistencia!!.momento.toString()
        viewHolder.horario!!.text=asistencia!!.horario.toString()


            view.tag = viewHolder

            return view
        }
    }

