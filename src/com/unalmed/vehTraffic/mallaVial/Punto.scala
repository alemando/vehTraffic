package com.unalmed.vehTraffic.mallaVial

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
  
}