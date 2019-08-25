package com.unalmed.vehTraffic.mallaVial

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.simulacion.Simulacion

class NodoSemaforo(val interseccion: Interseccion, val semaforos: ArrayBuffer[Semaforo]) {
  
  
  /* Dado un tiempo en la simulacion ya sea sumado un cambio del tiempo(dt) o no, y un semaforo
   * y su simulacion, obtengo el estado actual del semaforo en el tiempo global de la simulacion
   * y el tiempo para su proximo cambio de estado
   */
  def estadoDeSemaforo(dt:Int, semaforo: Semaforo, simulacion: Simulacion): (String, Int) = {
    var t = tiempoEnRango(simulacion.t) + dt
    var estado: String = ""
    var tiempoHastaSiguenteEstado = 0
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
  
  def tiempoEnRango(t : Int) = {
    val tiempoQueResto = sumaTiempoSemaforos()*Math.floor((t/sumaTiempoSemaforos())).toInt
    t- tiempoQueResto
  }
}