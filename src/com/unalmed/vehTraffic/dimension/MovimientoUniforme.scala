package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.base.Recorrido
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.Via

trait MovimientoUniforme{
  protected var _velocidad : Velocidad
  def velocidad: Velocidad
  def velocidad_=(vel:Velocidad):Unit=_velocidad=vel
  protected var _posicion : Punto
  def posicion: Punto
  def posicion_=(pos:Punto):Unit=_posicion=pos
  val ruta : Queue[Via]
  val intersecciones : Queue[Interseccion]
  
  def cambioPosicion(dt : Int) = {
    var tiempo = dt
    if(!intersecciones.isEmpty){
      while(tiempo !=0){
        var velocidadVehiculo = velocidad.magnitud 
        if (velocidadVehiculo > ruta.head.velocidadMaxima) velocidadVehiculo = ruta.head.velocidadMaxima
        if (posicion == intersecciones.head.asInstanceOf[Punto]) velocidad= Velocidad(velocidad.magnitud)(Angulo.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
//        if (posicion == intersecciones.head.asInstanceOf[Punto])velocidad.direccion_=(Angulo.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
        val tiempoInterseccion = calculoDt(_posicion, intersecciones.head, velocidadVehiculo)
        if(tiempoInterseccion > tiempo){
          val nuevoY = Velocidad.kilometroAmetro(velocidadVehiculo)*tiempo*Math.sin(velocidad.direccion.valor.toRadians)
          val nuevoX = Velocidad.kilometroAmetro(velocidadVehiculo)*tiempo*Math.cos(velocidad.direccion.valor.toRadians)
          posicion = Punto(posicion.x+nuevoX, posicion.y+nuevoY)
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
  
  def calculoDt(posActual: Punto, posFin: Punto, velocidad: Double):Int = {
    (posActual.longitudEntrePunto(posFin)/Velocidad.kilometroAmetro(velocidad)).toInt
  }
  
}