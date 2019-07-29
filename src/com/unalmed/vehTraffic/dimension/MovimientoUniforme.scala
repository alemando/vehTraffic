package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto

trait MovimientoUniforme{
  val velocidad : Velocidad
  val posicion : Punto
  
  def cambioPosicion(dt : Int) = {
    
  }
  
}