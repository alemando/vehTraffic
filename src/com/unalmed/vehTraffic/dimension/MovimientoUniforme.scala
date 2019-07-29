package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.base.Recorrido
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via

trait MovimientoUniforme{
  val velocidad : Velocidad
  val posicion : Punto
  //val ruta : Queue[Via]
  
  def cambioPosicion(dt : Int) = {
    
    //ruta.
    
  }
  
}