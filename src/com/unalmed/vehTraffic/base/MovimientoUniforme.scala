package com.unalmed.vehTraffic.base

trait MovimientoUniforme {
  var velocidad:Velocidad
  var dt:Double=velocidad.magnitud
  
}