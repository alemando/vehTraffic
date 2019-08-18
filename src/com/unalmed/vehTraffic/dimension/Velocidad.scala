package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.main.Main

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
  
  def apply(magnitud:Double)(direccion:Angulo):Velocidad={
    val Simulacion = Main.objectSimulacion
    val mag = magnitud match{
      case n if(magnitud <= Simulacion.minVelocidad) => Simulacion.minVelocidad
      case n if(magnitud>= Simulacion.maxVelocidad) => Simulacion.maxVelocidad
      case n => magnitud
    }
    new Velocidad(mag)(direccion)
  }
  
  def metroAkilometro(metro:Double):Double={metro*3.6}
  
  def kilometroAmetro(kilo:Double):Double={kilo/3.6}
  
}
