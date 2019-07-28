package com.unalmed.vehTraffic.mallaVial

class Interseccion(xx : Double, yy : Double, private val _name : Option[String]) extends Punto(xx,yy){
  
  val color = "#%06x".format(scala.util.Random.nextInt(1<<24))
  
  //TODO name get an setters
  
}