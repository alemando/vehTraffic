package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.MovimientoUniforme
import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad

case class Vehiculo(placa : String)(pos : Punto, vel : Velocidad) extends Movil(pos,vel) with MovimientoUniforme {
  
}
object Vehiculo{
  def apply(){
    //new Vehiculo
  }
  
}