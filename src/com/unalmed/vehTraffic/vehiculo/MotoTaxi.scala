package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.grafo.Viaje

class MotoTaxi private (val pla : String, private var _pos : Punto, private var _vel : Velocidad, private var _ace:Double ,private val velCruc:Double,
    private val tazAce: Double) extends Vehiculo(pla)(_pos, _vel, _ace, velCruc, tazAce){
  private def pos: Punto= _pos
  private def vel: Velocidad=_vel
  private def pos_=(pos: Punto):Unit= _pos=pos
  private def vel_=(vel: Velocidad):Unit= _vel=vel
  
}
object MotoTaxi{
  
  def apply(posicion: Punto, velocidad: Velocidad, aceleracion:Double ,velocidadCrucero: Double, tazaAceleracion: Double):MotoTaxi={
    new MotoTaxi(placa, posicion, velocidad, aceleracion,velocidadCrucero, tazaAceleracion)
  }
  
  def apply(placa:String, posicion: Punto, velocidad: Velocidad, aceleracion:Double ,velocidadCrucero: Double, tazaAceleracion: Double):MotoTaxi={
    new MotoTaxi(placa, posicion, velocidad, aceleracion,velocidadCrucero, tazaAceleracion)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(placa=="" || Placa.placas.contains(placa)){
      placa = (List.fill(3)(r.nextInt(10)):::List.fill(3)(letras(r.nextInt(letras.length)))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}