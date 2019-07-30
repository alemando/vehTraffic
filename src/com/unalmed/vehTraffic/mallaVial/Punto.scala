package com.unalmed.vehTraffic.mallaVial

case class Punto(var x:Double,var y:Double){
  //x y y son longitud y latitud, nos da la posición en el mapa
  //TODO pueden cambiar
  
  def longitudEntrePunto(p: Punto) = {
    Math.sqrt(Math.pow((p.x - this.x),2) + Math.pow((p.y - this.y),2))
  }
}
