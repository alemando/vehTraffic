package com.unalmed.vehTraffic.main

import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.TipoVia
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.Via
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.frame.Grafico
import com.unalmed.vehTraffic.mallaVial.CamaraFotoDeteccion

object Main extends App{
  
  val (listaIntersecciones,listaVias, listaCamaraFotoDeteccion) = Conexion.getInterseccionesViasCamaras()
  
  Grafico.iniciarGrafico(listaVias, listaIntersecciones, listaCamaraFotoDeteccion)
  
  var objectSimulacion: Simulacion = new Simulacion(listaVias, listaIntersecciones)
  
  def start() = {
    while(Thread.activeCount()>3){
      Thread.sleep(100)
    }
    if (objectSimulacion != null) objectSimulacion.borrar
    objectSimulacion = new Simulacion(listaVias, listaIntersecciones)
    objectSimulacion.start()
    
  }
  
  def stop() = {
    objectSimulacion.stop()
  }
  
}