package com.unalmed.vehTraffic.base

trait Recta {
  type T<:Punto
  var origen:T
  var fin:T
}