package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.{Via,Interseccion,Punto}
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.dimension._
import scala.collection.mutable.{Queue,ArrayBuffer}

class Viaje private (val vehiculo: Vehiculo, val origen: Interseccion, val destino: Interseccion, val camino: GrafoVia.grafo.Path ,
    val ruta: Queue[Via], val intersecciones: Queue[Interseccion]){
  
	def recorrerEnVehiculo(dt : Double, dtSimulacion: Double, tSimulacion: Double, xSemaforoFrenar: Int, xSemaforoAmarilloContinuar:Int):Unit = {
    
    def definirAngulo(interseccion: Interseccion, via: Via):Angulo= if (via.origen == interseccion) via.anguloOrigen else via.anguloDestino

    if(!intersecciones.isEmpty){
      
      val interseccionSiguiente = intersecciones.head
      val viaActual = ruta.head
//      println("\n")
//      println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
//      if (vehiculo.posicion.x.isNaN()){
//      println(camino)
//      println(interseccionSiguiente)
//      println(viaActual)
//      println(s"dt: $dt")
      val distanciaCarroAInterseccion = vehiculo.posicion.longitudEntrePunto(interseccionSiguiente)
      val crucero = if(Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud) >= vehiculo.velocidadCrucero) true else false
      if(interseccionSiguiente==origen){
//        println("Origen")
          val interseccionActual = intersecciones.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, viaActual))
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
      }
      else{
      val semaforo = interseccionSiguiente.nodoSemaforo.semaforoEnVia(viaActual)
      val (estadoSemaforo, tiempoProximoCambioSemaforo) = interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, semaforo, dtSimulacion, tSimulacion)
