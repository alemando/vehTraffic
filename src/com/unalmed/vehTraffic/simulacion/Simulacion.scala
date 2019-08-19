package com.unalmed.vehTraffic.simulacion

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.{Via, Interseccion,Sentido, TipoVia}
import com.unalmed.vehTraffic.vehiculo.{Vehiculo, Carro}
import com.unalmed.vehTraffic.util.JsonRW
import com.unalmed.vehTraffic.frame.Grafico
import com.unalmed.vehTraffic.grafo.GrafoVia
import com.unalmed.vehTraffic.grafo.Recorrido
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.vehiculo.Placa
import com.unalmed.vehTraffic.main.Main

class Simulacion(val listaVias: ArrayBuffer[Via], val listaIntersecciones: ArrayBuffer[Interseccion])extends Runnable{
  
  var _hilo: Thread = _
  
  def hilo: Thread= _hilo
  
  def hilo_=(hilo: Thread):Unit = _hilo = hilo
  
  private var _running = false
  
  private def running: Boolean = _running
  private def running_=(valor: Boolean): Unit = _running = valor

  private var _t: Double = 0
  
  def t: Double = _t
  
  private def t_=(t: Double):Unit= _t=t
  
  GrafoVia.construir(listaVias)
  
  Grafico.graficarVias(listaVias)
  
  var _listaVehiculos: ArrayBuffer[Vehiculo] = ArrayBuffer()
  
  def listaVehiculos: ArrayBuffer[Vehiculo] = _listaVehiculos
  
  def listaVehiculos_=(nuevaLista: ArrayBuffer[Vehiculo]): Unit = _listaVehiculos = nuevaLista
  
  //Leer archivo json (crea objeto con todos los valores en una variable (config) de la clase JsonRW)
  val config = JsonRW.readConfig()
  
  val dt: Double = config.parametrosSimulacion.dt
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
  
  def run() { //Si requiere usar el método imprimir para verificar los resultados, debe descomentar la función en ResultadosSimulaion
    running = true
    while (running) {
      Grafico.graficarVehiculos(listaVehiculos)
      listaVehiculos.foreach(_.cambioPosicion(dt))
      t = t + dt
      Thread.sleep(tRefresh)
      if (listaVehiculos.filter(x => x.recorrido.destino == x.posicion).length == listaVehiculos.length){
        running = false
        new ResultadosSimulacion()//.imprimir()
        Grafico.graficarVehiculos(listaVehiculos)
      }
    }
  }
  
  def start() = {
    hilo = new Thread(this)
    listaVehiculos = Vehiculo.llenarVehiculos(minVehiculos, maxVehiculos)
    Grafico.iniciarVehiculos(listaVehiculos)
    hilo.start()
  }
  
  def stop() = {
    running = false
  }
  
  def borrar(){
    Grafico.removerVehiculos(listaVehiculos)
    listaVehiculos.clear()
    Placa.placas.clear
    t = 0
  }
  
}