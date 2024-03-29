package com.unalmed.vehTraffic.mallaVial

import com.unalmed.vehTraffic.dimension.Velocidad

case class Punto(x:Double,y:Double){
  
  def longitudEntrePunto(p: Punto) = {
    Math.sqrt(Math.pow((p.x - x),2) + Math.pow((p.y - y),2))
  }
}

object Punto{
  
  def apply(x:Double,y:Double):Punto={
    val nuevaX = x match{
      case n if(x<=0) => 0
      case n if(x>=30000) => 30000
      case n => x
    }
    val nuevaY = y match{
      case n if(y<=0) => 0
      case n if(y>=30000) => 30000
      case n => y
    }
    new Punto(nuevaX, nuevaY)
  }
  
  def cambioEnPosicion(velocidad: Double, tiempo: Double, direccion: Double) = {
    val nuevoY = Velocidad.kilometroAmetro(velocidad)*tiempo*Math.sin(direccion.toRadians)
    val nuevoX = Velocidad.kilometroAmetro(velocidad)*tiempo*Math.cos(direccion.toRadians)
    (nuevoX, nuevoY)
  }
}