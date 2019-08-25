package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{Velocidad, Angulo}
import com.unalmed.vehTraffic.mallaVial.{Punto, Interseccion}
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.grafo.Viaje
import scala.collection.mutable.{Queue, ArrayBuffer}
import com.unalmed.vehTraffic.mallaVial.Via

abstract case class Vehiculo(placa : String)(private var _p : Punto, private var _v: Velocidad, private val _vc: Double, private val _a: Double)
extends Movil(_p,_v, _vc, _a) with MovimientoAcelerado {
  
  private def p: Punto= _p
  private def v: Velocidad=_v
  private def p_=(p: Punto):Unit= _p=p
  private def v_=(v: Velocidad):Unit= _v=v
  val semaforo = scala.util.Random.nextBoolean()
  
  //Esto lo cree para acceder a mi velocidad por parte de la fotoMulta att Espino
  def magnitudPublica=_v.magnitud
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
    val aceleracion = simulacion.minAceleracion + scala.util.Random.nextInt(simulacion.maxAceleracion + 1 - simulacion.minAceleracion)
    val nodo = viaje.intersecciones.head
    val angulo = {if (nodo == viaje.ruta.head.origen)viaje.ruta.head.anguloOrigen
                  else viaje.ruta.head.anguloDestino}
    if (r>=0 && r<=probCarros)
      return Carro(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>probCarros && r<=probMotos)
      return Moto(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>probMotos && r<=probBuses)
      return Bus(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>probBuses && r<=probCamiones)
      return Camion(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else
      return MotoTaxi(nodo, Velocidad(0, angulo), velocidad, aceleracion)
  }
  
}