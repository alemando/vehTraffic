package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{MovimientoUniforme, Velocidad, Angulo}
import com.unalmed.vehTraffic.mallaVial.{Punto, Interseccion}
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.base.Recorrido
import scala.collection.mutable.{Queue, ArrayBuffer}

case class Vehiculo(placa : String)(pos : Punto, vel : Velocidad, recorrido: Recorrido) extends Movil(pos,vel) with MovimientoUniforme {

}

object Vehiculo{
  
  def apply(): Vehiculo={
    val r= scala.util.Random.nextFloat()
    val velocidad = Simulacion.minVelocidad + scala.util.Random.nextInt({Simulacion.maxVelocidad - Simulacion.minVelocidad})
    val recorrido = Recorrido()
    val nodo = recorrido.camino.startNode.asInstanceOf[Punto]
    var contador: Double= 0
    if (r<contador && r>={contador= contador+Simulacion.proporciónCarros; contador})
      return Carro(Carro.placa, nodo, Velocidad(velocidad,Angulo(0)), recorrido)
    else if(r<contador && r>={contador= contador+Simulacion.proporciónMotos; contador})
      return Moto(Moto.placa, nodo, Velocidad(velocidad,Angulo(0)), recorrido)
    else if(r<contador && r>={contador= contador+Simulacion.proporciónBuses; contador})
      return Bus(Bus.placa, nodo, Velocidad(velocidad,Angulo(0)), recorrido)
    else if(r<contador && r>={contador= contador+Simulacion.proporciónCamiones; contador})
      return Camion(Camion.placa, nodo, Velocidad(velocidad,Angulo(0)), recorrido)
    else
      return MotoTaxi(MotoTaxi.placa, nodo, Velocidad(velocidad,Angulo(0)), recorrido)
  }
  
  def llenarVehiculos(minimo: Int, maximo: Int): ArrayBuffer[Vehiculo]={
    val cantidad = minimo + {scala.util.Random.nextInt(maximo-minimo)}
    val todos = ArrayBuffer.fill(cantidad)(Vehiculo())
    todos
  }
  
  
}