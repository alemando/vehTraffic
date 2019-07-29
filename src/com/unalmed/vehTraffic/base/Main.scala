package com.unalmed.vehTraffic.base

import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.mallaVial.Interseccion
import scala.collection.mutable.Queue

object Main extends App{
  Simulacion
  val recorrido = Recorrido()
  println(recorrido.origen)
  println(recorrido.destino)
  val vehiculo = Vehiculo()
  val ruta = recorrido.camino.get
  println(ruta)
  val ruta2 = vehiculo.recorrido.camino.get
  println(ruta2)
  println(vehiculo)
  println(vehiculo.placa)
  println("hola")
}