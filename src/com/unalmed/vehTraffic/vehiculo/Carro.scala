package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class Carro(pla : String, pos : Punto, vel : Velocidad, rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}

object Carro{
  
  def apply(placa: String, posicion: Punto, velocidad: Velocidad, recorrido: Recorrido):Carro={
    new Carro(placa, posicion, velocidad, recorrido)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(Placa.placas.contains(placa)){
      placa = (List.fill(3)(r.nextInt(letras.length)):::List.fill(3)(r.nextInt(10))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}