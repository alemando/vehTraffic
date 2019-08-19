package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.mallaVial.Punto

abstract class Movil(protected var _posicion : Punto, protected var _velocidad : Velocidad) {
  def velocidad = _velocidad
  def posicion = _posicion
  def velocidad_=(vel:Velocidad):Unit
  def posicion_=(pos:Punto):Unit
  def movimientoRectilineoUniforme(dt : Double)
  def angulo = velocidad.direccion.valor
}