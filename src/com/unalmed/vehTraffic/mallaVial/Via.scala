package com.unalmed.vehTraffic.mallaVial

import com.unalmed.vehTraffic.dimension.TipoVia
import com.unalmed.vehTraffic.dimension.Sentido

class Via(or : Interseccion, fn : Interseccion, val velocidadMaxima : Double, 
    tipo : TipoVia, sentido : Sentido, val numero : String, val nombre : String) extends Recta {
  //TODO get setters
  type T = Interseccion
  val origen = or
  val fin = fn
  
  def longitud = {
    Math.sqrt(Math.pow((fin.x - origen.x),2) + Math.pow((fin.y - origen.y),2))
  }
}