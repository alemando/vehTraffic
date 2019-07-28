package com.unalmed.vehTraffic.base

import scalax.collection.mutable.Graph
import scalax.collection.edge.WLDiEdge
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion

object GrafoVia {
  
  val grafo = Graph[Interseccion, WLDiEdge]()
  
  def construir(vias: Array[Via]) = {
    vias.foreach(v =>grafo.add(WLDiEdge(v.origen,v.fin)(v.longitud, v)))
  }
  
  def camino(origen: Interseccion, destino: Interseccion) = {
    val camino = grafo.get(origen).shortestPathTo(grafo.get(destino)).get
    camino
  }
  
}