package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.dimension.Velocidad

trait MovimientoUniforme extends Movil{
    
  def aplicarMovimientoRectilineoUniforme (t: Double):Unit = {
    val theta = angulo.toRadians
    val v = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dy = v*t*Math.sin(theta)
    val dx = v*t*Math.cos(theta)
    posicion=Punto(posicion.x+dx,posicion.y+dy)
  }
  
  def maximaDistanciaUniforme(t:Double):Double={
    val v = Velocidad.kilometroAmetro(velocidadCrucero)
    val dx = v*t
    dx
  }

}