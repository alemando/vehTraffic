package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{Velocidad, Angulo}
import com.unalmed.vehTraffic.mallaVial.{Punto, Interseccion}
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.grafo.Viaje
import scala.collection.mutable.{Queue, ArrayBuffer}
import com.unalmed.vehTraffic.mallaVial.Via

abstract case class Vehiculo(placa : String)(private var _p : Punto, private var _v: Velocidad)
extends Movil(_p,_v) with MovimientoUniforme {
  
  private def p: Punto= _p
  private def v: Velocidad=_v
  private def p_=(p: Punto):Unit= _p=p
  private def v_=(v: Velocidad):Unit= _v=v
  
//  def cambioPosicion(dt : Double) = {
//    val error = dt*Velocidad.kilometroAmetro(velocidad.magnitud)
//    if(!viaje.intersecciones.isEmpty){
//    if (posicion == viaje.intersecciones.head.asInstanceOf[Punto] && viaje.intersecciones.length>=2 && viaje.ruta.length>=1) {
//        val interseccionActual = viaje.intersecciones.dequeue()
//        val viaActual = viaje.ruta.dequeue()
//        velocidad= Velocidad(velocidad.magnitud)({if (viaActual.origen == interseccionActual)viaActual.anguloOrigen
//                                                  else viaActual.anguloDestino})}
//    val interseccionSiguiente = viaje.intersecciones.head
//    if(posicion.longitudEntrePunto(interseccionSiguiente) >= error){
//      movimientoRectilineoUniforme(dt)
//      if(posicion.longitudEntrePunto(interseccionSiguiente) <= 0.5*error) posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)}
//    else posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
//    }
//  }
}

object Vehiculo{
  
  def apply(simulacion: Simulacion, viaje: Viaje): Vehiculo={
    val r= scala.util.Random.nextFloat()
    val probCarros = simulacion.proporciónCarros
    val probMotos = probCarros + simulacion.proporciónMotos
    val probBuses = probMotos + simulacion.proporciónBuses
    val probCamiones = probBuses + simulacion.proporciónCamiones
    val probMotoTaxi = probCamiones + simulacion.proporciónMotoTaxis
    val velocidad = simulacion.minVelocidad + scala.util.Random.nextInt({simulacion.maxVelocidad + 1 - simulacion.minVelocidad})
    val nodo = viaje.intersecciones.head
    val angulo = {if (nodo == viaje.ruta.head.origen)viaje.ruta.head.anguloOrigen
                  else viaje.ruta.head.anguloDestino}
    if (r>=0 && r<=probCarros)
      return Carro(nodo, Velocidad(velocidad)(angulo))
    else if(r>probCarros && r<=probMotos)
      return Moto(nodo, Velocidad(velocidad)(angulo))
    else if(r>probMotos && r<=probBuses)
      return Bus(nodo, Velocidad(velocidad)(angulo))
    else if(r>probBuses && r<=probCamiones)
      return Camion(nodo, Velocidad(velocidad)(angulo))
    else
      return MotoTaxi(nodo, Velocidad(velocidad)(angulo))
  }
  
}