//      println("\n")
//      println(s"Movimiento hacia $interseccionSiguiente por $viaActual\n")
//      println(s"dt: $dt")
//      interseccionSiguiente.nodoSemaforo.semaforos.foreach(sem=>{
//        if(sem==semaforo)println(s"Este semáforo: ${interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, sem, simulacion)}")
//        else println(s"Otro semáforo: ${interseccionSiguiente.nodoSemaforo.estadoDeSemaforo(dt, sem, simulacion)}")})
//      
        if(dt<=0){
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
            recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        //Termina recorrido
          else if(interseccionSiguiente==destino){
//          println("Fin")
            val interseccionActual = intersecciones.dequeue()
            val viaActual = ruta.dequeue()
          }
        //Cambia de calle
          else if(estadoSemaforo=="Verde" || estadoSemaforo=="Amarillo" || (estadoSemaforo=="Rojo" && tiempoProximoCambioSemaforo==0.0)){
//          println("Cambiar calle")
          val interseccionActual = intersecciones.dequeue()
          val viaPasada = ruta.dequeue()
          vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)(definirAngulo(interseccionActual, ruta.head))
          recorrerEnVehiculo(dt, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
          else{
//          println("Esperando rojo")
            if(dt>tiempoProximoCambioSemaforo){
            recorrerEnVehiculo(dt-tiempoProximoCambioSemaforo, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        }
      }
      //Si estoy en la zona roja
      else if(distanciaCarroAInterseccion<= xSemaforoFrenar){
        if(estadoSemaforo=="Rojo"){
//          println("Zona roja, rojo")
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoComparacion = if (tiempoFrenado<tiempoProximoCambioSemaforo)tiempoFrenado else tiempoProximoCambioSemaforo
          //Frenando sin llegar a la interseccion
          if (dt<tiempoComparacion){
//            println("Voy frenando")
//            println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
            vehiculo.aplicarAceleracion(dt, desaceleracion)
//            println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
          }
          //Freno en la intersección e implemento tiempo sobrante
          else{
//            println("Freno del todo y queda tiempo")
//            println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
            vehiculo.aplicarAceleracion(tiempoProximoCambioSemaforo,desaceleracion)
//            println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
            if(tiempoFrenado==tiempoComparacion) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            if(dt>tiempoComparacion)recorrerEnVehiculo(dt-tiempoComparacion,dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        }
        //Si estoy en la zona roja pero estoy en verde o en amarillo y en la zona amarillo
        else if(estadoSemaforo=="Verde" || (estadoSemaforo=="Amarillo" && distanciaCarroAInterseccion <= xSemaforoAmarilloContinuar)){
//          println("Zona roja pero semáforo no rojo")
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
//          println("Máxima distancia<AIntersección")
          //Aplico aceleración solo si hay tiempo de aceleracion
          if(tiempoAceleracion!=0)vehiculo.aplicarAceleracion(tiempoAceleracion)
          //Aplico crucero el resto del tiempo
          vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCrucero)
        }
        //Si la máxima distancia sobrepasa a la zona de intersección
          else /*if(maximaDistancia>=distanciaCarroAInterseccion-simulacion.xSemaforoFrenar)*/{
//            println("Sobrepaso la intersección")
            val distanciaInterseccion:Double ={ if(distanciaCarroAInterseccion<0.05)0.05 else distanciaCarroAInterseccion}
            //Encuentro el tiempo sobrante luego de aplicar 
            val tiempoRestante:Double ={
              //Aplico aceleración hasta llegar a la intersección
              if(distanciaInterseccion<distanciaAceleracion){
//                println("Distancia Interseccion<Distancia Aceleración")
                val tiempoAceleracionRecortado = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaInterseccion))
                vehiculo.aplicarAceleracion(tiempoAceleracionRecortado)
                dt-tiempoAceleracionRecortado
              }
              else{
//                println("Llego a crucero y aplico")
                //Aplico aceleración hasta llegar a crucero y luego aplico crucero hasta la intersección
                vehiculo.aplicarAceleracion(tiempoAceleracion)
                val tiempoCruceroRecortado = (distanciaInterseccion-distanciaAceleracion)/vehiculo.velocidadCrucero
                vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCruceroRecortado)
                dt-tiempoAceleracion-tiempoCruceroRecortado
              }
            }
            //El tiempo restante lo aplico en la intersección
            vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            if (tiempoRestante>0) recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        }
        //Amarillo y frenar
        else{
//          println("Amarillo y freno")
          val distanciaAZonaAmarillo = if(distanciaCarroAInterseccion-xSemaforoAmarilloContinuar<0.05)0.05 else distanciaCarroAInterseccion-xSemaforoAmarilloContinuar
          val(desaceleracion,tiempoFrenado)= vehiculo.parametrosFrenadoEnDistancia(distanciaCarroAInterseccion)
          val tiempoAZonaAmarillo = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaAZonaAmarillo, desaceleracion), desaceleracion)
          val tiempoComparacion = if (tiempoAZonaAmarillo<tiempoProximoCambioSemaforo)tiempoAZonaAmarillo else tiempoProximoCambioSemaforo
          //Frenando sin llegar a la zona amarilla
          if (dt<tiempoComparacion){
//            println("No llego a amarillo")
            vehiculo.aplicarAceleracion(dt, desaceleracion)
          }
          //Freno hasta zona amarilla e implemento tiempo sobrante
          else{
//            println("Si llego a amarillo")
            vehiculo.aplicarAceleracion(tiempoAZonaAmarillo,desaceleracion)
//            if(tiempoFrenado==tiempoComparacion) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
            if (dt>tiempoComparacion) recorrerEnVehiculo(dt-tiempoComparacion,dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
          }
        }
      }
      //Si estoy afuera de la zona de frenado
      else /*if(distanciaCarroAInterseccion> simulacion.xSemaforoFrenar)*/{
        //Calculo la máxima distancia
//        println("Fuera de la zona de frenado")
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
        if (maximaDistancia<distanciaCarroAInterseccion-xSemaforoFrenar){
//          println("No llego a zona roja")
          //Aplico aceleración solo si hay tiempo de aceleracion
          if(tiempoAceleracion!=0)vehiculo.aplicarAceleracion(tiempoAceleracion)
          //Aplico crucero el resto del tiempo
          vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCrucero)
        }
        //Si la máxima distancia sobrepasa a la zona de frenado
        else /*if(maximaDistancia>=distanciaCarroAInterseccion-simulacion.xSemaforoFrenar)*/{
//          println("Llego a zona roja")
          val distanciaRojo:Double ={ if(distanciaCarroAInterseccion-xSemaforoFrenar<0.05)0.05 else distanciaCarroAInterseccion-xSemaforoFrenar}
          //Encuentro el tiempo sobrante luego de aplicar 
          val tiempoRestante:Double ={
            //Aplico aceleración hasta llegar al límite de zona de frenado
            if(distanciaRojo<distanciaAceleracion){
//              println("Acelero hasta zona de frenado")
              val tiempoAceleracionRecortado = vehiculo.tiempoParaVelocidad(vehiculo.velocidadEnDistancia(distanciaRojo))
              vehiculo.aplicarAceleracion(tiempoAceleracionRecortado)
              dt-tiempoAceleracionRecortado
            }
            else{
//              println("Crucero hasta zona de frenado")
//              println(s"Tiempo aceleración: $tiempoAceleracion")
//              println(s"Distancia a intersección: $distanciaCarroAInterseccion")
              //Aplico aceleración hasta llegar a crucero y luego aplico crucero hasta el límite de zona roja
              if (tiempoAceleracion>0) vehiculo.aplicarAceleracion(tiempoAceleracion)
              val tiempoCruceroRecortado = (distanciaRojo-distanciaAceleracion)/vehiculo.velocidadCrucero
//              println(s"Tiempo crucero recortado: $tiempoCruceroRecortado")
              if (tiempoCruceroRecortado>0)vehiculo.aplicarMovimientoRectilineoUniforme(tiempoCruceroRecortado)
//              println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
//              println(s"Posición intersección, x: ${interseccionSiguiente.x}, y: ${interseccionSiguiente.y}")
              dt-tiempoAceleracion-tiempoCruceroRecortado
              }
            }
          //El tiempo restante lo aplico en la zona de frenado
//          println(s"Tiempo restante: $tiempoRestante")
          if(tiempoRestante>0)recorrerEnVehiculo(tiempoRestante, dtSimulacion, tSimulacion, xSemaforoFrenar, xSemaforoAmarilloContinuar)
            }
        }
      }
//      if(vehiculo.posicion.x.isNaN){
//      println(s"Posición vehiculo, x: ${vehiculo.posicion.x}, y: ${vehiculo.posicion.y}")
//      println(interseccionSiguiente)
//      println(viaActual)
//      println(s"dt: $dt")
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