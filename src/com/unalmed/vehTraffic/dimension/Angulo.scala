package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto

case class Angulo(valor:Double)

object Angulo{
  
  def apply(valor:Double):Angulo={
    var grados = valor
    while (grados<0 || grados>360){
      if(grados<0) grados+=360
      else if (grados>360) grados-=360
    }
    new Angulo(grados)
  }
  
  def anguloDosPuntos(p1:Punto, p2:Punto) = {
    val y = p2.y - p1.y
    val x = p2.x - p1.x
    var mag = Math.atan(Math.abs(y)/Math.abs(x))
    if(x<=0 && y<=0) mag = Math.PI + mag else if(x<=0 && y>=0) mag = Math.PI -mag else if (x>=0 && y<=0) mag =(2*Math.PI)-mag
    new Angulo(mag.toDegrees)
  }
}