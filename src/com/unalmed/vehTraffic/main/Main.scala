package com.unalmed.vehTraffic.main

import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.TipoVia
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.Via
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.frame.Grafico
import com.unalmed.vehTraffic.mallaVial.CamaraFotoDeteccion
import com.unalmed.vehTraffic.grafo.GrafoVia

object Main extends App{
  
  val (listaIntersecciones,listaVias, listaCamaraFotoDeteccion) = Conexion.getInterseccionesViasCamaras()
  
  Grafico.iniciarGrafico(listaVias, listaIntersecciones, listaCamaraFotoDeteccion)
  
  Grafico.graficarVias(listaVias)
  
  GrafoVia.construir(listaVias)
  
  var objectSimulacion: Simulacion = _
  
  def start() = {
    while(Thread.activeCount()>3){
      Thread.sleep(100)
    }
    if (objectSimulacion != null) objectSimulacion.borrar
    objectSimulacion = Simulacion(listaVias, listaIntersecciones, listaCamaraFotoDeteccion)
    objectSimulacion.start()
    
  }
  
  def stop() = {
    if(objectSimulacion != null) objectSimulacion.stop()
  }
  
  def guardar() = {
    if(objectSimulacion != null){
      objectSimulacion.stop()
      Conexion.guardarSimulacion(objectSimulacion)
    }
  }
  
  def reconstruir(){
    if(objectSimulacion != null) objectSimulacion.stop()
    if (objectSimulacion != null) objectSimulacion.borrar
    if(Conexion.comprobarSimulacio()) objectSimulacion = Conexion.cargarSimulacion(listaVias, listaIntersecciones, listaCamaraFotoDeteccion)
    while(Thread.activeCount()>3){
      Thread.sleep(100)
    }
    objectSimulacion.start()
  }
  
}