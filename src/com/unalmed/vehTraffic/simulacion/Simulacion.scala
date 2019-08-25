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
  val minAceleracion: Int = 5
  val maxAceleracion: Int = 20
  val xSemaforoFrenar: Int = 40
  val xSemaforoAmarilloContinuar: Int = 20
  val minTiempoVerde: Int = 20
  val maxTiempoVerde: Int = 40
  val tiempoAmarillo: Int = 3
  
  generarSemaforos()
  
  val listaNodosSemaforos = ArrayBuffer(listaVias.map(via => via.semaforos).reduce(_++_).groupBy(_.interseccion).map(ArraySemaforos => new NodoSemaforo(ArraySemaforos._2)).toArray:_*)
  
  
  val listaViajes: ArrayBuffer[Viaje] = Viaje.llenarViajes(minVehiculos, maxVehiculos, this)
  
  val listaVehiculos: ArrayBuffer[Vehiculo] = listaViajes.map(_.vehiculo)
  
  var listaComparendos:ArrayBuffer[Comparendo]=ArrayBuffer[Comparendo]()
  //Esa es la lista de comparendos, aun no la he llenado porque no he creado las fotoMultas correspondientes, pero lo dejo aquí para
  //hacer cálculos con el en ResultadosSimulacion ATT espino 
  
  def run() { //Si requiere usar el método imprimir para verificar los resultados, debe descomentar la función en ResultadosSimulaion
    running = true
    while (running) {
      Grafico.graficarVehiculos(listaViajes)
      listaNodosSemaforos.foreach(_.cambiarEstadoSemaforos(this))
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