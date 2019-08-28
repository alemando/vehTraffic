package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.{Via,Interseccion,Punto,CamaraFotoDeteccion}
import com.unalmed.vehTraffic.vehiculo.{Vehiculo, Comparendo}
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.dimension._
import scala.collection.mutable.{Queue,ArrayBuffer}

class Viaje private (val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion, val camino: GrafoVia.grafo.Path ,
    val ruta: Queue[Via], val intersecciones: Queue[Interseccion]){
  
	def recorrerEnVehiculo(dt : Double, dtSimulacion: Double, tSimulacion: Double, xSemaforoFrenar: Int, xSemaforoAmarilloContinuar:Int,
	    listaComparendos:ArrayBuffer[Comparendo]):Unit = {
    
	  //Usado para definir el ángulo del auto cuando se cambia de vía
    def definirAngulo(interseccion: Interseccion, via: Via):Angulo= if (via.origen == interseccion) via.anguloOrigen else via.anguloDestino
    
    def calcularDistanciaACamara(distanciaAInterseccion: Double, interseccionSiguiente: Interseccion, viaActual: Via, camara: Option[CamaraFotoDeteccion]):Double={
      if(interseccionSiguiente==viaActual.fin) distanciaAInterseccion-(viaActual.longitud-camara.get.longitudRespectoOrigen)
      else distanciaAInterseccion-camara.get.longitudRespectoOrigen
    }
    
    //El método estaba más simplificado pero al agregar las comprobaciones de la cámara se exendió muchísimo
    if(!intersecciones.isEmpty){
      
      val interseccionSiguiente = intersecciones.head
      val viaActual = ruta.head
      val distanciaCarroAInterseccion = vehiculo.posicion.longitudEntrePunto(interseccionSiguiente)
      val crucero = if(Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud) >= vehiculo.velocidadCrucero) true else false
      val camaraEnVia: Boolean = ruta.head.fotomulta.isDefined
      val camara: Option[CamaraFotoDeteccion]= ruta.head.fotomulta
      
      //Inicia el recorrido
      if(interseccionSiguiente==origen){
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
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
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
          //Esperar en rojo
          else{
            vehiculo.aceleracion=0.0
            if(dt>tiempoProximoCambioSemaforo){
            recorrerEnVehiculo(dt-tiempoProximoCambioSemaforo, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
        }
      }
      //Si estoy en la zona roja
      else if(distanciaCarroAInterseccion<= xSemaforoFrenar){
        if(estadoSemaforo=="Rojo"){
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          //En caso de que haya una cámara en la vía, compruebo
          if(camaraEnVia){
            val distanciaACamara=calcularDistanciaACamara(distanciaCarroAInterseccion, interseccionSiguiente, viaActual, camara)
            val tiempoACamara=vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaACamara, desaceleracion), desaceleracion)
            //Si es posible pasar por la cámara en el próximo movimiento
            if(distanciaACamara>0.0 && distanciaACamara<=distanciaCarroAInterseccion && tiempoACamara<=tiempoProximoCambioSemaforo){
              val tiempoRestante=vehiculo.frenar(dt,tiempoFrenado, tiempoACamara, desaceleracion)
              if (tiempoACamara==dt-tiempoRestante) camara.get.comprobar(vehiculo, listaComparendos)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
            }
            //Si no me preocupo por la cámara
            else{
              val tiempoRestante=vehiculo.frenar(dt,tiempoFrenado, tiempoProximoCambioSemaforo, desaceleracion)
              if (tiempoFrenado==dt-tiempoRestante) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
            }
          }
          //Si no hay cámara
          else{
            val tiempoRestante=vehiculo.frenar(dt,tiempoFrenado, tiempoProximoCambioSemaforo, desaceleracion)
            if (tiempoFrenado==dt-tiempoRestante) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
        }
        //Si estoy en la zona roja pero estoy en verde o en amarillo y en la zona amarillo
        else if(estadoSemaforo=="Verde" || (estadoSemaforo=="Amarillo" && distanciaCarroAInterseccion <= xSemaforoAmarilloContinuar)){
          //Otra vez se aplica en caso de que haya una cámara en la vía
          if(camaraEnVia){
            val distanciaACamara=calcularDistanciaACamara(distanciaCarroAInterseccion, interseccionSiguiente, viaActual, camara)
            //Si la cámara está dentro de mis límites
            val distanciaACambioSemaforo=vehiculo.maximaDistancia(tiempoProximoCambioSemaforo, crucero, vehiculo.velocidadCrucero)
            if(distanciaACamara>0.0 && distanciaACamara<=distanciaCarroAInterseccion && distanciaACamara<=distanciaACambioSemaforo){
              val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, distanciaACamara)
              if(enLimite)camara.get.comprobar(vehiculo, listaComparendos)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
            }
            //Si no me tengo que preocupar por la cámara
            else{
	            if(dt<=tiempoProximoCambioSemaforo){
	              val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, distanciaCarroAInterseccion)
                if (enLimite) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
                if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
	            }
	            else{
	              val (tiempoRestante,enLimite)=vehiculo.moverse(tiempoProximoCambioSemaforo,crucero, distanciaCarroAInterseccion)
                if (enLimite) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
                val tiempoPorAplicar = dt-tiempoProximoCambioSemaforo+tiempoRestante
                if (tiempoPorAplicar>0) recorrerEnVehiculo(tiempoPorAplicar, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
	            }
            }
          }
          //Si no hay cámara
	        else{
	          if(dt<=tiempoProximoCambioSemaforo){
	            val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, distanciaCarroAInterseccion)
              if (enLimite) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
	          }
	          else{
	            val (tiempoRestante,enLimite)=vehiculo.moverse(tiempoProximoCambioSemaforo,crucero, distanciaCarroAInterseccion)
              if (enLimite) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
              val tiempoPorAplicar = dt-tiempoProximoCambioSemaforo+tiempoRestante
              if (tiempoPorAplicar>0) recorrerEnVehiculo(tiempoPorAplicar, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
	          }
	        }
        }
        //Amarillo y frenar
        else{
          val distanciaAZonaAmarillo = if(distanciaCarroAInterseccion-xSemaforoAmarilloContinuar<0.05)0.05 else distanciaCarroAInterseccion-xSemaforoAmarilloContinuar
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoAZonaAmarillo = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaAZonaAmarillo, desaceleracion), desaceleracion)
          //Se aplica una lógica similar al pare en rojo, pero teniendo en cuenta que no se llega a la Intersección sino a la zona Amarillo Continuar
          if(camaraEnVia){
            val distanciaACamara=calcularDistanciaACamara(distanciaCarroAInterseccion, interseccionSiguiente, viaActual, camara)
            val tiempoACamara=vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaACamara, desaceleracion), desaceleracion)
            if(distanciaACamara>0.0 && distanciaACamara<=distanciaAZonaAmarillo && tiempoACamara<=tiempoProximoCambioSemaforo){
              val tiempoRestante=vehiculo.frenar(dt,tiempoAZonaAmarillo, tiempoACamara, desaceleracion)
              if (tiempoACamara==dt-tiempoRestante) camara.get.comprobar(vehiculo, listaComparendos)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
            }
            else{
              val tiempoRestante=vehiculo.frenar(dt,tiempoAZonaAmarillo, tiempoProximoCambioSemaforo, desaceleracion)
              if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
            }
          }
          else{
            val tiempoRestante=vehiculo.frenar(dt,tiempoAZonaAmarillo, tiempoProximoCambioSemaforo, desaceleracion)
            if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
        }
      }
      //Si estoy afuera de la zona de frenado
      else{
        val distanciaARojo=distanciaCarroAInterseccion- xSemaforoFrenar
        //Si hay una cámara en la vía
        if(camaraEnVia){
          val distanciaACamara=calcularDistanciaACamara(distanciaCarroAInterseccion, interseccionSiguiente, viaActual, camara)
          //Si la cámara está dentro de mis límites
          if(distanciaACamara>0.0 && distanciaACamara<=distanciaARojo){
            val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, distanciaACamara)
            if(enLimite)camara.get.comprobar(vehiculo, listaComparendos)
            if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
          //Si no me tengo que preocupar por la cámara
          else{
            val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, (distanciaCarroAInterseccion- xSemaforoFrenar))
            if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
          }
        }
        //Si no hay cámara en la vía
        else{
          val (tiempoRestante,enLimite)=vehiculo.moverse(dt,crucero, (distanciaCarroAInterseccion- xSemaforoFrenar))
          //El tiempo restante lo aplico en la intersección
          if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar, listaComparendos)
        }
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