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
import com.unalmed.vehTraffic.mallaVial.NodoSemaforo

class Simulacion(val listaVias: ArrayBuffer[Via],val listaIntersecciones: ArrayBuffer[Interseccion])extends Runnable{
  
  var _hilo: Thread = _
  
  def hilo: Thread= _hilo
  
  def hilo_=(hilo: Thread):Unit = _hilo = hilo
  
  private var _running = false
  
  private def running: Boolean = _running
  private def running_=(valor: Boolean): Unit = _running = valor

  private var _t: Int = 0
  
  def t: Int = _t
  
  private def t_=(t: Int):Unit= _t=t
  GrafoVia.construir(listaVias)
  
  Grafico.graficarVias(listaVias)  
  
  //Leer archivo json (crea objeto con todos los valores en una variable (config) de la clase JsonRW)
  val config = JsonRW.readConfig()
  
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
  
  this.generarSemaforos()
  
  
  this.agruparSemaforosEnNodos()
  
  //Temporal para revisar estado semaforos
  val nodoSemaforo = listaIntersecciones(0).nodoSemaforo
  
  val listaViajes: ArrayBuffer[Viaje] = Viaje.llenarViajes(minVehiculos, maxVehiculos, this)
  
  val listaVehiculos: ArrayBuffer[Vehiculo] = listaViajes.map(_.vehiculo)
  
  //Si requiere usar el método imprimir para verificar los resultados, debe descomentar la función en ResultadosSimulaion
  def run() {
    running = true
    while (running) {
      Grafico.graficarVehiculos(listaViajes)
      
      //Interseccion(0) es niquia solo tiene dos semaforos
      //Temporal para ver el cambio de los estados
      val semaforo1 = nodoSemaforo.semaforos(0)
      val semaforo2 = nodoSemaforo.semaforos(1)
      println(nodoSemaforo.estadoDeSemaforo(0, semaforo1, this))
      println(nodoSemaforo.estadoDeSemaforo(0, semaforo2, this))
      
      listaViajes.foreach(_.recorrerEnVehiculo(dt))
      t = t + dt
      Thread.sleep(tRefresh)
//      if (listaVehiculos.filter(x => x.viaje.destino == x.posicion).length == listaVehiculos.length){
      if (listaViajes.filter(x => x.destino == x.vehiculo.posicion).length == listaVehiculos.length){
        running = false
        new ResultadosSimulacion(this)//.imprimir()
        Grafico.graficarVehiculos(listaViajes)
        println("Se acabó")
      }
    }
  }
  
  def generarSemaforos() = {
    listaVias.foreach(via=>{
      if(via.sentido.nombre == "unaVia"){ 
        via.semaforos = Option(ArrayBuffer(Semaforo(this, via.origen))) 
      }else{
        via.semaforos = Option(ArrayBuffer(Semaforo(this, via.origen),Semaforo(this, via.fin))) 
      }
      
    })
  }
  
  def agruparSemaforosEnNodos() = {
    val arr = new ArrayBuffer[NodoSemaforo]()
    val semaforos = ArrayBuffer(listaVias.map(via => via.semaforos).reduce(_++_)).flatten
    listaIntersecciones.foreach(interseccion=>{
      val nodoSemaforo = new NodoSemaforo(interseccion, semaforos.filter(_.interseccion == interseccion))
      arr += nodoSemaforo
      interseccion.nodoSemaforo =  Option(nodoSemaforo)
    })
    arr
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