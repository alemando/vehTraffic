package com.unalmed.vehTraffic.mallaVial

import com.unalmed.vehTraffic.dimension.Angulo

trait Recta {
  type T<:Punto
  val origen:T
  val fin:T
    
  def anguloDosPuntos(p1:Punto, p2:Punto) = {
    val y = p2.y - p1.y
    val x = p2.x - p1.x
    var mag = Math.atan(Math.abs(y)/Math.abs(x))
    if(x<=0 && y<=0) mag = Math.PI + mag else if(x<=0 && y>=0) mag = Math.PI -mag else if (x>=0 && y<=0) mag =(2*Math.PI)-mag
    Angulo(mag.toDegrees)
  }
}