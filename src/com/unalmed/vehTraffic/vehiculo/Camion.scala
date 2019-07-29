package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class Camion(pla : String, pos : Punto, vel : Velocidad, rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}
object Camion{
  
  def apply(placa: String, posicion: Punto, velocidad: Velocidad, recorrido: Recorrido):Camion={
    new Camion(placa, posicion, velocidad, recorrido)
  }
  
  def placa: String ={
    val r= scala.util.Random
    val letras = Placa.letras
    var placa: String = ""
    while(Placa.placas.contains(placa)){
      placa = "R"+(List.fill(5)(r.nextInt(10))).mkString("")
    }
    Placa.placas += placa
    placa
  }
}