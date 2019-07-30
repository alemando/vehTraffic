package com.unalmed.vehTraffic.dimension

import com.unalmed.vehTraffic.mallaVial.Punto

case class Angulo(valor:Double) {
//TODO get setters
}
object Angulo{
  def anguloDosPuntos(p1:Punto, p2:Punto) = {
    new Angulo(Math.atan((p2.y - p1.y)/(p2.x - p1.x)))
  }
}