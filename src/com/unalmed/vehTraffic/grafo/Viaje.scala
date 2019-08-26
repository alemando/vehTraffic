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

    if(!intersecciones.isEmpty){
      val interseccionSiguiente = intersecciones.head
      val viaActual = ruta.head
      val distanciaCarroAInterseccion = vehiculo.posicion.longitudEntrePunto(interseccionSiguiente)
      val crucero = if(Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud) >= vehiculo.velocidadCrucero) true else false
      if(interseccionSiguiente==origen){
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt)
      }
      else{
      val semaforo = interseccionSiguiente.nodoSemaforo.semaforoEnVia(viaActual)
      val (estadoSemaforo, tiempoProximoCambioSemaforo) = interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, semaforo, simulacion)
//      println("\n")
//      println(s"Movimiento hacia $interseccionSiguiente por $viaActual\n")
//      println(s"dt: $dt")
//      interseccionSiguiente.nodoSemaforo.semaforos.foreach(sem=>{
//        if(sem==semaforo)println(s"Este semáforo: ${interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, sem, simulacion)}")
//        /*else println(s"Otro semáforo: ${interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, sem, simulacion)}")*/})
//      
      if(dt<0){
        println("ERROR")
        println("ERROR")
        println("ERROR")
        println("ERROR")
        println("ERROR")
      }
      //Si estoy adentro de la zona de frenado
      else if(vehiculo.posicion == interseccionSiguiente.asInstanceOf[Punto]){
        //Iniciar el recorrido
        if(interseccionSiguiente==origen){
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt)
        }
        //Termina recorrido
        else if(interseccionSiguiente==destino){
          val interseccionActual = intersecciones.dequeue()
          val viaActual = ruta.dequeue()
        }
        //Cambia de calle
        else if(estadoSemaforo=="Verde" || estadoSemaforo=="Amarillo" || (estadoSemaforo=="Rojo" && tiempoProximoCambioSemaforo==0.0)){
        val interseccionActual = intersecciones.dequeue()
        val viaPasada = ruta.dequeue()
        vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, ruta.head))
        recorrerEnVehiculo(dt)
        }
        else{
          if(dt>tiempoProximoCambioSemaforo){
            recorrerEnVehiculo(dt-tiempoProximoCambioSemaforo)
          }
        }
      }
      //Si estoy en la zona roja
      else if(distanciaCarroAInterseccion<= simulacion.xSemaforoFrenar){
        if(estadoSemaforo=="Rojo"){
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoComparacion = if (tiempoFrenado<tiempoProximoCambioSemaforo)tiempoFrenado else tiempoProximoCambioSemaforo
          //Frenando sin llegar a la interseccion
          if (dt<tiempoComparacion){
            vehiculo.aplicarAceleracion(dt, desaceleracion)
          }
          //Freno en la intersección e implemento tiempo sobrante
          else{
            vehiculo.aplicarAceleracion(tiempoFrenado,desaceleracion)
            if(tiempoFrenado==tiempoComparacion) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            recorrerEnVehiculo(dt-tiempoComparacion)
          }
        }
        //Si estoy en la zona roja pero estoy en verde o en amarillo y en la zona amarillo
        else if(estadoSemaforo=="Verde" || (estadoSemaforo=="Amarillo" && distanciaCarroAInterseccion <= simulacion.xSemaforoAmarilloContinuar)){
          //Calculo la máxima distancia
        val (distanciaAceleracion, distanciaCrucero,tiempoAceleracion, tiempoCrucero): (Double,Double,Double,Double) = {
          if (crucero)(0.0,vehiculo.maximaDistanciaUniforme(dt),0.0,dt)
          else{
            val tiempoAcelerando = vehiculo.tiempoParaVelocidad(vehiculo.velocidadCrucero)
            if(dt>= tiempoAcelerando){
              val tiempoCrucero = dt-tiempoAcelerando
              (vehiculo.maximaDistanciaAcelerado(tiempoAcelerando),vehiculo.maximaDistanciaUniforme(tiempoCrucero),tiempoAcelerando,tiempoCrucero)
            }
            else (vehiculo.maximaDistanciaAcelerado(dt),0.0,dt,0.0)
          }
        }
        val maximaDistancia = distanciaAceleracion + distanciaCrucero
        //Si la distancia máxima no sobrepasa a la intersección
        if (maximaDistancia<distanciaCarroAInterseccion){
          //Aplico aceleración solo si hay tiempo de aceleracion
          if(tiempoAceleracion!=0)vehiculo.aplicarAceleracion(tiempoAceleracion)
          //Aplico crucero el resto del tiempo
          vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCrucero)
        }
        //Si la máxima distancia sobrepasa a la zona de intersección
        else /*if(maximaDistancia>=distanciaCarroAInterseccion-simulacion.xSemaforoFrenar)*/{
          val distanciaInterseccion:Double ={ if(distanciaCarroAInterseccion<0.05)0.05 else distanciaCarroAInterseccion}
          //Encuentro el tiempo sobrante luego de aplicar 
          val tiempoRestante:Double ={
            //Aplico aceleración hasta llegar a la intersección
            if(distanciaInterseccion<distanciaAceleracion){
              val tiempoAceleracionRecortado = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaInterseccion))
              vehiculo.aplicarAceleracion(tiempoAceleracionRecortado)
              dt-tiempoAceleracionRecortado
            }
            else{
              //Aplico aceleración hasta llegar a crucero y luego aplico crucero hasta la intersección
              vehiculo.aplicarAceleracion(tiempoAceleracion)
              val tiempoCruceroRecortado = (distanciaInterseccion-distanciaAceleracion)/vehiculo.velocidadCrucero
              vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCruceroRecortado)
              dt-tiempoAceleracion-tiempoCruceroRecortado
            }
          }
          //El tiempo restante lo aplico en la intersección
          vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
          recorrerEnVehiculo(tiempoRestante)
        }
        }
        //Amarillo y frenar
        else{
          val distanciaAZonaAmarillo = if(distanciaCarroAInterseccion-simulacion.xSemaforoAmarilloContinuar<0.05)0.05 else distanciaCarroAInterseccion-simulacion.xSemaforoAmarilloContinuar
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoAZonaAmarillo = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaAZonaAmarillo, desaceleracion), desaceleracion)
          val tiempoComparacion = if (tiempoAZonaAmarillo<tiempoProximoCambioSemaforo)tiempoAZonaAmarillo else tiempoProximoCambioSemaforo
          //Frenando sin llegar a la zona amarilla
          if (dt<tiempoComparacion){
            vehiculo.aplicarAceleracion(dt, desaceleracion)
          }
          //Freno hasta zona amarilla e implemento tiempo sobrante
          else{
            vehiculo.aplicarAceleracion(tiempoAZonaAmarillo,desaceleracion)
//            if(tiempoFrenado==tiempoComparacion) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            recorrerEnVehiculo(dt-tiempoComparacion)
          }
        }
      }
      //Si estoy afuera de la zona de frenado
      else /*if(distanciaCarroAInterseccion> simulacion.xSemaforoFrenar)*/{
        //Calculo la máxima distancia
        val (distanciaAceleracion, distanciaCrucero,tiempoAceleracion, tiempoCrucero): (Double,Double,Double,Double) = {
          if (crucero)(0.0,vehiculo.maximaDistanciaUniforme(dt),0.0,dt)
          else{
            val tiempoAcelerando = vehiculo.tiempoParaVelocidad(vehiculo.velocidadCrucero)
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
          //Aplico aceleración solo si hay tiempo de aceleracion
          if(tiempoAceleracion!=0)vehiculo.aplicarAceleracion(tiempoAceleracion)
          //Aplico crucero el resto del tiempo
          vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCrucero)
        }
        //Si la máxima distancia sobrepasa a la zona de frenado
        else /*if(maximaDistancia>=distanciaCarroAInterseccion-simulacion.xSemaforoFrenar)*/{
          val distanciaRojo:Double ={ if(distanciaCarroAInterseccion-simulacion.xSemaforoFrenar<0.05)0.05 else distanciaCarroAInterseccion-simulacion.xSemaforoFrenar}
          //Encuentro el tiempo sobrante luego de aplicar 
          val tiempoRestante:Double ={
            //Aplico aceleración hasta llegar al límite de zona de frenado
            if(distanciaRojo<distanciaAceleracion){
              val tiempoAceleracionRecortado = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaRojo))
              vehiculo.aplicarAceleracion(tiempoAceleracionRecortado)
              dt-tiempoAceleracionRecortado
            }
            else{
              //Aplico aceleración hasta llegar a crucero y luego aplico crucero hasta el límite de zona roja
              if (tiempoAceleracion!=0) vehiculo.aplicarAceleracion(tiempoAceleracion)
              val tiempoCruceroRecortado = (distanciaRojo-distanciaAceleracion)/vehiculo.velocidadCrucero
              vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCruceroRecortado)
              dt-tiempoAceleracion-tiempoCruceroRecortado
            }
          }
          //El tiempo restante lo aplico en la zona de frenado
          recorrerEnVehiculo(tiempoRestante)
        }
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