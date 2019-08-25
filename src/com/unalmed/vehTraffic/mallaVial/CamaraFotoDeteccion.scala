package com.unalmed.vehTraffic.mallaVial
import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.simulacion.Simulacion

class CamaraFotoDeteccion(via: Via, longitudRespectoOrigen:Double, ubicacion: Punto){
  
  def comprobar(vehiculo: Vehiculo, velocidad: Double, simulacion: Simulacion) = {
    if(velocidad > via.velocidadMaxima){
      val comparendo = new Comparendo(vehiculo, velocidad, via.velocidadMaxima)
      simulacion.listaComparendos += comparendo
    }
  }
  
}

object CamaraFotoDeteccion {
  
  def apply(via:Via, longitudRespectoOrigen:Double){
    
    new CamaraFotoDeteccion(via:Via, longitudRespectoOrigen:Double, calcularUbicacion(via, longitudRespectoOrigen))
  }
  
  
  
  def calcularUbicacion(via:Via, longitud:Double) = {
    val theta = via.anguloOrigen.valor.toRadians
    val interseccion = via.origen
    val dy = longitud*Math.sin(theta)
    val dx = longitud*Math.cos(theta)
    Punto(interseccion.x+dx,interseccion.y+dy)
  }
}
