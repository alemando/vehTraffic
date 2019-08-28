package com.unalmed.vehTraffic.vehiculo
import com.unalmed.vehTraffic.vehiculo._
class Comparendo(val vehiculo:Vehiculo,val velocidadVehiculo: Double,val maximaVelocidad: Double){
  
  override def toString:String={s"$vehiculo, $velocidadVehiculo, $maximaVelocidad, $porcentajeExcedido"}
  
  def porcentajeExcedido={
    ((velocidadVehiculo-maximaVelocidad)/maximaVelocidad)*100
  }
}