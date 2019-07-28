package dePrueba

class Movil(var posicion:Punto, var velocidad:Velocidad) extends MovimientoUniforme {
  def aumento={
    var value:Double=velocidad.direccion.valor
    posicion.x+=(scala.math.cos(value)*dt)
    posicion.y+=(scala.math.sin(value)*dt)
  }
  def angulo=velocidad.direccion.valor
}