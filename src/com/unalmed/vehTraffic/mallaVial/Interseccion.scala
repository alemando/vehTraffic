package com.unalmed.vehTraffic.mallaVial

class Interseccion(val longitud : Double,val latitud : Double, val nombre : Option[String]) extends Punto(longitud,latitud){
  
  val color = "#%06x".format(scala.util.Random.nextInt(1<<24))
  
  override def toString ={s"${nombre.getOrElse("Desconocida")}"}
  //TODO name get an setters
  
}

object Interseccion{
  
  def apply(x:Double, y:Double, nombre: Option[String]):Interseccion={
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
    new Interseccion(nuevaX, nuevaY, nombre)
  }
  
}