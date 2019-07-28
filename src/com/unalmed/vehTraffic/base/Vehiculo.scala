package com.unalmed.vehTraffic.base

abstract class Vehiculo(val placa:String,var pos:Punto,vel:Velocidad,tipo:TipoVehiculo) extends Movil(pos,vel) with MovimientoUniforme {
  
}
object Vehiculo{
  
}