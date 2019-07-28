package com.unalmed.vehTraffic.dimension

case class Velocidad(var magnitud : Double, var direccion : Angulo){
  //TODO get setters
}
object Velocidad{
  def apply(mag:Double,dir:Angulo)={
    new Velocidad(mag,dir)
  }
  def metroAkilometro(metro:Double):Double={
    metro*3.6
  }
  def kilometroAmetro(kilo:Double):Double={
    kilo*(1/3.6)
  }
}