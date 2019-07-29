package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class MotoTaxi(pla : String, pos : Punto, vel : Velocidad, rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}
object MotoTaxi{
  
  def apply(placa: String, posicion: Punto, velocidad: Velocidad, recorrido: Recorrido):MotoTaxi={
    new MotoTaxi(placa, posicion, velocidad, recorrido)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(Placa.placas.contains(placa)){
      placa = (List.fill(3)(r.nextInt(10)):::List.fill(3)(r.nextInt(letras.length))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}