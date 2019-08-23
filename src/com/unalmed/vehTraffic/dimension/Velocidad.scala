package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.main.Main
import com.unalmed.vehTraffic.simulacion.Simulacion

case class Velocidad private(val magnitud : Double) (var _direccion : Angulo){
  
  def direccion = _direccion
  
  def direccion_= (angulo: Angulo):Unit = _direccion = angulo
  
  def limitarVelocidad(velocidadVia: Double) = {
    if(this.magnitud < velocidadVia){
      this.magnitud
    }else{
      velocidadVia
    }
  }
}
object Velocidad{
  
  def apply(magnitud:Double, direccion:Angulo, velocidadMaxima: Double=500.0):Velocidad={
    val mag = magnitud match{
      case n if(magnitud <= 0.0) => 0.0
      case n if(magnitud>= velocidadMaxima) => velocidadMaxima
      case n => magnitud
    }
    new Velocidad(mag)(direccion)
  }
  
  def metroAkilometro(metro:Double):Double={metro*3.6}
  
  def kilometroAmetro(kilo:Double):Double={kilo/3.6}
  
}
