package com.unalmed.vehTraffic.simulacion

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.{Via, Interseccion}
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.dimension.{Sentido,TipoVia}
import com.unalmed.vehTraffic.util.JsonRW
import com.unalmed.vehTraffic.base.Grafico

object Simulacion extends Runnable{
  
  val niquia = new Interseccion(300, 12000, Some("Niquia"))
  val lauraAuto = new Interseccion(2400, 11400, Some("M. Laura Auto"))
  val lauraReg = new Interseccion(2400, 12600, Some("M. Laura Reg"))
  val ptoCero = new Interseccion(5400, 12000, Some("Pto 0"))
  val mino = new Interseccion(6300, 15000, Some("Minorista"))
  val villa = new Interseccion(6300, 19500, Some("Villanueva"))
  val ig65 = new Interseccion(5400, 10500, Some("65 Igu"))
  val robledo = new Interseccion(5400, 1500, Some("Exito Rob"))
  val colReg = new Interseccion(8250, 12000, Some("Col Reg"))
  val col65 = new Interseccion(8250, 10500, Some("Col 65"))
  val col80 = new Interseccion(8250, 1500, Some("Col 80"))
  val juanOr = new Interseccion(10500, 19500, Some("Sn Juan Ori"))
  val maca = new Interseccion(10500, 12000, Some("Macarena"))
  val expo = new Interseccion(12000, 13500, Some("Exposiciones"))
  val reg30 = new Interseccion(13500, 15000, Some("Reg 30"))
  val monte = new Interseccion(16500, 15000, Some("Monterrey"))
  val agua = new Interseccion(19500, 15000, Some("Aguacatala"))
  val viva = new Interseccion(21000, 15000, Some("Viva Env"))
  val mayor = new Interseccion(23400, 15000, Some("Mayorca"))
  val ferrCol = new Interseccion(8250, 15000, Some("Ferr Col"))
  val ferrJuan = new Interseccion(10500, 15000, Some("Alpujarra"))
  val sanDiego = new Interseccion(12000, 19500, Some("San Diego"))
  val premium = new Interseccion(13500, 19500, Some("Premium"))
  val pp = new Interseccion(16500, 19500, Some("Parque Pob"))
  val santafe = new Interseccion(19500, 18750, Some("Santa Fe"))
  val pqEnv = new Interseccion(21000, 18000, Some("Envigado"))
  val juan65 = new Interseccion(10500, 10500, Some("Juan 65"))
  val juan80 = new Interseccion(10500, 1500, Some("Juan 80"))
  val _33_65 = new Interseccion(12000, 10500, Some("33 con 65"))
  val bule = new Interseccion(12000, 7500, Some("Bulerias"))
  val gema = new Interseccion(12000, 1500, Some("St Gema"))
  val _30_65 = new Interseccion(13500, 10500, Some("30 con 65"))
  val _30_70 = new Interseccion(13500, 4500, Some("30 con 70"))
  val _30_80 = new Interseccion(13500, 1500, Some("30 con 80"))
  val bol65 = new Interseccion(11100, 10500, Some("Boliv con 65"))
  val gu10 = new Interseccion(16500, 12000, Some("Guay con 10"))
  val terminal = new Interseccion(16500, 10500, Some("Term Sur"))
  val gu30 = new Interseccion(13500, 12000, Some("Guay 30"))
  val gu80 = new Interseccion(19500, 12000, Some("Guay 80"))
  val _65_80 = new Interseccion(19500, 10500, Some("65 con 30"))
  val gu_37S = new Interseccion(21000, 12000, Some("Guay con 37S"))
  
