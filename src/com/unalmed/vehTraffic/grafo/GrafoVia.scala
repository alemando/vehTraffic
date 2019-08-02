package com.unalmed.vehTraffic.grafo

import scalax.collection.mutable.Graph
import scalax.collection.edge.WLDiEdge
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.{Via, Interseccion}
import com.unalmed.vehTraffic.mallaVial.Sentido
import scalax.collection.mutable.Graph.apply$default$3

object GrafoVia {
  
  val grafo = Graph[Interseccion, WLDiEdge]()
  
  def construir(vias: ArrayBuffer[Via]): Unit = {
    vias.foreach(v =>
      if(v.sentido==Sentido.unaVia){grafo.add(WLDiEdge(v.origen,v.fin)(v.longitud, v))}
      else{grafo.add(WLDiEdge(v.origen,v.fin)(v.longitud, v))
        grafo.add(WLDiEdge(v.fin,v.origen)(v.longitud, v))})
  }
  
  def camino(origen: Interseccion, destino: Interseccion): Option[GrafoVia.grafo.Path] = {
    val camino = grafo.get(origen).shortestPathTo(grafo.get(destino))
    camino
  }
  
}