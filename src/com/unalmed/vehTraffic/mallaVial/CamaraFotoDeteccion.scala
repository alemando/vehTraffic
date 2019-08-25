package com.unalmed.vehTraffic.mallaVial
import com.unalmed.vehTraffic.vehiculo._
class CamaraFotoDeteccion(val ubicacion:Double,via:Via,vehiculo:Vehiculo){
  var angle=via.anguloOrigen
  var newx=ubicacion*math.cos(angle.valor)
  var newy=ubicacion*math.sin(angle.valor)
  def posicion=new Punto(newx,newy)
  
  def comprobar={
    if((via.velocidadMaxima-vehiculo.magnitudPublica)<0){
      new Comparendo(vehiculo,via.velocidadMaxima)
    }
  }
}
