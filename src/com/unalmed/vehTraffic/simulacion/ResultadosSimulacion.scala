package com.unalmed.vehTraffic.simulacion
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.dimension.{Velocidad}
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.util.JsonRW

class ResultadosSimulacion(simulacion:Simulacion) {
  
  //Obtener clase base para resultados (inicializada con todos los valores en 0)
  val result = JsonRW.getResultBaseClass
  
  //Vehiculos
  val totalVehiculos = simulacion.listaVehiculos.length
  result.resultadosSimulacion.vehiculos.total=totalVehiculos
  val totalCarros = simulacion.listaVehiculos.filter(_.isInstanceOf[Carro]).length
  result.resultadosSimulacion.vehiculos.carros=totalCarros
  val totalMotos = simulacion.listaVehiculos.filter(_.isInstanceOf[Moto]).length
  result.resultadosSimulacion.vehiculos.motos=totalMotos
  val totalBuses = simulacion.listaVehiculos.filter(_.isInstanceOf[Bus]).length
  result.resultadosSimulacion.vehiculos.buses=totalBuses
  val totalCamiones = simulacion.listaVehiculos.filter(_.isInstanceOf[Camion]).length
  result.resultadosSimulacion.vehiculos.camiones=totalCamiones
  val totalMotoTaxis = simulacion.listaVehiculos.filter(_.isInstanceOf[MotoTaxi]).length
  result.resultadosSimulacion.vehiculos.motoTaxis=totalMotoTaxis
  
  //Malla vial
  val totalVias = simulacion.listaVias.length
  result.resultadosSimulacion.mallaVial.vias=totalVias
  val totalIntersecciones = simulacion.listaIntersecciones.length
  result.resultadosSimulacion.mallaVial.intersecciones=totalIntersecciones
  val viasUnSentido = simulacion.listaVias.filter(_.sentido==Sentido.unaVia).length
  result.resultadosSimulacion.mallaVial.viasUnSentido=viasUnSentido
  val viasDobleSentido = simulacion.listaVias.filter(_.sentido==Sentido.dobleVia).length
  result.resultadosSimulacion.mallaVial.viasDobleSentido=viasDobleSentido
  val velocidadMinimaVias = simulacion.listaVias.map(_.velocidadMaxima).min
  result.resultadosSimulacion.mallaVial.velocidadMinima=velocidadMinimaVias.toInt
  val velocidadMaximaVias = simulacion.listaVias.map(_.velocidadMaxima).max
  result.resultadosSimulacion.mallaVial.velocidadMaxima=velocidadMaximaVias.toInt
  val longitudPromedio = simulacion.listaVias.map(_.longitud).reduce(_+_)/totalVias
  result.resultadosSimulacion.mallaVial.longitudPromedio=longitudPromedio
  
  //Vehiculos en intersección
  val promedioOrigen= {val listaCantidad = simulacion.listaViajes.groupBy(_.origen).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.promedioOrigen=promedioOrigen
  val promedioDestino = {val listaCantidad = simulacion.listaViajes.groupBy(_.destino).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.promedioDestino=promedioDestino
  val sinOrigen = simulacion.listaIntersecciones.diff(simulacion.listaViajes.groupBy(_.origen).toArray.map(_._1)).length
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.sinOrigen=sinOrigen
  val sinDestino = simulacion.listaIntersecciones.diff(simulacion.listaViajes.groupBy(_.destino).toArray.map(_._1)).length
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.sinDestino=sinDestino
  
  //Tiempos
  val tiempoSimulacion = simulacion.t
  result.resultadosSimulacion.tiempos.simulacion=tiempoSimulacion
  val tiempoRealidad = (simulacion.t/simulacion.dt)*(simulacion.tRefresh/1000.0)
  result.resultadosSimulacion.tiempos.realidad=tiempoRealidad
  
  //Velocidad vehículos
  val velocidadMinimaVehiculos = simulacion.listaVehiculos.map(_.velocidad.magnitud).min
  result.resultadosSimulacion.velocidades.minima=velocidadMinimaVehiculos.toInt
  val velocidadMaximaVehiculos = simulacion.listaVehiculos.map(_.velocidad.magnitud).max
  result.resultadosSimulacion.velocidades.maxima=velocidadMaximaVehiculos.toInt
  val velocidadPromedioVehiculos = simulacion.listaVehiculos.map(_.velocidad.magnitud).reduce(_+_)/totalVehiculos
  result.resultadosSimulacion.velocidades.promedio=velocidadPromedioVehiculos
  
  //Distancia vehículos
  val distanciaMinima = simulacion.listaViajes.map(_.camino.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).min
  result.resultadosSimulacion.distancias.minima=distanciaMinima.toInt
  val distanciaMaxima = simulacion.listaViajes.map(_.camino.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).max
  result.resultadosSimulacion.distancias.maxima=distanciaMaxima.toInt
  val distanciaPromedio = simulacion.listaViajes.map(_.camino.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).reduce(_+_)/totalVehiculos
  result.resultadosSimulacion.distancias.promedio=distanciaPromedio
  
  //Comparendos
  val cantidadComparendos=simulacion.listaComparendos.size
  val promedioPorcentajeExceso={
    var suma=simulacion.listaComparendos.map(_.porcentajeExcedido).reduce(_+_)
    var pr=suma/cantidadComparendos
    pr
  }
  
//  Escribir json
  JsonRW.writeResult(result)
  
  //Esta función permite ver los resultados por pantalla, lo dejamos aquí por si requiere usarlo
  //Para activarlo tiene que ir a Simulacion y quitar el comentario
//  def imprimir():Unit={
//    println(s"Total vehiculos: $totalVehiculos")
//    println(s"Total carros: $totalCarros")
//    println(s"Total motos: $totalMotos")
//    println(s"Total buses: $totalBuses")
//    println(s"Total camiones: $totalCamiones")
//    println(s"Total moto taxis: $totalMotoTaxis")
//    
//    println(s"Total vias: $totalVias")
//    println(s"Total intersecciones: $totalIntersecciones")
//    println(s"Total vias un sentido: $viasUnSentido")
//    println(s"Total vias doble sentido: $viasDobleSentido")
//    println(s"Velocidad mínima vías: $velocidadMinimaVias")
//    println(s"Velocidad máxima vías: $velocidadMaximaVias")
//    println(s"Longitud promedio de vías: $longitudPromedio")
//    println(s"Promedio origen: $promedioOrigen")
//    println(s"Promedio destino: $promedioDestino")
//    println(s"Sin origen: $sinOrigen")
//    println(s"Sin destino: $sinDestino")
//    
//    println(s"Tiempo simulación: $tiempoSimulacion")
//    println(s"Tiempo realidad: $tiempoRealidad")
//    
//    println(s"Velocidad mínima vehículos: $velocidadMinimaVehiculos")
//    println(s"Velocidad máxima vehículos: $velocidadMaximaVehiculos")
//    println(s"Velocidad promedio vehículos: $velocidadPromedioVehiculos")
//    
//    println(s"Distancia mínima: $distanciaMinima")
//    println(s"Distancia máxima: $distanciaMaxima")
//    println(s"Distancia promedio: $distanciaPromedio")
//    }
}