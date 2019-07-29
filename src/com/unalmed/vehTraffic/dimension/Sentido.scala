package com.unalmed.vehTraffic.dimension

case class Sentido private (nombre:String)

object Sentido{
  def unaVia={
    new Sentido("unaVia")
  }
  def dobleVia={
    new Sentido("dobleVia")
  }
}