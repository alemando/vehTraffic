package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.{Via,Interseccion,Punto}
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.dimension._
import scala.collection.mutable.{Queue,ArrayBuffer}

class Viaje private (val origen: Interseccion, val destino: Interseccion, val camino: GrafoVia.grafo.Path ,val ruta: Queue[Via], val intersecciones: Queue[Interseccion])(simulacion:Simulacion){
  val vehiculo = Vehiculo(simulacion,this)
  
  def recorrerEnVehiculo(dt : Double):Unit = {
    
    def definirAngulo(interseccion: Interseccion, via: Via):Angulo= if (via.origen == interseccion) via.anguloOrigen else via.anguloDestino
//    println("\n")
//    println("dt: "+dt)
//    println("Velocidad actual: " + vehiculo.velocidad)
    if(!intersecciones.isEmpty){
      val interseccionSiguiente = intersecciones.head
      val viaActual = ruta.head
      val distanciaCarroAInterseccion = vehiculo.posicion.longitudEntrePunto(interseccionSiguiente)
      val crucero = if(Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud) >= vehiculo.velocidadCrucero) true else false
      if(dt<0){
        println("ERROR")
        println("ERROR")
        println("ERROR")
        println("ERROR")
        println("ERROR")
      }
      //Si estoy adentro de la zona de frenado
      else if(vehiculo.posicion == interseccionSiguiente.asInstanceOf[Punto]){
        if(interseccionSiguiente==origen){
//          println("Empieza el recorrido")
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt)
        }
        else if(interseccionSiguiente==destino){
//          println("Termina el recorrido")
          val interseccionActual = intersecciones.dequeue()
          val viaActual = ruta.dequeue()
        }
        else{
//        println("Cambio de calle")
        val interseccionActual = intersecciones.dequeue()
        val viaPasada = ruta.dequeue()
        vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, ruta.head))
        recorrerEnVehiculo(dt)
        }
      }
      else if(distanciaCarroAInterseccion<= simulacion.xSemaforoFrenar){
        val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
        if (dt<tiempoFrenado){
          vehiculo.aplicarDesaceleracion(dt, desaceleracion)
//          println("Estoy frenando pero desde rojo")
//          println(s"Velocidad actual: ${vehiculo.velocidad}")
        }
        else{
          vehiculo.aplicarDesaceleracion(tiempoFrenado,desaceleracion)
          vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
//          println("Freno y me queda tiempo")
//          println(s"Velocidad actual: ${vehiculo.velocidad}")
          if(!(dt==tiempoFrenado)) recorrerEnVehiculo(dt-tiempoFrenado)
        }
      }
      //Si estoy afuera de la zona de frenado
      else /*if(distanciaCarroAInterseccion> simulacion.xSemaforoFrenar)*/{
        val (distanciaAceleracion, distanciaCrucero,tiempoAceleracion, tiempoCrucero): (Double,Double,Double,Double) = {
          if (crucero)(0.0,vehiculo.maximaDistanciaUniforme(dt),0.0,dt)
          else{
            val tiempoAcelerando = vehiculo.tiempoMaximaVelocidad(vehiculo.velocidadCrucero)
            if(dt>= tiempoAcelerando){
              val tiempoCrucero = dt-tiempoAcelerando
              (vehiculo.maximaDistanciaAcelerado(tiempoAcelerando),vehiculo.maximaDistanciaUniforme(tiempoCrucero),tiempoAcelerando,tiempoCrucero)
            }
            else (vehiculo.maximaDistanciaAcelerado(dt),0.0,dt,0.0)
          }
        }
        val maximaDistancia = distanciaAceleracion + distanciaCrucero
        //Si la distancia máxima no sobrepasa a la zona de frenado
        if (maximaDistancia<distanciaCarroAInterseccion-simulacion.xSemaforoFrenar){
//          println(s"Estoy acelerando por $tiempoAceleracion tiempo")
//          println(s"Velocidad previa: ${vehiculo.velocidad}")
          if(tiempoAceleracion!=0)vehiculo.aplicarAceleracion(tiempoAceleracion)
//          println(s"Velocidad después de acelerar: ${vehiculo.velocidad}")
//          println(s"Estoy en crucero por $tiempoCrucero tiempo")
//          println(s"Velocidad previa: ${vehiculo.velocidad}")
          vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCrucero)
//          println(s"Velocidad después de crucero: ${vehiculo.velocidad}")
        }
        else /*if(maximaDistancia>=distanciaCarroAInterseccion-simulacion.xSemaforoFrenar)*/{
          val distanciaRojo:Double ={ if(distanciaCarroAInterseccion-simulacion.xSemaforoFrenar<0.05)0.05 else distanciaCarroAInterseccion-simulacion.xSemaforoFrenar}
//          println(s"Distancia rojo: $distanciaRojo")
//          println(s"Distancia aceleración: $distanciaAceleracion")
          val tiempoRestante:Double ={
            if(distanciaRojo<distanciaAceleracion){
//              println("DistanciaRojo<distanciaAceleración")
              val tiempoAceleracionRecortado = vehiculo.tiempoMaximaVelocidad(vehiculo.velocidadEnDistancia(distanciaRojo))
//              println(tiempoAceleracionRecortado)
//              println(dt-tiempoAceleracionRecortado)
              vehiculo.aplicarAceleracion(tiempoAceleracionRecortado)
              dt-tiempoAceleracionRecortado
            }
            else{
//              println("DistanciaRojo>=distanciaAceleración")
              vehiculo.aplicarAceleracion(tiempoAceleracion)
              val tiempoCruceroRecortado = (distanciaRojo-distanciaAceleracion)/vehiculo.velocidadCrucero
              vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCruceroRecortado)
//              println(s"Tiempo crucero recortado: $tiempoCruceroRecortado")
//              println(dt-tiempoAceleracion-tiempoCruceroRecortado)
              dt-tiempoAceleracion-tiempoCruceroRecortado
            }
          }
//          println("Llegando a la zona de frenado")
          recorrerEnVehiculo(tiempoRestante)
        }
      }
    }
  }
}


object Viaje{
  def apply(simulacion: Simulacion): Viaje={
    val random1=scala.util.Random.nextInt(simulacion.listaIntersecciones.length)
    var random2=scala.util.Random.nextInt(simulacion.listaIntersecciones.length)
    while(random1 == random2) random2=scala.util.Random.nextInt(simulacion.listaIntersecciones.length)
    val origen = simulacion.listaIntersecciones(random1)
    val destino = simulacion.listaIntersecciones(random2)
//    val origen = {val l=simulacion.listaIntersecciones.filter(_.nombre==Some("Aguacatala"))
//      l(0)}
//    val destino ={val l=simulacion.listaIntersecciones.filter(_.nombre==Some("Parque Pob"))
//      l(0)}
    val camino = GrafoVia.camino(origen, destino)
    val ruta: Queue[Via] = Queue(camino.get.edges.map(_.label.asInstanceOf[Via]).toList: _*)
    val intersecciones: Queue[Interseccion] = Queue(camino.get.nodes.map(_.value.asInstanceOf[Interseccion]).toList: _*)
    new Viaje(origen, destino, camino.get ,ruta, intersecciones)(simulacion)
  }
  
    def llenarViajes(minimo: Int, maximo: Int, simulacion: Simulacion): ArrayBuffer[Viaje]={
    val cantidad = minimo + {scala.util.Random.nextInt(maximo+1 -minimo)}
    val todos = ArrayBuffer.fill(cantidad)(Viaje(simulacion))
    todos
  }
  
  
}