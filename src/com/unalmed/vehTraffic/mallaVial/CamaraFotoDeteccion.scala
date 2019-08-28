package com.unalmed.vehTraffic.mallaVial
import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.simulacion.Simulacion
import scala.collection.mutable.ArrayBuffer

class CamaraFotoDeteccion private(val via: Via, val longitudRespectoOrigen:Double, val ubicacion: Punto){
  
  def comprobar(vehiculo: Vehiculo, listaComparendos: ArrayBuffer[Comparendo]) = {
    if(vehiculo.velocidad.magnitud > via.velocidadMaxima){
      val comparendo = new Comparendo(vehiculo, vehiculo.velocidad.magnitud, via.velocidadMaxima)
      listaComparendos += comparendo
    }
  }
}

object CamaraFotoDeteccion {
  
  def apply(via:Via, longitudRespectoOrigen:Double) = {
    val longitudFinal = if(longitudRespectoOrigen>via.longitud-100)via.longitud-100 else if(longitudRespectoOrigen<100)100 else longitudRespectoOrigen
    new  CamaraFotoDeteccion(via:Via, longitudFinal:Double, calcularUbicacion(via, longitudFinal))
  }
  
  
  def calcularUbicacion(via:Via, longitud:Double) = {
    val theta = via.anguloOrigen.valor.toRadians
    val interseccion = via.origen
    val dy = longitud*Math.sin(theta)
    val dx = longitud*Math.cos(theta)
    Punto(interseccion.x+dx,interseccion.y+dy)
  }
}
