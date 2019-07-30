package com.unalmed.vehTraffic.simulacion

import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.dimension.{Sentido, Velocidad}
import com.unalmed.vehTraffic.mallaVial.Via

class ResultadosSimulacion {
  val totalVehiculos = Simulacion.listaVehiculos.length
  val totalCarros = Simulacion.listaVehiculos.filter(_.isInstanceOf[Carro]).length
  val totalMotos = Simulacion.listaVehiculos.filter(_.isInstanceOf[Moto]).length
  val totalBuses = Simulacion.listaVehiculos.filter(_.isInstanceOf[Bus]).length
  val totalCamiones = Simulacion.listaVehiculos.filter(_.isInstanceOf[Camion]).length
  val totalMotoTaxis = Simulacion.listaVehiculos.filter(_.isInstanceOf[MotoTaxi]).length
  
  val totalVias = Simulacion.listaVias.length
  val totalIntersecciones = Simulacion.listaIntersecciones.length
  val viasUnSentido = Simulacion.listaVias.filter(_.sentido==Sentido.unaVia).length
  val viasDobleSentido = Simulacion.listaVias.filter(_.sentido==Sentido.dobleVia).length
  val velocidadMinima = Simulacion.listaVias.map(_.velocidadMaxima).min
  val velocidadMaxima = Simulacion.listaVias.map(_.velocidadMaxima).max
  val longitudPromedio = Simulacion.listaVias.map(_.longitud).reduce(_+_)/totalVias
  
  //val promedioOrigen = Simulacion.listaVehiculos.groupBy(_.recorrido.origen).mapValues(_.length).reduce(_+_)
  
  val velocidadMinimaVehiculos = Velocidad.metroAkilometro(Simulacion.listaVehiculos.map(_._velocidad.magnitud).min)
  val velocidadMaximaVehiculos = Velocidad.metroAkilometro(Simulacion.listaVehiculos.map(_._velocidad.magnitud).max)
  val velocidadPromedioVehiculos = Velocidad.metroAkilometro(Simulacion.listaVehiculos.map(_._velocidad.magnitud).reduce(_+_))/totalVehiculos
  
  val distanciaMinima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).min
  val distanciaMaxima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).max
  val distanciaPromedio = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).reduce(_+_)/totalVehiculos
}