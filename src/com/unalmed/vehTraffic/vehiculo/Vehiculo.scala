package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{MovimientoUniforme, Velocidad}
import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.base.Recorrido

case class Vehiculo(placa : String)(pos : Punto, vel : Velocidad, val recorrido: Recorrido) extends Movil(pos,vel) with MovimientoUniforme {

  
}
object Vehiculo{
  
  def apply(){
    val r= scala.util.Random.nextFloat()
    //new Vehiculo
  }
  
  
}