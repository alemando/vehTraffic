package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.base.Recorrido
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via

trait MovimientoUniforme{
  var _velocidad : Velocidad
  var _posicion : Punto
  val ruta : Queue[Via]
  val intersecciones : Queue[Interseccion]
  
  def cambioPosicion(dt : Int) = {
    var tiempo = dt
    if(!intersecciones.isEmpty){
      while(tiempo !=0){
        var velocidadVehiculo = _velocidad.magnitud 
        if (velocidadVehiculo > ruta.head.velocidadMaxima) velocidadVehiculo = ruta.head.velocidadMaxima
        if (_posicion == intersecciones.head.asInstanceOf[Punto])_velocidad.direccion_=(Angulo.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
        val tiempoInterseccion = calculoDt(_posicion, intersecciones.head, velocidadVehiculo)
        if(tiempoInterseccion > tiempo){
          val nuevoY = Velocidad.kilometroAmetro(velocidadVehiculo)*tiempo*Math.sin(_velocidad.direccion.valor)
          val nuevoX = Velocidad.kilometroAmetro(velocidadVehiculo)*tiempo*Math.cos(_velocidad.direccion.valor)
          _posicion = Punto(_posicion.x+nuevoX, _posicion.y+nuevoY)
          tiempo = 0
          
        }else{
          tiempo = tiempo - tiempoInterseccion
          _posicion = Punto(intersecciones.head.x, intersecciones.head.y)
          ruta.dequeue()
          if (ruta.isEmpty){
            tiempo = 0
            intersecciones.dequeue()
          }
        }
      }
    }
  }
  
  def calculoDt(posActual: Punto, posFin: Punto, velocidad: Double):Int = {
    (posActual.longitudEntrePunto(posFin)/Velocidad.kilometroAmetro(velocidad)).toInt
  }
  
}