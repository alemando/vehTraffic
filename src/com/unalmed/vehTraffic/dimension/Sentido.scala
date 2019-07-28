package com.unalmed.vehTraffic.dimension

case class Sentido private (_nombre:String){
  
}
object Sentido{
  def unaVia={
    new Sentido("una via")
  }
  def dobleVia={
    new Sentido("doble via")
  }
}