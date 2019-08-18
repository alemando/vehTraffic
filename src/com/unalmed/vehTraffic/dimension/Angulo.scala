package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto

case class Angulo private(valor:Double)

object Angulo{
  
  def apply(valor:Double):Angulo={
    var grados = valor
    while (grados<0 || grados>360){
      if(grados<0) grados+=360
      else if (grados>360) grados-=360
    }
    new Angulo(grados)
  }
}