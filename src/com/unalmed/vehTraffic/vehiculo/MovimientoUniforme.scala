package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.dimension.Velocidad

trait MovimientoUniforme{
  protected var _velocidad : Velocidad
  def velocidad: Velocidad
  def velocidad_=(vel:Velocidad):Unit=_velocidad=vel
  protected var _posicion : Punto
  def posicion: Punto
  def posicion_=(pos:Punto):Unit=_posicion=pos
    
  def movimientoRectilineoUniforme (t: Double):Unit = {
    val theta = velocidad.direccion.valor.toRadians
    val v = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dy = v*t*Math.sin(theta)
    val dx = v*t*Math.cos(theta)
    posicion=Punto(posicion.x+dx,posicion.y+dy)
  }

}