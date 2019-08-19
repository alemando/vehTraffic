package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{Velocidad, Angulo}
import com.unalmed.vehTraffic.mallaVial.{Punto, Interseccion}
import com.unalmed.vehTraffic.main.Main
import com.unalmed.vehTraffic.grafo.Recorrido
import scala.collection.mutable.{Queue, ArrayBuffer}
import com.unalmed.vehTraffic.mallaVial.Via

abstract case class Vehiculo (placa : String)(private var _p : Punto, private var _v: Velocidad, val recorrido: Recorrido)
extends Movil(_p,_v) with MovimientoUniforme {
  
  private def p: Punto= _p
  private def v: Velocidad=_v
  private def p_=(p: Punto):Unit= _p=p
  private def v_=(v: Velocidad):Unit= _v=v
  
  val ruta: Queue[Via] = Queue(recorrido.camino.get.edges.map(_.label.asInstanceOf[Via]).toList: _*)
  val intersecciones: Queue[Interseccion] = Queue(recorrido.camino.get.nodes.map(_.value.asInstanceOf[Interseccion]).toList: _*)
  
  def cambioPosicion(dt : Double) = {
    var tiempo = dt
    if(!intersecciones.isEmpty){
      while(tiempo !=0){
        val velocidadVehiculo = velocidad.limitarVelocidad(ruta.head.velocidadMaxima)
        //Side effects con el angulo
        //if (posicion == intersecciones.head.asInstanceOf[Punto] && intersecciones.length>=2) velocidad= Velocidad(velocidad.magnitud)(Angulo.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
        if (posicion == intersecciones.head.asInstanceOf[Punto] && intersecciones.length>=2) velocidad= Velocidad(velocidad.magnitud)(ruta.head.anguloDosPuntos(intersecciones.dequeue(), intersecciones.head))
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
object Vehiculo{
  
  def apply(): Vehiculo={
    
    val Simulacion = Main.objectSimulacion
    val r= scala.util.Random.nextFloat()
    val probCarros = Simulacion.proporciónCarros
    val probMotos = probCarros + Simulacion.proporciónMotos
    val probBuses = probMotos + Simulacion.proporciónBuses
    val probCamiones = probBuses + Simulacion.proporciónCamiones
    val probMotoTaxi = probCamiones + Simulacion.proporciónMotoTaxis
    val velocidad = Simulacion.minVelocidad + scala.util.Random.nextInt({Simulacion.maxVelocidad + 1 - Simulacion.minVelocidad})
    val recorrido = Recorrido()
    val nodo = recorrido.origen
    //val angulo = Angulo.anguloDosPuntos(nodo, recorrido.destino)
    val angulo = recorrido.camino.get.edges.map(_.label.asInstanceOf[Via]).toList(0).anguloDosPuntos(nodo, recorrido.destino)
    if (r>=0 && r<=probCarros)
      return Carro(nodo, Velocidad(velocidad)(angulo), recorrido)
    else if(r>probCarros && r<=probMotos)
      return Moto(nodo, Velocidad(velocidad)(angulo), recorrido)
    else if(r>probMotos && r<=probBuses)
      return Bus(nodo, Velocidad(velocidad)(angulo), recorrido)
    else if(r>probBuses && r<=probCamiones)
      return Camion(nodo, Velocidad(velocidad)(angulo), recorrido)
    else
      return MotoTaxi(nodo, Velocidad(velocidad)(angulo), recorrido)
  }
  
  def llenarVehiculos(minimo: Int, maximo: Int): ArrayBuffer[Vehiculo]={
    val cantidad = minimo + {scala.util.Random.nextInt(maximo+1 -minimo)}
    val todos = ArrayBuffer.fill(cantidad)(Vehiculo())
    todos
  }
  
  
}