package com.unalmed.vehTraffic.simulacion

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.vehiculo.Vehiculo

class Simulacion extends Runnable{
   
  var t: Int = 0
  
  val listaVehiculos: ArrayBuffer[Vehiculo] = new ArrayBuffer()
  
  val listaVias: ArrayBuffer[Via] = new ArrayBuffer()
  
  //TODO cargar datos vias y vehiculos
  val dt: Int = 0
  
  val tRefresh: Int = 0*1000
  
  val minVehiculos: Int = 0
  
  val maxVehiculos: Int = 0
  
  val minVelocidad: Int = 0
  
  val maxVelocidad: Int = 0
  
  //Falta proporciones
  
  //TODO Resultados simulacion
  
  def run() {
    while (true) {
      listaVehiculos.foreach(_.cambioPosicion(dt))
      t = t + dt
      //Grafico.graficarVehiculos(listadevehiculosOSimilar)
      Thread.sleep(tRefresh)
    }
  }
}