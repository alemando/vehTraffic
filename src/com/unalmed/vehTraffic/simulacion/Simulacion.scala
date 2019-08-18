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

class Simulacion extends Runnable{
  
  var _hilo: Thread = _
  
  def hilo: Thread= _hilo
  
  def hilo_=(hilo: Thread):Unit = _hilo = hilo
  
  private var _running = false
  
  private def running: Boolean = _running
  private def running_=(valor: Boolean): Unit = _running = valor

  private var _t: Double = 0
  
  def t: Double = _t
  
  private def t_=(t: Double):Unit= _t=t
  
  val listaVias = cargarVias()
  
  val listaIntersecciones: ArrayBuffer[Interseccion] = (listaVias.map(_.origen) ++ listaVias.map(_.fin)).distinct
  
  GrafoVia.construir(listaVias)
   
  Grafico.iniciarGrafico(listaVias, listaIntersecciones)
  
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
    while(Thread.activeCount()>2){
      Thread.sleep(100)
    }
    hilo = new Thread(this)
    Grafico.removerVehiculos(listaVehiculos)
    listaVehiculos.clear()
    Placa.placas.clear
    t = 0
    listaVehiculos = Vehiculo.llenarVehiculos(minVehiculos, maxVehiculos)
    Grafico.iniciarVehiculos(listaVehiculos)
    hilo.start()
  }
  
  def stop() = {
    running = false
  }
  
  def cargarVias():ArrayBuffer[Via] = {
    val niquia = Interseccion(300, 12000, Some("Niquia"))
  val lauraAuto = Interseccion(2400, 11400, Some("M. Laura Auto"))
  val lauraReg = Interseccion(2400, 12600, Some("M. Laura Reg"))
  val ptoCero = Interseccion(5400, 12000, Some("Pto 0"))
  val mino = Interseccion(6300, 15000, Some("Minorista"))
  val villa = Interseccion(6300, 19500, Some("Villanueva"))
  val ig65 = Interseccion(5400, 10500, Some("65 Igu"))
  val robledo = Interseccion(5400, 1500, Some("Exito Rob"))
  val colReg = Interseccion(8250, 12000, Some("Col Reg"))
  val col65 = Interseccion(8250, 10500, Some("Col 65"))
  val col80 = Interseccion(8250, 1500, Some("Col 80"))
  val juanOr = Interseccion(10500, 19500, Some("Sn Juan Ori"))
  val maca =  Interseccion(10500, 12000, Some("Macarena"))
  val expo =  Interseccion(12000, 13500, Some("Exposiciones"))
  val reg30 =  Interseccion(13500, 15000, Some("Reg 30"))
  val monte =  Interseccion(16500, 15000, Some("Monterrey"))
  val agua =  Interseccion(19500, 15000, Some("Aguacatala"))
  val viva =  Interseccion(21000, 15000, Some("Viva Env"))
  val mayor =  Interseccion(23400, 15000, Some("Mayorca"))
  val ferrCol =  Interseccion(8250, 15000, Some("Ferr Col"))
  val ferrJuan =  Interseccion(10500, 15000, Some("Alpujarra"))
  val sanDiego =  Interseccion(12000, 19500, Some("San Diego"))
  val premium =  Interseccion(13500, 19500, Some("Premium"))
  val pp =  Interseccion(16500, 19500, Some("Parque Pob"))
  val santafe =  Interseccion(19500, 18750, Some("Santa Fe"))
  val pqEnv =  Interseccion(21000, 18000, Some("Envigado"))
  val juan65 =  Interseccion(10500, 10500, Some("Juan 65"))
  val juan80 =  Interseccion(10500, 1500, Some("Juan 80"))
  val _33_65 =  Interseccion(12000, 10500, Some("33 con 65"))
  val bule =  Interseccion(12000, 7500, Some("Bulerias"))
  val gema =  Interseccion(12000, 1500, Some("St Gema"))
  val _30_65 =  Interseccion(13500, 10500, Some("30 con 65"))
  val _30_70 =  Interseccion(13500, 4500, Some("30 con 70"))
  val _30_80 =  Interseccion(13500, 1500, Some("30 con 80"))
  val bol65 =  Interseccion(11100, 10500, Some("Boliv con 65"))
  val gu10 =  Interseccion(16500, 12000, Some("Guay con 10"))
  val terminal =  Interseccion(16500, 10500, Some("Term Sur"))
  val gu30 =  Interseccion(13500, 12000, Some("Guay 30"))
  val gu80 =  Interseccion(19500, 12000, Some("Guay 80"))
  val _65_80 =  Interseccion(19500, 10500, Some("65 con 30"))
  val gu_37S =  Interseccion(21000, 12000, Some("Guay con 37S"))
  
  val listaVias = ArrayBuffer(
     Via(niquia, lauraAuto, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
     Via(niquia, lauraReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(lauraAuto, lauraReg, 60, TipoVia("Calle"), Sentido.dobleVia, "94", "Pte Madre Laura"),
     Via(lauraAuto, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
     Via(lauraReg, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(ptoCero, mino, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
     Via(mino, villa, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
     Via(ptoCero, ig65, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
     Via(ig65, robledo, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
     Via(ptoCero, colReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(colReg, maca, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(maca, expo, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(expo, reg30, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(reg30, monte, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(monte, agua, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(agua, viva, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(viva, mayor, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
     Via(mino, ferrCol, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
     Via(ferrCol, ferrJuan, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
     Via(ferrJuan, expo, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
     Via(villa, juanOr, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
     Via(juanOr, sanDiego, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
     Via(sanDiego, premium, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
     Via(premium, pp, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
     Via(pp, santafe, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
     Via(santafe, pqEnv, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
     Via(pqEnv, mayor, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
     Via(ferrCol, colReg, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
     Via(colReg, col65, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
     Via(col65, col80, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
     Via(juanOr, ferrJuan, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
     Via(ferrJuan, maca, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
     Via(maca, juan65, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
     Via(juan65, juan80, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
     Via(sanDiego, expo, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
     Via(expo, _33_65, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
     Via(_33_65, bule, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
     Via(bule, gema, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
     Via(premium, reg30, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
     Via(reg30, _30_65, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
     Via(_30_65, _30_70, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
     Via(_30_70, _30_80, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
     Via(maca, bol65, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
     Via(bol65, bule, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
     Via(bule, _30_70, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
     Via(juan80, bule, 60, TipoVia("Transversal"), Sentido.dobleVia, "39B", "Nutibara"),
     Via(pp, monte, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
     Via(monte, gu10, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
     Via(gu10, terminal, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
     Via(expo, gu30, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
     Via(gu30, gu10, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
     Via(gu10, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
     Via(gu80, gu_37S, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
     Via(lauraAuto, ig65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
     Via(ig65, col65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
     Via(juan65, col65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
     Via(bol65, juan65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
     Via(_33_65, bol65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
     Via(_30_65, _33_65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
     Via(_30_65, terminal, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
     Via(terminal, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "65"),
     Via(robledo, col80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(col80, juan80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(juan80, gema, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(gema, _30_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(_30_80, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(_65_80, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(gu80, agua, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
     Via(agua, santafe, 60, TipoVia("Calle"), Sentido.dobleVia, "12S", "80"),
     Via(viva, pqEnv, 60, TipoVia("Calle"), Sentido.dobleVia, "37S", "37S"),
     Via(viva, gu_37S, 60, TipoVia("Calle"), Sentido.dobleVia, "63", "37S"))
    
    listaVias
  }
}