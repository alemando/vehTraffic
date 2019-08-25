package com.unalmed.vehTraffic.vehiculo
import com.unalmed.vehTraffic.vehiculo._
class Comparendo(vehiculo:Vehiculo, velocidadVehiculo: Double, maximaVelocidad: Double){
  def porcentajeExcedido={
    (velocidadVehiculo/maximaVelocidad)*100
  }
}