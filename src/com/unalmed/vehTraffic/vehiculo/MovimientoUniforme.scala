package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.dimension.Velocidad

trait MovimientoUniforme{
  protected var _velocidad : Velocidad
  def velocidad: Velocidad
  def velocidad_=(vel:Velocidad):Unit=_velocidad=vel
  protected var _posicion : Punto
  def posicion: Punto
  def posicion_=(pos:Punto):Unit=_posicion=pos
  val ruta : Queue[Via]
  val intersecciones : Queue[Interseccion]
  
  def cambioPosicion(dt : Double) = {
    var tiempo = dt
    if(!intersecciones.isEmpty){
      while(tiempo !=0){
        val velocidadVehiculo = velocidad.limitarVelocidad(ruta.head.velocidadMaxima)
        if (posicion == intersecciones.head.asInstanceOf[Punto] && intersecciones.length>=2) velocidad= Velocidad(velocidad.magnitud)(Angulo.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
        val tiempoInterseccion = calculoDt(posicion, intersecciones.head, velocidadVehiculo)
        if(tiempoInterseccion > tiempo){
          val (nuevoX, nuevoY) = Punto.cambioEnPosicion(velocidadVehiculo, tiempo, velocidad.direccion.valor) 
          posicion = Punto(posicion.x + nuevoX, posicion.y + nuevoY)
          tiempo = 0
          
        }else{
          tiempo = tiempo - tiempoInterseccion
          posicion = Punto(intersecciones.head.x, intersecciones.head.y)
          ruta.dequeue()
          if (ruta.isEmpty){
            tiempo = 0
            intersecciones.dequeue()
          }
        }
      }
    }
  }
  
  def calculoDt(posicionActual: Punto, posicionFinal: Punto, velocidad: Double):Double = {
    posicionActual.longitudEntrePunto(posicionFinal)/Velocidad.kilometroAmetro(velocidad)
  }
  
}