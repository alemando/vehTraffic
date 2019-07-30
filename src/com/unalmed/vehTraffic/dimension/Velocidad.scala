package com.unalmed.vehTraffic.dimension

case class Velocidad(magnitud : Double, var _direccion : Angulo){
  
  def direccion = _direccion
  
  def direccion_= (angulo: Angulo):Unit = _direccion = angulo
}
object Velocidad{
  def metroAkilometro(metro:Double):Double={
    metro*3.6
  }
  def kilometroAmetro(kilo:Double):Double={
    kilo/3.6
  }
}