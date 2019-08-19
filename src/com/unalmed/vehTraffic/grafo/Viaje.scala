package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.{Via,Interseccion,Punto}
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.dimension._
import scala.collection.mutable.{Queue,ArrayBuffer}

class Viaje private (val origen: Interseccion, val destino: Interseccion, val ruta: Queue[Via], val intersecciones: Queue[Interseccion])(simulacion:Simulacion){
  val vehiculo = Vehiculo(simulacion,this)
  
  def recorrerEnVehiculo(dt : Double) = {
    val error = dt*Velocidad.kilometroAmetro(vehiculo.velocidad.magnitud)
    if(!intersecciones.isEmpty){
    if (vehiculo.posicion == intersecciones.head.asInstanceOf[Punto] && intersecciones.length>=2 && ruta.length>=1) {
        val interseccionActual = intersecciones.dequeue()
        val viaActual = ruta.dequeue()
        vehiculo.velocidad= Velocidad(vehiculo.velocidad.magnitud)({if (viaActual.origen == interseccionActual)viaActual.anguloOrigen
                                                  else viaActual.anguloDestino})}
    val interseccionSiguiente = intersecciones.head
    if(vehiculo.posicion.longitudEntrePunto(interseccionSiguiente) >= error){
      vehiculo.movimientoRectilineoUniforme(dt)
      if(vehiculo.posicion.longitudEntrePunto(interseccionSiguiente) <= 0.5*error) vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)}
    else vehiculo.posicion = Punto(interseccionSiguiente.x, interseccionSiguiente.y)
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
    val camino = GrafoVia.camino(origen, destino)
    val ruta: Queue[Via] = Queue(camino.get.edges.map(_.label.asInstanceOf[Via]).toList: _*)
    val intersecciones: Queue[Interseccion] = Queue(camino.get.nodes.map(_.value.asInstanceOf[Interseccion]).toList: _*)
    new Viaje(origen, destino, ruta, intersecciones)(simulacion)
  }
  
    def llenarViajes(minimo: Int, maximo: Int, simulacion: Simulacion): ArrayBuffer[Viaje]={
    val cantidad = minimo + {scala.util.Random.nextInt(maximo+1 -minimo)}
    val todos = ArrayBuffer.fill(cantidad)(Viaje(simulacion))
    todos
  }
  
  
}