  /*val listaIntersecciones: ArrayBuffer[Interseccion] = ArrayBuffer(niquia, lauraAuto, lauraReg, ptoCero, mino, villa, ig65, robledo, colReg, col65, col80,
      juanOr, maca, expo, reg30, monte, agua, viva, mayor, ferrCol, ferrJuan, sanDiego, premium, pp, santafe, pqEnv, juan65, juan80, _33_65, bule,
      gema, _30_65, _30_70, _30_80, bol65, gu10, terminal, gu30, gu80,  _65_80, gu_37S)
  */
  val listaVias = ArrayBuffer(
    new Via(niquia, lauraAuto, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
    new Via(niquia, lauraReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(lauraAuto, lauraReg, 60, TipoVia("Calle"), Sentido.dobleVia, "94", "Pte Madre Laura"),
    new Via(lauraAuto, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
    new Via(lauraReg, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(ptoCero, mino, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
    new Via(mino, villa, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
    new Via(ptoCero, ig65, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
    new Via(ig65, robledo, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
    new Via(ptoCero, colReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(colReg, maca, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(maca, expo, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(expo, reg30, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(reg30, monte, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(monte, agua, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(agua, viva, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(viva, mayor, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
    new Via(mino, ferrCol, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
    new Via(ferrCol, ferrJuan, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
    new Via(ferrJuan, expo, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
    new Via(villa, juanOr, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
    new Via(juanOr, sanDiego, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
    new Via(sanDiego, premium, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
    new Via(premium, pp, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
    new Via(pp, santafe, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
    new Via(santafe, pqEnv, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
    new Via(pqEnv, mayor, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
    new Via(ferrCol, colReg, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
    new Via(colReg, col65, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
    new Via(col65, col80, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
    new Via(juanOr, ferrJuan, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
    new Via(ferrJuan, maca, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
    new Via(maca, juan65, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
    new Via(juan65, juan80, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
    new Via(sanDiego, expo, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
    new Via(expo, _33_65, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
    new Via(_33_65, bule, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
    new Via(bule, gema, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
    new Via(premium, reg30, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
    new Via(reg30, _30_65, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
    new Via(_30_65, _30_70, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
    new Via(_30_70, _30_80, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
    new Via(maca, bol65, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
    new Via(bol65, bule, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
    new Via(bule, _30_70, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
    new Via(juan80, bule, 60, TipoVia("Transversal"), Sentido.dobleVia, "39B", "Nutibara"),
    new Via(pp, monte, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
    new Via(monte, gu10, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
    new Via(gu10, terminal, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
    new Via(expo, gu30, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
    new Via(gu30, gu10, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
    new Via(gu10, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
    new Via(gu80, gu_37S, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
    new Via(lauraAuto, ig65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
    new Via(ig65, col65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
    new Via(juan65, col65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
    new Via(bol65, juan65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
    new Via(_33_65, bol65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
    new Via(_30_65, _33_65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
    new Via(_30_65, terminal, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
    new Via(terminal, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "65"),
    new Via(robledo, col80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(col80, juan80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(juan80, gema, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(gema, _30_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(_30_80, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(_65_80, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(gu80, agua, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
    new Via(agua, santafe, 60, TipoVia("Calle"), Sentido.dobleVia, "12S", "80"),
    new Via(viva, pqEnv, 60, TipoVia("Calle"), Sentido.dobleVia, "37S", "37S"),
    new Via(viva, gu_37S, 60, TipoVia("Calle"), Sentido.dobleVia, "63", "37S"))
   
  var t: Int = 0
  
  val listaVehiculos: ArrayBuffer[Vehiculo] = new ArrayBuffer()
  
  val listaIntersecciones: ArrayBuffer[Interseccion] = (listaVias.map(_.origen) ++ listaVias.map(_.fin)).distinct
  
  Grafico.iniciarGrafico(listaVias, listaVehiculos, listaIntersecciones)
  
  Grafico.graficarVias(listaVias)
  
  //TODO Generacion autos
  
  //Ubicacion json
  val basePath = System.getProperty("user.dir")+ "\\temp\\"
  val configFile ="Config.json"
  
  //Leer archivo json (crea objeto con todos los valores en una variable (config) de la clase JsonRW)
  var config = JsonRW.readConfig(basePath + configFile)
  
  val dt: Int = config.parametrosSimulacion.dt  
  val tRefresh: Int = config.parametrosSimulacion.tRefresh*1000
  val minVehiculos: Int = config.parametrosSimulacion.vehiculos.minimo 
  val maxVehiculos: Int = config.parametrosSimulacion.vehiculos.maximo 
  val minVelocidad: Int = config.parametrosSimulacion.velocidad.minimo  
  val maxVelocidad: Int = config.parametrosSimulacion.velocidad.maximo  
  val proporciónCarros: Double = JsonRW.config.parametrosSimulacion.proporciones.carros 
  val proporciónMotos: Double = JsonRW.config.parametrosSimulacion.proporciones.motos
  val proporciónBuses: Double = JsonRW.config.parametrosSimulacion.proporciones.buses
  val proporciónCamiones: Double = JsonRW.config.parametrosSimulacion.proporciones.camiones  
  val proporciónMotoTaxis: Double = JsonRW.config.parametrosSimulacion.proporciones.motoTaxis
  
  //TODO Resultados simulacion
  
  def run() {
    while (true) {
      listaVehiculos.foreach(_.cambioPosicion(dt))
      t = t + dt
      Grafico.graficarVehiculos(listaVehiculos)
      Thread.sleep(tRefresh)
    }
  }
}