package com.unalmed.vehTraffic.mallaVial

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.simulacion.Simulacion

class NodoSemaforo(val semaforos: ArrayBuffer[Semaforo]) {
  
  def tiempoYsiguienteEstadoEnSemaforo(semaforo: Semaforo, simulacion: Simulacion): (Int, String) = {
    var t = tiempoEnRango(simulacion.t)
    if (semaforo.estado == "Verde"){
      var encontrado = false
      semaforos.foreach(semaforoLista => {
        if(semaforoLista == semaforo) encontrado = true
        if (!encontrado) t = t - semaforoLista.tiempoSemaforo()
      })
      (semaforo.tiempoVerde - t,"Amarilo")
    }else if(semaforo.estado == "Amarillo"){
      var encontrado = false
      semaforos.foreach(semaforoLista => {
        if(semaforoLista == semaforo) encontrado = true
        if (!encontrado) t = t - semaforoLista.tiempoSemaforo()
      })
      t = t - semaforo.tiempoVerde
      (semaforo.tiempoAmarillo - t, "Rojo")
     } else{
       var tiempoHastaVerde = 0
       var encontrado = false
       semaforos.foreach(semaforoLista => {
         if(semaforoLista == semaforo) encontrado = true
         if (!encontrado) tiempoHastaVerde = t - semaforoLista.tiempoSemaforo()
        
      })
      if (tiempoHastaVerde >= t){
        (tiempoHastaVerde - t, "Verde")
      }else{
        val tiempoRojo = sumaTiempoSemaforos() - semaforo.tiempoSemaforo()
        (tiempoRojo - (t - tiempoHastaVerde + semaforo.tiempoSemaforo()), "Verde")
      }
      
      
     }
  }
  
  def cambiarEstadoSemaforos(simulacion:Simulacion) = {
    var t = tiempoEnRango(simulacion.t)
     semaforos.foreach(semaforo => {
       if (t > 0){
         if(t-semaforo.tiempoVerde < 0){
           t=0
           semaforo.estado = "Verde"
         }else if (t-semaforo.tiempoAmarillo < 0){
           t=0
           semaforo.estado = "Amarillo"
         }else{
           semaforo.estado = "Rojo"
         }
       }else{
         semaforo.estado = "Rojo"
       }
     })
  }
  
  def sumaTiempoSemaforos() = {
    semaforos.map(_.tiempoSemaforo()).reduce(_+_)
  }
  
  def tiempoEnRango(t : Int) = {
    val tiempoQueResto = sumaTiempoSemaforos()*Math.floor((t/sumaTiempoSemaforos())).toInt
    t- tiempoQueResto
  }
}