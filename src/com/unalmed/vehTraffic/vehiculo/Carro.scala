package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class Carro(val pla : String, var pos : Punto, var vel : Velocidad, val rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}

object Carro{
  
  def apply(posicion: Punto, velocidad: Velocidad, recorrido: Recorrido):Carro={
    new Carro(placa, posicion, velocidad, recorrido)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(placa=="" || Placa.placas.contains(placa)){
      placa = (List.fill(3)(letras(r.nextInt(letras.length))):::List.fill(3)(r.nextInt(10))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}