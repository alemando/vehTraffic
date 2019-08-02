package com.unalmed.vehTraffic.grafo

import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.grafo.GrafoVia

class Recorrido private (val origen: Interseccion, val destino: Interseccion, val camino: Option[GrafoVia.grafo.Path]){
}

object Recorrido{
  def apply(): Recorrido={
    val random1=scala.util.Random.nextInt(Simulacion.listaIntersecciones.length)
    var random2=scala.util.Random.nextInt(Simulacion.listaIntersecciones.length)
    while(random1 == random2) random2=scala.util.Random.nextInt(Simulacion.listaIntersecciones.length)
    val origen = Simulacion.listaIntersecciones(random1)
    val destino = Simulacion.listaIntersecciones(random2)
    val camino = GrafoVia.camino(origen, destino)
    new Recorrido(origen, destino, camino)
  }
}