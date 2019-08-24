package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad

trait MovimientoAcelerado extends MovimientoUniforme{
  
  
  def aplicarAceleracion (t: Double):Unit = {
    val theta = velocidad.direccion.valor.toRadians
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dy = v0*t*Math.sin(theta)+(aceleracion*Math.sin(theta)*t*t)/2.0
    val dx = v0*t*Math.cos(theta)+(aceleracion*Math.cos(theta)*t*t)/2.0
    aumentarVelocidad(t)
    posicion=Punto(posicion.x+dx,posicion.y+dy)
  }
  
  def aplicarDesaceleracion (t: Double, a: Double):Unit = {
    val theta = velocidad.direccion.valor.toRadians
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dy = v0*t*Math.sin(theta)+(a*Math.sin(theta)*t*t)/2.0
    val dx = v0*t*Math.cos(theta)+(a*Math.cos(theta)*t*t)/2.0
    disminuirVelocidad(t,a)
    posicion=Punto(posicion.x+dx,posicion.y+dy)
  }
  
  def disminuirVelocidad(t:Double, a:Double):Unit = {
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dv = Velocidad.metroAkilometro(v0 + a*t)
    velocidad = Velocidad(if (dv>=0) dv else 0)(velocidad.direccion)
  }
  
  def aumentarVelocidad(t: Double):Unit = {
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dv = Velocidad.metroAkilometro(v0 + aceleracion*t)
    velocidad = Velocidad(if (dv>=0) dv else 0)(velocidad.direccion)
  }
  
  def tiempoMaximaVelocidad(vel:Double):Double={
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dt = (vel-v0)/aceleracion
    dt
  }
  
  def velocidadEnDistancia(d:Double):Double={
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val v = Math.sqrt(Math.pow(v0, 2)+2*aceleracion*d)
    v
  }
    
  def maximaDistanciaAcelerado(t: Double):Double ={
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val dMax= v0*t+(aceleracion*t*t)/2.0
    dMax
  }
   
  def parametrosFrenadoEnDistancia(d:Double):(Double,Double)={
    val v0 = Velocidad.kilometroAmetro(velocidad.magnitud)
    val t=(2*d)/v0
    val a=(-v0)/t
    (a,t)
  }

}