package com.unalmed.vehTraffic.mallaVial

class Via(val or : Interseccion, val fn : Interseccion, val velocidadMaxima : Double, 
    val tipo : TipoVia, val sentido : Sentido, val numero : String, val nombre : String) extends Recta {
  
  type T = Interseccion
  val origen = or
  val fin = fn
  
  def longitud = {
    Math.sqrt(Math.pow((fin.x - origen.x),2) + Math.pow((fin.y - origen.y),2))
  }
  
  override def toString = {s"${tipo.nombre}: $nombre"}
}