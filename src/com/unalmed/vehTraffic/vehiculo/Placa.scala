package com.unalmed.vehTraffic.vehiculo
import scala.collection.mutable.{Set => SetM}

object Placa {
  val letras = Array("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
  var _placas: SetM[String] = SetM()
  def placas: SetM[String]= _placas
  def placas_=(placas: SetM[String]):Unit = _placas=placas
}