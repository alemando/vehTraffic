package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class Moto(pla : String, pos : Punto, vel : Velocidad, rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}
object Moto{
  
  def apply(placa: String, posicion: Punto, velocidad: Velocidad, recorrido: Recorrido):Moto={
    new Moto(placa, posicion, velocidad, recorrido)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(Placa.placas.contains(placa)){
      placa = (List.fill(3)(r.nextInt(letras.length)):::List.fill(2)(r.nextInt(10)):::List(r.nextInt(letras.length))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}