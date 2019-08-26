package com.unalmed.vehTraffic.mallaVial

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.simulacion.Simulacion

class NodoSemaforo(val interseccion: Interseccion, val semaforos: ArrayBuffer[Semaforo]) {
  
  
  /* Dado un tiempo en la simulacion ya sea sumado un cambio del tiempo(dt) o no, y un semaforo
   * y su simulacion, obtengo el estado actual del semaforo en el tiempo global de la simulacion
   * y el tiempo para su proximo cambio de estado
   */
  def estadoDeSemaforo(dt:Double, semaforo: Semaforo, simulacion: Simulacion): (String, Double) = {
    var t = {
      if (dt==simulacion.dt)tiempoEnRango(simulacion.t + dt)
      else tiempoEnRango(simulacion.t +simulacion.dt + (simulacion.dt - dt))}
    var estado: String = ""
    var tiempoHastaSiguenteEstado = 0.0
    semaforos.foreach(semaforoLista => {
      if(semaforoLista == semaforo){
        if(t<0){
          estado = "Rojo"
          tiempoHastaSiguenteEstado = t* -1
        }else{
          t = t - semaforo.tiempoVerde
          if(t < 0){
            estado = "Verde"
            tiempoHastaSiguenteEstado = t* -1
          }else if(t - semaforo.tiempoAmarillo < 0){
            t = t - semaforo.tiempoAmarillo
            estado = "Amarillo"
            tiempoHastaSiguenteEstado = t* -1
          }else{
            t = t - semaforo.tiempoAmarillo
            val tiempoRojo = sumaTiempoSemaforos() - semaforo.tiempoSemaforo()
            estado = "Rojo"
            tiempoHastaSiguenteEstado = tiempoRojo - t
          }
        }
      }else{
        t = t - semaforoLista.tiempoSemaforo()
      }
    })
    (estado, tiempoHastaSiguenteEstado)
    
  }
  
  def sumaTiempoSemaforos() = {
    semaforos.map(_.tiempoSemaforo()).reduce(_+_)
  }
  
  def tiempoEnRango(t : Double) = {
    val tiempoQueResto = sumaTiempoSemaforos()*Math.floor((t/sumaTiempoSemaforos())).toInt
    t- tiempoQueResto
  }
  
  def semaforoEnVia(via: Via):Semaforo={
    semaforos.filter(_.via==via)(0)
  }
}

object NodoSemaforo{
  
  def crearNodos(semaforos: ArrayBuffer[Semaforo], intersecciones: ArrayBuffer[Interseccion]):Unit={
    val arr = new ArrayBuffer[NodoSemaforo]()
    intersecciones.foreach(interseccion=>{
      val nodoSemaforo = new NodoSemaforo(interseccion, semaforos.filter(_.interseccion == interseccion))
      arr += nodoSemaforo
      interseccion.nodoSemaforo =  Option(nodoSemaforo)
    })
  }
  
}