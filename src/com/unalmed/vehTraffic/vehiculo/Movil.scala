package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.mallaVial.Punto

abstract class Movil(protected var _posicion : Punto, protected var _velocidad : Velocidad, protected var _aceleracion: Double,
    val velocidadCrucero: Double,val tazaAceleracion: Double) {
  def velocidad = _velocidad
  def posicion = _posicion
  def aceleracion = _aceleracion
  def velocidad_=(vel:Velocidad):Unit=_velocidad=vel
  def posicion_=(pos:Punto):Unit=_posicion=pos
  def aceleracion_=(ace:Double):Unit=_aceleracion=ace
  def aplicarMovimientoRectilineoUniforme(dt : Double)
  def aplicarAceleracion(dt : Double):Unit
  def angulo = velocidad.direccion.valor
}