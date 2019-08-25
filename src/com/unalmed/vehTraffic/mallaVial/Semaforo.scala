package com.unalmed.vehTraffic.mallaVial

import com.unalmed.vehTraffic.simulacion.Simulacion

class Semaforo(val tiempoVerde: Int, val tiempoAmarillo: Int, val interseccion: Interseccion) {
  
  def tiempoSemaforo() = {
    tiempoVerde + tiempoAmarillo
  }
}

object Semaforo{
  
  def apply(simulacion: Simulacion, interseccion: Interseccion): Semaforo ={
    val tiempoVerde = simulacion.minTiempoVerde + scala.util.Random.nextInt({simulacion.maxTiempoVerde + 1 - simulacion.minTiempoVerde})
    new Semaforo(tiempoVerde, simulacion.tiempoAmarillo, interseccion)
  }
}