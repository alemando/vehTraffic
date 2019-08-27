package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.{Via,Interseccion,Punto}
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.dimension._
import scala.collection.mutable.{Queue,ArrayBuffer}

class Viaje private (val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion, val camino: GrafoVia.grafo.Path ,
    val ruta: Queue[Via], val intersecciones: Queue[Interseccion]){
  
	def recorrerEnVehiculo(dt : Double, dtSimulacion: Double, tSimulacion: Double, xSemaforoFrenar: Int, xSemaforoAmarilloContinuar:Int):Unit = {
    
	  //Usado para definir el ángulo del auto cuando se cambia de vía
    def definirAngulo(interseccion: Interseccion, via: Via):Angulo= if (via.origen == interseccion) via.anguloOrigen else via.anguloDestino
    
    if(!intersecciones.isEmpty){
      
      val interseccionSiguiente = intersecciones.head
      val viaActual = ruta.head
      val distanciaCarroAInterseccion = vehiculo.posicion.longitudEntrePunto(interseccionSiguiente)
      val crucero = if(Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud) >= vehiculo.velocidadCrucero) true else false
      
      //Inicia el recorrido
      if(interseccionSiguiente==origen){
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
      }
      else{
      val semaforo = interseccionSiguiente.nodoSemaforo.semaforoEnVia(viaActual)
      val (estadoSemaforo, tiempoProximoCambioSemaforo) = interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, semaforo, dtSimulacion, tSimulacion)
      //Si estoy en la intersección final
        if(vehiculo.posicion == interseccionSiguiente.asInstanceOf[Punto]){
        //Termina recorrido
          if(interseccionSiguiente==destino){
            val interseccionActual = intersecciones.dequeue()
            val viaActual = ruta.dequeue()
          }
        //Cambia de calle
          else if(estadoSemaforo=="Verde" || estadoSemaforo=="Amarillo" || (estadoSemaforo=="Rojo" && tiempoProximoCambioSemaforo==0.0)){
          val interseccionActual = intersecciones.dequeue()
          val viaPasada = ruta.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, ruta.head))
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
          //Esperar en rojo
          else{
            vehiculo.aceleracion=0.0
            if(dt>tiempoProximoCambioSemaforo){
            recorrerEnVehiculo(dt-tiempoProximoCambioSemaforo, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        }
      }
      //Si estoy en la zona roja
      else if(distanciaCarroAInterseccion<= xSemaforoFrenar){
        if(estadoSemaforo=="Rojo"){
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoRestante=vehiculo.frenar(dt,tiempoFrenado, tiempoProximoCambioSemaforo, desaceleracion)
          if (tiempoFrenado==dt-tiempoRestante) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
          if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
        }
        //Si estoy en la zona roja pero estoy en verde o en amarillo y en la zona amarillo
        else if(estadoSemaforo=="Verde" || (estadoSemaforo=="Amarillo" && distanciaCarroAInterseccion <= xSemaforoAmarilloContinuar)){
          val tiempoRestante=vehiculo.moverse(dt,crucero, distanciaCarroAInterseccion)
          //El tiempo restante lo aplico en la intersección
          vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
          if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
        }
        //Amarillo y frenar
        else{
          val distanciaAZonaAmarillo = if(distanciaCarroAInterseccion-xSemaforoAmarilloContinuar<0.05)0.05 else distanciaCarroAInterseccion-xSemaforoAmarilloContinuar
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoAZonaAmarillo = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaAZonaAmarillo, desaceleracion), desaceleracion)
          val tiempoRestante=vehiculo.frenar(dt,tiempoAZonaAmarillo, tiempoProximoCambioSemaforo, desaceleracion)
          if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
        }
      }
      //Si estoy afuera de la zona de frenado
      else{
        val tiempoRestante=vehiculo.moverse(dt,crucero, (distanciaCarroAInterseccion- xSemaforoFrenar))
        //El tiempo restante lo aplico en la intersección
        if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
        }
      }
    }
  }
}



object Viaje{
  def apply(vehiculo: Vehiculo, listaIntersecciones: ArrayBuffer[Interseccion]): Viaje={
    val random1=scala.util.Random.nextInt(listaIntersecciones.length)
    var random2=scala.util.Random.nextInt(listaIntersecciones.length)
    while(random1 == random2) random2=scala.util.Random.nextInt(listaIntersecciones.length)
    val origen = listaIntersecciones(random1)
    val destino = listaIntersecciones(random2)
//    val origen = {val l=simulacion.listaIntersecciones.filter(_.nombre==Some("Juan 65"))
//      l(0)}
//    val destino ={val l=simulacion.listaIntersecciones.filter(_.nombre==Some("Boliv con 65"))
//      l(0)}
    val camino = GrafoVia.camino(origen, destino)
    val ruta: Queue[Via] = Queue(camino.get.edges.map(_.label.asInstanceOf[Via]).toList: _*)
    val intersecciones: Queue[Interseccion] = Queue(camino.get.nodes.map(_.value.asInstanceOf[Interseccion]).toList: _*)
    new Viaje(vehiculo, origen, destino, camino.get ,ruta, intersecciones)
  }
  
  def apply(vehiculo: Vehiculo, origen: Interseccion, destino: Interseccion, ruta: Queue[Via], intersecciones: Queue[Interseccion]) = {
     val camino = GrafoVia.camino(origen, destino)
     new Viaje(vehiculo, origen, destino, camino.get ,ruta, intersecciones)
  }
  
    def llenarViajes(vehiculos: ArrayBuffer[Vehiculo], intersecciones: ArrayBuffer[Interseccion]): ArrayBuffer[Viaje]={
    val todos:ArrayBuffer[Viaje] = ArrayBuffer()
    vehiculos.foreach(vehiculo =>{
      val viaje = Viaje(vehiculo,intersecciones)
      val nodo = viaje.intersecciones.head
      vehiculo.posicion=nodo
      val angulo = {if (nodo == viaje.ruta.head.origen)viaje.ruta.head.anguloOrigen
                  else viaje.ruta.head.anguloDestino}
      vehiculo.velocidad=Velocidad(0.0)(angulo)
      todos+=viaje
    })
    todos
  }
  
  
}