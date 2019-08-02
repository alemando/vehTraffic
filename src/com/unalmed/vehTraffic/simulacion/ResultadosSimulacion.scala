package com.unalmed.vehTraffic.simulacion
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.dimension.{Sentido, Velocidad}
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion
object ResultadosSimulacion {
  
  //Vehiculos
  val totalVehiculos = Simulacion.listaVehiculos.length
  val totalCarros = Simulacion.listaVehiculos.filter(_.isInstanceOf[Carro]).length
  val totalMotos = Simulacion.listaVehiculos.filter(_.isInstanceOf[Moto]).length
  val totalBuses = Simulacion.listaVehiculos.filter(_.isInstanceOf[Bus]).length
  val totalCamiones = Simulacion.listaVehiculos.filter(_.isInstanceOf[Camion]).length
  val totalMotoTaxis = Simulacion.listaVehiculos.filter(_.isInstanceOf[MotoTaxi]).length
  
  //Malla vial
  val totalVias = Simulacion.listaVias.length
  val totalIntersecciones = Simulacion.listaIntersecciones.length
  val viasUnSentido = Simulacion.listaVias.filter(_.sentido==Sentido.unaVia).length
  val viasDobleSentido = Simulacion.listaVias.filter(_.sentido==Sentido.dobleVia).length
  val velocidadMinimaVias = Simulacion.listaVias.map(_.velocidadMaxima).min
  val velocidadMaximaVias = Simulacion.listaVias.map(_.velocidadMaxima).max
  val longitudPromedio = Simulacion.listaVias.map(_.longitud).reduce(_+_)/totalVias
  //Vehiculos en intersección
  val promedioOrigen= {val listaCantidad = Simulacion.listaVehiculos.groupBy(_.recorrido.origen).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  val promedioDestino = {val listaCantidad = Simulacion.listaVehiculos.groupBy(_.recorrido.destino).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  val sinOrigen = Simulacion.listaIntersecciones.diff(Simulacion.listaVehiculos.groupBy(_.recorrido.origen).toArray.map(_._1)).length
  val sinDestino = Simulacion.listaIntersecciones.diff(Simulacion.listaVehiculos.groupBy(_.recorrido.destino).toArray.map(_._1)).length
  
  //Tiempos
  val tiempoSimulacion = Simulacion.t
  val tiempoRealidad = (Simulacion.t/Simulacion.dt)*Simulacion.tRefresh
  
  //Velocidad vehículos
  val velocidadMinimaVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).min
  val velocidadMaximaVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).max
  val velocidadPromedioVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).reduce(_+_)/totalVehiculos
  
  //Distancia vehículos
  val distanciaMinima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).min
  val distanciaMaxima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).max
  val distanciaPromedio = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).reduce(_+_)/totalVehiculos

  def imprimir():Unit={
    println(s"Total vehiculos: $totalVehiculos")
    println(s"Total carros: $totalCarros")
    println(s"Total motos: $totalMotos")
    println(s"Total buses: $totalBuses")
    println(s"Total camiones: $totalCamiones")
    println(s"Total moto taxis: $totalMotoTaxis")
    
    println(s"Total vias: $totalVias")
    println(s"Total intersecciones: $totalIntersecciones")
    println(s"Total vias un sentido: $viasUnSentido")
    println(s"Total vias doble sentido: $viasDobleSentido")
    println(s"Velocidad mínima vías: $velocidadMinimaVias")
    println(s"Velocidad máxima vías: $velocidadMaximaVias")
    println(s"Longitud promedio de vías: $longitudPromedio")
    println(s"Promedio origen: $promedioOrigen")
    println(s"Promedio destino: $promedioDestino")
    println(s"Sin origen: $sinOrigen")
    println(s"Sin destino: $sinDestino")
    
    println(s"Tiempo simulación: $tiempoSimulacion")
    println(s"Tiempo realidad: $tiempoRealidad")
    
    println(s"Velocidad mínima vehículos: $velocidadMinimaVehiculos")
    println(s"Velocidad máxima vehículos: $velocidadMaximaVehiculos")
    println(s"Velocidad promedio vehículos: $velocidadPromedioVehiculos")
    
    println(s"Distancia mínima: $distanciaMinima")
    println(s"Distancia máxima: $distanciaMaxima")
    println(s"Distancia promedio: $distanciaPromedio")
    }
}