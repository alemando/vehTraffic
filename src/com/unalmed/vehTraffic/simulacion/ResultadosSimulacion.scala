package com.unalmed.vehTraffic.simulacion
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.vehiculo._
import com.unalmed.vehTraffic.dimension.{Sentido, Velocidad}
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.util.JsonRW

class ResultadosSimulacion {
  
  //Obtener clase base para resultados (inicializada con todos los valores en 0)
  val result = JsonRW.getResultBaseClass
  
  //Vehiculos
  val totalVehiculos = Simulacion.listaVehiculos.length
  result.resultadosSimulacion.vehiculos.total_=(totalVehiculos)
  val totalCarros = Simulacion.listaVehiculos.filter(_.isInstanceOf[Carro]).length
  result.resultadosSimulacion.vehiculos.carros_=(totalCarros)
  val totalMotos = Simulacion.listaVehiculos.filter(_.isInstanceOf[Moto]).length
  result.resultadosSimulacion.vehiculos.motos_=(totalMotos)
  val totalBuses = Simulacion.listaVehiculos.filter(_.isInstanceOf[Bus]).length
  result.resultadosSimulacion.vehiculos.buses_=(totalBuses)
  val totalCamiones = Simulacion.listaVehiculos.filter(_.isInstanceOf[Camion]).length
  result.resultadosSimulacion.vehiculos.camiones_=(totalCamiones)
  val totalMotoTaxis = Simulacion.listaVehiculos.filter(_.isInstanceOf[MotoTaxi]).length
  result.resultadosSimulacion.vehiculos.motoTaxis_=(totalMotoTaxis)
  
  //Malla vial
  val totalVias = Simulacion.listaVias.length
  result.resultadosSimulacion.mallaVial.vias_=(totalVias)
  val totalIntersecciones = Simulacion.listaIntersecciones.length
  result.resultadosSimulacion.mallaVial.intersecciones_=(totalIntersecciones)
  val viasUnSentido = Simulacion.listaVias.filter(_.sentido==Sentido.unaVia).length
  result.resultadosSimulacion.mallaVial.viasUnSentido_=(viasUnSentido)
  val viasDobleSentido = Simulacion.listaVias.filter(_.sentido==Sentido.dobleVia).length
  result.resultadosSimulacion.mallaVial.viasDobleSentido_=(viasDobleSentido)
  val velocidadMinimaVias = Simulacion.listaVias.map(_.velocidadMaxima).min
  result.resultadosSimulacion.mallaVial.velocidadMinima_=(velocidadMinimaVias.toInt)
  val velocidadMaximaVias = Simulacion.listaVias.map(_.velocidadMaxima).max
  result.resultadosSimulacion.mallaVial.velocidadMaxima_=(velocidadMaximaVias.toInt)
  val longitudPromedio = Simulacion.listaVias.map(_.longitud).reduce(_+_)/totalVias
  result.resultadosSimulacion.mallaVial.longitudPromedio_=(longitudPromedio)
  
  //Vehiculos en intersección
  val promedioOrigen= {val listaCantidad = Simulacion.listaVehiculos.groupBy(_.recorrido.origen).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.promedioOrigen_=(promedioOrigen)
  val promedioDestino = {val listaCantidad = Simulacion.listaVehiculos.groupBy(_.recorrido.destino).mapValues(_.size).toArray.map(_._2)
                       listaCantidad.reduce(_+_) / listaCantidad.length}
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.promedioDestino_=(promedioDestino)
  val sinOrigen = Simulacion.listaIntersecciones.diff(Simulacion.listaVehiculos.groupBy(_.recorrido.origen).toArray.map(_._1)).length
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.sinOrigen_=(sinOrigen)
  val sinDestino = Simulacion.listaIntersecciones.diff(Simulacion.listaVehiculos.groupBy(_.recorrido.destino).toArray.map(_._1)).length
  result.resultadosSimulacion.mallaVial.vehiculosEnInterseccion.sinDestino_=(sinDestino)
  
  //Tiempos
  val tiempoSimulacion = Simulacion.t
  result.resultadosSimulacion.tiempos.simulacion=tiempoSimulacion
  val tiempoRealidad = (Simulacion.t/Simulacion.dt)*(Simulacion.tRefresh/1000.0)
  result.resultadosSimulacion.tiempos.realidad=tiempoRealidad
  
  //Velocidad vehículos
  val velocidadMinimaVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).min
  result.resultadosSimulacion.velocidades.minima=velocidadMinimaVehiculos.toInt
  val velocidadMaximaVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).max
  result.resultadosSimulacion.velocidades.maxima=velocidadMaximaVehiculos.toInt
  val velocidadPromedioVehiculos = Simulacion.listaVehiculos.map(_.velocidad.magnitud).reduce(_+_)/totalVehiculos
  result.resultadosSimulacion.velocidades.promedio=velocidadPromedioVehiculos
  
  //Distancia vehículos
  val distanciaMinima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).min
  result.resultadosSimulacion.distancias.minima_=(distanciaMinima.toInt)
  val distanciaMaxima = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).max
  result.resultadosSimulacion.distancias.maxima_=(distanciaMaxima.toInt)
  val distanciaPromedio = Simulacion.listaVehiculos.map(_.recorrido.camino.get.edges.map(_.label.asInstanceOf[Via].longitud).toList.reduce(_+_)).reduce(_+_)/totalVehiculos
  result.resultadosSimulacion.distancias.promedio_=(distanciaPromedio)
  
  //Escribir json
  JsonRW.writeResult(result)
  
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