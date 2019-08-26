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
  
}

object Vehiculo{
  
  def apply(minVelocidad:Int, maxVelocidad:Int, minAceleracion: Int, maxAceleracion:Int, proporciones:ArrayBuffer[Double]): Vehiculo={
    val r= scala.util.Random.nextFloat()
//    val probCarros = simulacion.proporciónCarros
//    val probMotos = probCarros + simulacion.proporciónMotos
//    val probBuses = probMotos + simulacion.proporciónBuses
//    val probCamiones = probBuses + simulacion.proporciónCamiones
//    val probMotoTaxi = probCamiones + simulacion.proporciónMotoTaxis
    val velocidad = minVelocidad + scala.util.Random.nextInt({maxVelocidad + 1 - minVelocidad})
    val aceleracion = minAceleracion + scala.util.Random.nextInt(maxAceleracion + 1 - minAceleracion)
    val nodo = Punto(0.0,0.0)/*viaje.intersecciones.head*/
    val angulo = Angulo(0.0)/*{if (nodo == viaje.ruta.head.origen)viaje.ruta.head.anguloOrigen
                  else viaje.ruta.head.anguloDestino}*/
    if (r>=proporciones(0) && r<=proporciones(1))
      return Carro(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>proporciones(1) && r<=proporciones(2))
      return Moto(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>proporciones(2) && r<=proporciones(3))
      return Bus(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else if(r>proporciones(3) && r<=proporciones(4))
      return Camion(nodo, Velocidad(0, angulo), velocidad, aceleracion)
    else
      return MotoTaxi(nodo, Velocidad(0, angulo), velocidad, aceleracion)
  }
  
  def llenarVehiculos(minVehiculos:Int, maxVehiculos:Int, minVelocidad:Int, maxVelocidad:Int, minAceleracion: Int, maxAceleracion:Int, proporciones:ArrayBuffer[Double]): ArrayBuffer[Vehiculo]={
      val minimo = minVehiculos
      val maximo = maxVehiculos
    val cantidad = minimo + {scala.util.Random.nextInt(maximo+1 -minimo)}
    val vehiculos = ArrayBuffer.fill(cantidad)(Vehiculo(minVelocidad, maxVelocidad, minAceleracion, maxAceleracion, proporciones))
    vehiculos
  }
  
}