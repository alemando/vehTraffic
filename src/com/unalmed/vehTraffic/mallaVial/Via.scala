package com.unalmed.vehTraffic.mallaVial

class Via private(val or : Interseccion, val fn : Interseccion, val velocidadMaxima : Double, 
    val tipo : TipoVia, val sentido : Sentido, val numero : String, val nombre : Option[String]) extends Recta {
  
  type T = Interseccion
  val origen = or
  val fin = fn
  
  def longitud = {
    Math.sqrt(Math.pow((fin.x - origen.x),2) + Math.pow((fin.y - origen.y),2))
  }
  
  override def toString = {s"${tipo.nombre}: $nombre"}
}

object Via{
  
  def apply(origen:Interseccion, fin: Interseccion, velocidadMaxima: Double, tipo: TipoVia, sentido: Sentido, numero: String, nombre: Option[String]): Via={
    val velocidad = velocidadMaxima match{
      case n if(velocidadMaxima<0) => 0.0
      case n if(velocidadMaxima>120) => 120.0
      case n => velocidadMaxima
    }
    new Via(origen, fin, velocidad, tipo, sentido, numero, nombre)
  }
  
}