package com.unalmed.vehTraffic.mallaVial

class Interseccion(val xx : Double,val yy : Double, val nombre : Option[String]) extends Punto(xx,yy){
  
  val color = "#%06x".format(scala.util.Random.nextInt(1<<24))
  
  override def toString ={s"${nombre.getOrElse("Desconocida")}"}
  //TODO name get an setters
  
}

object Interseccion{
  
}