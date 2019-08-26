package com.unalmed.vehTraffic.mallaVial

import com.unalmed.vehTraffic.simulacion.Simulacion
import scala.collection.mutable.ArrayBuffer

class Semaforo(val tiempoVerde: Int, val tiempoAmarillo: Int, val interseccion: Interseccion, val via: Via) {
  
  def tiempoSemaforo() = {
    tiempoVerde + tiempoAmarillo
  }
}

object Semaforo{
  
  def apply(minVerde:Int, maxVerde:Int, amarillo: Int, interseccion: Interseccion, via: Via): Semaforo ={
    val tiempoVerde = minVerde + scala.util.Random.nextInt({maxVerde + 1 - minVerde})
    new Semaforo(tiempoVerde, amarillo, interseccion, via)
  }
  
  def llenarSemaforos(vias: ArrayBuffer[Via], minTiempoVerde: Int, maxTiempoVerde: Int, tiempoAmarillo:Int): ArrayBuffer[Semaforo]={
    val semaforos: ArrayBuffer[Semaforo]= ArrayBuffer()
    vias.foreach(via=>{
      if(via.sentido.nombre == "unaVia"){
        semaforos += Semaforo(minTiempoVerde, maxTiempoVerde, tiempoAmarillo, via.fin, via)
      }
      else{
        semaforos += Semaforo(minTiempoVerde, maxTiempoVerde, tiempoAmarillo, via.fin, via)
        semaforos += Semaforo(minTiempoVerde, maxTiempoVerde, tiempoAmarillo, via.origen, via)
      } 
    })
  semaforos
  }
}