package com.unalmed.vehTraffic.base

import scalax.collection.mutable.Graph
import scalax.collection.edge.WLDiEdge
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion

object GrafoVia {
  
  val g = Graph[Interseccion, WLDiEdge]()
  
  def construir(vias: ArrayBuffer[Via]) = {
    vias.foreach(v =>g.add(WLDiEdge(v.origen,v.fin)(v.longitud, v)))
  }
  
}