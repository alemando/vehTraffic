package com.unalmed.vehTraffic.vehiculo
import com.unalmed.vehTraffic.vehiculo._
class Comparendo(vehiculo:Vehiculo,maximaVelocidad:Double){
  def vehiculoMultado=vehiculo //Devuelve al vehiculo multado
  def velocidadVehiculo=vel
  var vel=vehiculo.magnitudPublica
  def porcentajeExcedido={
    (vel/maximaVelocidad)*100
  }
  //Máxima velocidad es la velocidad de la vía
  //vel es la velocidad el vehículo
}