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
  
  def apply(magnitud:Double, direccion:Angulo, simulacion: Simulacion):Velocidad={
    val mag = magnitud match{
      case n if(magnitud <= simulacion.minVelocidad) => simulacion.minVelocidad
      case n if(magnitud>= simulacion.maxVelocidad) => simulacion.maxVelocidad
      case n => magnitud
    }
    new Velocidad(mag)(direccion)
  }
  
  def metroAkilometro(metro:Double):Double={metro*3.6}
  
  def kilometroAmetro(kilo:Double):Double={kilo/3.6}
  
}
