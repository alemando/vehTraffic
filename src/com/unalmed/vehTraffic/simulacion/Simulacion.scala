package com.unalmed.vehTraffic.simulacion

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.{Via, Interseccion,Sentido, TipoVia}
import com.unalmed.vehTraffic.vehiculo.{Vehiculo, Carro}
import com.unalmed.vehTraffic.util.JsonRW
import com.unalmed.vehTraffic.frame.Grafico
import com.unalmed.vehTraffic.grafo.GrafoVia
import com.unalmed.vehTraffic.grafo.Viaje
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.vehiculo.Placa
import com.unalmed.vehTraffic.main.Main
import com.unalmed.vehTraffic.mallaVial.Semaforo
import com.unalmed.vehTraffic.mallaVial.NodoSemaforo
import com.unalmed.vehTraffic.mallaVial.CamaraFotoDeteccion
import com.unalmed.vehTraffic.vehiculo.Comparendo

class Simulacion private(val listaVias: ArrayBuffer[Via],val listaIntersecciones: ArrayBuffer[Interseccion], 
    val listaCamaraFotoDeteccion: ArrayBuffer[CamaraFotoDeteccion], val listaSemaforos: ArrayBuffer[Semaforo],
    val listaVehiculos: ArrayBuffer[Vehiculo], val listaViajes: ArrayBuffer[Viaje], 
    val listaComparendos: ArrayBuffer[Comparendo], private var _t: Int = 0)extends Runnable{
  
  var _hilo: Thread = _
  
  def hilo: Thread= _hilo
  
  def hilo_=(hilo: Thread):Unit = _hilo = hilo
  
  private var _running = false
  
  private def running: Boolean = _running
  private def running_=(valor: Boolean): Unit = _running = valor
  
  def t: Int = _t
  
  def t_=(t: Int):Unit= _t = t

  //Si requiere usar el método imprimir para verificar los resultados, debe descomentar la función en ResultadosSimulaion
  def run() { 
    running = true
    while (running) {
      Grafico.graficarVehiculos(listaViajes)
      listaViajes.foreach(_.recorrerEnVehiculo(Simulacion.dt,Simulacion.dt,t,Simulacion.xSemaforoFrenar, Simulacion.xSemaforoAmarilloContinuar, listaComparendos))
      t = t + Simulacion.dt
      Thread.sleep(Simulacion.tRefresh)
      if (listaViajes.filter(x => x.destino == x.vehiculo.posicion).length == listaVehiculos.length){
        running = false
        new ResultadosSimulacion(this)//.imprimir()
        Grafico.graficarVehiculos(listaViajes)
        println("Se acabó")
      }
    }
  }
  
  def start() = {
    hilo = new Thread(this)
    Grafico.iniciarVehiculos(listaViajes)
    hilo.start()
  }
  
  def stop() = {
    running = false
  }
  
  def borrar(){
    Grafico.removerVehiculos(listaVehiculos)
    Placa.placas.clear
    t = 0
  }
  
}

object Simulacion{
  
  def apply(listaVias: ArrayBuffer[Via], listaIntersecciones: ArrayBuffer[Interseccion], 
     listaCamaraFotoDeteccion: ArrayBuffer[CamaraFotoDeteccion]) = {
    
    val proporciones=Array(0.0,proporciónCarros, proporciónMotos, proporciónBuses, proporciónCamiones, proporciónMotoTaxis)
    
    val proporcionesSumadas:ArrayBuffer[Double]={
      var sum = 0.0
      val prop: ArrayBuffer[Double]= ArrayBuffer()
      proporciones.foreach(x => {sum += x
                                  prop+=sum})
      prop
    }
    
    val listaSemaforos: ArrayBuffer[Semaforo] = Semaforo.llenarSemaforos(listaVias, minTiempoVerde, maxTiempoVerde, tiempoAmarillo)
  
    NodoSemaforo.crearNodos(listaSemaforos, listaIntersecciones)
    
    val listaVehiculos: ArrayBuffer[Vehiculo] = Vehiculo.llenarVehiculos(minVehiculos, maxVehiculos, minVelocidad, maxVelocidad, minAceleracion, maxAceleracion, proporcionesSumadas)
  
    val listaViajes: ArrayBuffer[Viaje] = Viaje.llenarViajes(listaVehiculos, listaIntersecciones)
  
    val listaComparendos:ArrayBuffer[Comparendo]=ArrayBuffer[Comparendo]()
    
    new Simulacion(listaVias, listaIntersecciones, listaCamaraFotoDeteccion, listaSemaforos, listaVehiculos, listaViajes, listaComparendos)
    
  }
  
  def apply(listaVias: ArrayBuffer[Via], listaIntersecciones: ArrayBuffer[Interseccion], 
     listaCamaraFotoDeteccion: ArrayBuffer[CamaraFotoDeteccion], listaSemaforos: ArrayBuffer[Semaforo], 
     listaVehiculos: ArrayBuffer[Vehiculo], listaViajes: ArrayBuffer[Viaje], listaComparendos:ArrayBuffer[Comparendo], tiempoSimulacion: Int ) = {
  
    NodoSemaforo.crearNodos(listaSemaforos, listaIntersecciones)
    
    new Simulacion(listaVias, listaIntersecciones, listaCamaraFotoDeteccion, listaSemaforos, listaVehiculos, listaViajes, listaComparendos, tiempoSimulacion)
  }
  
  //Leer archivo json (crea objeto con todos los valores en una variable (config) de la clase JsonRW)
  val config = JsonRW.readConfig()
  
  //Parametros del Json
  val dt: Int = config.parametrosSimulacion.dt.toInt
  val tRefresh: Int = (config.parametrosSimulacion.tRefresh*1000).toInt
  val minVehiculos: Int = config.parametrosSimulacion.vehiculos.minimo 
  val maxVehiculos: Int = config.parametrosSimulacion.vehiculos.maximo 
  val minVelocidad: Int = config.parametrosSimulacion.velocidad.minimo  
  val maxVelocidad: Int = config.parametrosSimulacion.velocidad.maximo  
  val proporciónCarros: Double = config.parametrosSimulacion.proporciones.carros 
  val proporciónMotos: Double = config.parametrosSimulacion.proporciones.motos
  val proporciónBuses: Double = config.parametrosSimulacion.proporciones.buses
  val proporciónCamiones: Double = config.parametrosSimulacion.proporciones.camiones  
  val proporciónMotoTaxis: Double = config.parametrosSimulacion.proporciones.motoTaxis
  val minAceleracion: Int = config.parametrosSimulacion.aceleracion.minimo
  val maxAceleracion: Int = config.parametrosSimulacion.aceleracion.maximo
  val xSemaforoFrenar: Int = config.parametrosSimulacion.distanciasFrenadoVehiculos.xSemaforoFrenar
  val xSemaforoAmarilloContinuar: Int = config.parametrosSimulacion.distanciasFrenadoVehiculos.xSemaforoAmarilloContinuar
  val minTiempoVerde: Int = config.parametrosSimulacion.semaforos.minTiempoVerde
  val maxTiempoVerde: Int = config.parametrosSimulacion.semaforos.maxTiempoVerde
  val tiempoAmarillo: Int = config.parametrosSimulacion.semaforos.tiempoAmarillo
  
}