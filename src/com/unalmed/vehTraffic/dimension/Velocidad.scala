package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.simulacion.Simulacion.{maxVelocidad, minVelocidad}

case class Velocidad (val magnitud : Double) (var _direccion : Angulo){
  
  def direccion = _direccion
  
  def direccion_= (angulo: Angulo):Unit = _direccion = angulo
}
object Velocidad{
  
  def apply(magnitud:Double)(direccion:Angulo):Velocidad={
    val mag = magnitud match{
      case n if(magnitud <=minVelocidad) => minVelocidad
      case n if(magnitud>=maxVelocidad) => maxVelocidad
      case n => magnitud
    }
    new Velocidad(mag)(direccion)
  }
  
  def metroAkilometro(metro:Double):Double={metro*3.6}
  
  def kilometroAmetro(kilo:Double):Double={kilo/3.6}
  
}
