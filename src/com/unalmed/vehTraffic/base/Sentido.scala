package com.unalmed.vehTraffic.base

class Sentido(private val _nombre:String){
  
}
object Sentido{
  def unaVia={
    new Sentido("una via")
  }
  def dobleVia={
    new Sentido("doble via")
  }
}