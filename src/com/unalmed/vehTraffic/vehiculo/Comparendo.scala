package com.unalmed.vehTraffic.vehiculo
import com.unalmed.vehTraffic.vehiculo._
class Comparendo(val vehiculo:Vehiculo,val velocidadVehiculo: Double,val maximaVelocidad: Double){
  def porcentajeExcedido={
    (velocidadVehiculo/maximaVelocidad)*100
  }
}