package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.mallaVial.Punto

abstract class Movil(val posicion : Punto, val velocidad : Velocidad) {
  //TODO get Setters
  def cambioPosicion(dt : Int)
  def angulo = velocidad.direccion.valor
}