package com.unalmed.vehTraffic.vehiculo

import com.unalmed.vehTraffic.dimension.{Velocidad, Angulo}
import com.unalmed.vehTraffic.mallaVial.{Punto, Interseccion}
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.grafo.Viaje
import scala.collection.mutable.{Queue, ArrayBuffer}
import com.unalmed.vehTraffic.mallaVial.Via

abstract case class Vehiculo(placa : String)(private var _p : Punto, private var _v: Velocidad, private var _a:Double,private val _vc: Double, private val _ta: Double)
extends Movil(_p,_v,_a,_vc,_ta) with MovimientoAcelerado {
  
  private def p: Punto= _p
  private def v: Velocidad=_v
  private def p_=(p: Punto):Unit= _p=p
  private def v_=(v: Velocidad):Unit= _v=v
  
  //Implementa el movimiento acelerado y de crucero según el movimiento que se esté aplicando actualmente y de acuerdo al límite en dónde se puede
  //mover de esa forma
  def moverse(dt: Double,crucero: Boolean, distanciaALimite:Double):Double={
    //Busco los parámetros de máxima distancia con la función
    val (maximaDistancia, distanciaAceleracion, distanciaCrucero,tiempoAceleracion, tiempoCrucero) = parametrosMaximaDistancia(dt,crucero, velocidadCrucero)
    val tiempoRestante:Double={
      if(maximaDistancia<distanciaALimite){
      if(tiempoAceleracion>0){
        aceleracion= tazaAceleracion
        aplicarAceleracion(tiempoAceleracion)}
      if(tiempoCrucero>0){
        aceleracion=0.0
        aplicarAceleracion(tiempoCrucero)}
      0.0
    }
    else{
      val distanciaLimite:Double ={ if(distanciaALimite<0.05)0.05 else distanciaALimite}
      //Encuentro el tiempo sobrante luego de aplicar 
      val tiempoRestanteInterno:Double ={
        //Aplico aceleración hasta llegar a la intersección
        if(distanciaLimite<distanciaAceleracion){
          aceleracion=tazaAceleracion
          val tiempoAceleracionRecortado = tiempoParaVelocidad(velocidadEnDistancia(distanciaLimite))
          aplicarAceleracion(tiempoAceleracionRecortado)
          dt-tiempoAceleracionRecortado
        }
        else{
         //Aplico aceleración hasta llegar a crucero y luego aplico crucero hasta la intersección
          aceleracion=tazaAceleracion
          aplicarAceleracion(tiempoAceleracion)
          aceleracion=0.0
          val tiempoCruceroRecortado = (distanciaLimite-distanciaAceleracion)/velocidadCrucero
          aplicarAceleracion(tiempoCruceroRecortado)
          dt-tiempoAceleracion-tiempoCruceroRecortado
        }
      }
      tiempoRestanteInterno
    }
    }
    tiempoRestante
  }
  
  //Define los parámetros de máxima distancia de acuerdo al movimiento actual
  def parametrosMaximaDistancia (dt: Double, crucero: Boolean, velocidadCrucero:Double):(Double,Double,Double,Double,Double)={
    val parametros: (Double,Double,Double,Double,Double) = {
      if (crucero)(maximaDistanciaUniforme(dt),0.0,maximaDistanciaUniforme(dt),0.0,dt)
      else{
        val tiempoAcelerando = tiempoParaVelocidad(velocidadCrucero)
        if(dt>= tiempoAcelerando){
          val tiempoCrucero = dt-tiempoAcelerando
          (maximaDistanciaAcelerado(tiempoAcelerando)+maximaDistanciaUniforme(tiempoCrucero),maximaDistanciaAcelerado(tiempoAcelerando),maximaDistanciaUniforme(tiempoCrucero),tiempoAcelerando,tiempoCrucero)
        }
        else (maximaDistanciaAcelerado(dt),maximaDistanciaAcelerado(dt),0.0,dt,0.0)
      }
    }
  parametros
  }
  
  def frenar(dt:Double, tiempoALimite:Double, tiempoSemaforo: Double, desaceleracion: Double):Double={
    val tiempoComparacion = if (tiempoALimite<tiempoSemaforo) tiempoALimite else tiempoSemaforo
    val tiempoSobrante:Double={
    if (dt<tiempoComparacion){
      aceleracion=desaceleracion
      aplicarAceleracion(dt)
      0.0
    }
    else{
      aceleracion=desaceleracion
      aplicarAceleracion(tiempoComparacion)
      dt-tiempoComparacion
    }
    }
    tiempoSobrante
  }
  
}

object Vehiculo{
  
  def apply(minVelocidad:Int, maxVelocidad:Int, minAceleracion: Int, maxAceleracion:Int, proporciones:ArrayBuffer[Double]): Vehiculo={
    val r= scala.util.Random.nextFloat()
    val velocidad = minVelocidad + scala.util.Random.nextInt({maxVelocidad + 1 - minVelocidad})
    val tazaAceleracion = minAceleracion + scala.util.Random.nextInt(maxAceleracion + 1 - minAceleracion)
    val nodo = Punto(0.0,0.0)
    val angulo = Angulo(0.0)
    if (r>=proporciones(0) && r<=proporciones(1))
      return Carro(nodo, Velocidad(0, angulo), 0.0, velocidad, tazaAceleracion)
    else if(r>proporciones(1) && r<=proporciones(2))
      return Moto(nodo, Velocidad(0, angulo), 0.0, velocidad, tazaAceleracion)
    else if(r>proporciones(2) && r<=proporciones(3))
      return Bus(nodo, Velocidad(0, angulo), 0.0, velocidad, tazaAceleracion)
    else if(r>proporciones(3) && r<=proporciones(4))
      return Camion(nodo, Velocidad(0, angulo), 0.0, velocidad, tazaAceleracion)
    else
      return MotoTaxi(nodo, Velocidad(0, angulo), 0.0, velocidad, tazaAceleracion)
  }
  
  def apply(placa : String, posicion : Punto, velocidad: Velocidad, aceleracion : Double, velocidadCrucero: Double, tazaAceleracion: Double, tipo:String): Vehiculo = {
     
    tipo match{
      case "Carro" => Carro(placa, posicion, velocidad, aceleracion, velocidadCrucero, tazaAceleracion)
      case "Camion" => Camion(placa, posicion, velocidad, aceleracion, velocidadCrucero, tazaAceleracion)
      case "Bus" => Bus(placa, posicion, velocidad, aceleracion, velocidadCrucero, tazaAceleracion)
      case "Moto" => Moto(placa, posicion, velocidad, aceleracion, velocidadCrucero, tazaAceleracion)
      case "MotoTaxi" => MotoTaxi(placa, posicion, velocidad, aceleracion, velocidadCrucero, tazaAceleracion)
    }
  }
  
  
  def llenarVehiculos(minVehiculos:Int, maxVehiculos:Int, minVelocidad:Int, maxVelocidad:Int, minAceleracion: Int, maxAceleracion:Int, proporciones:ArrayBuffer[Double]): ArrayBuffer[Vehiculo]={
      val minimo = minVehiculos
      val maximo = maxVehiculos
    val cantidad = minimo + {scala.util.Random.nextInt(maximo+1 -minimo)}
    val vehiculos = ArrayBuffer.fill(cantidad)(Vehiculo(minVelocidad, maxVelocidad, minAceleracion, maxAceleracion, proporciones))
    vehiculos
  }
  
}