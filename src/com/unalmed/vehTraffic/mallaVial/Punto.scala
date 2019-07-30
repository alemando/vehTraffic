package com.unalmed.vehTraffic.mallaVial

case class Punto(x:Double,y:Double){
  
  def longitudEntrePunto(p: Punto) = {
    Math.sqrt(Math.pow((p.x - this.x),2) + Math.pow((p.y - this.y),2))
  }
}
