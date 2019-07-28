package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.base.Recorrido

class Bus(pla : String, pos : Punto, vel : Velocidad, rec: Recorrido) extends Vehiculo(pla)(pos, vel, rec){
  
}