package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.mallaVial.Punto

abstract class Movil(protected var _posicion : Punto, protected var _velocidad : Velocidad, val velocidadCrucero: Double,val aceleracion: Double) {
  def velocidad = _velocidad
  def posicion = _posicion
  def velocidad_=(vel:Velocidad):Unit=_velocidad=vel
  def posicion_=(pos:Punto):Unit=_posicion=pos
  def aplicarMovimientoRectilineoUniforme(dt : Double)
  def aplicarAceleracion(dt : Double)
  def aplicarDesaceleracion(dt: Double, a: Double)
  def angulo = velocidad.direccion.valor
}