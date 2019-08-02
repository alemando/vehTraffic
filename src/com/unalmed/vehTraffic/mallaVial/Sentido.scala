package com.unalmed.vehTraffic.mallaVial

case class Sentido private (nombre:String)

object Sentido{
  def unaVia={
    new Sentido("unaVia")
  }
  def dobleVia={
    new Sentido("dobleVia")
  }
}