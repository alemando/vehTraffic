package com.unalmed.vehTraffic.main

import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.mallaVial.Interseccion
import com.unalmed.vehTraffic.mallaVial.TipoVia
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.Via
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.frame.Grafico

object Main extends App{

  val listaVias = cargarVias()
  
  val listaIntersecciones: ArrayBuffer[Interseccion] = (listaVias.map(_.origen) ++ listaVias.map(_.fin)).distinct
  
  Grafico.iniciarGrafico(listaVias, listaIntersecciones)
  
  var objectSimulacion: Simulacion = new Simulacion(listaVias, listaIntersecciones)
  
  def start() = {
    while(Thread.activeCount()>2){
      Thread.sleep(100)
    }
    if (objectSimulacion != null) objectSimulacion.borrar
    objectSimulacion = new Simulacion(listaVias, listaIntersecciones)
    objectSimulacion.start()
    
  }
  
  def stop() = {
    objectSimulacion.stop()
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
     Via(niquia, lauraAuto, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", Some("Auto Norte")),
     Via(niquia, lauraReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(lauraAuto, lauraReg, 60, TipoVia("Calle"), Sentido.dobleVia, "94", Some("Pte Madre Laura")),
     Via(lauraAuto, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", Some("Auto Norte")),
     Via(lauraReg, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(ptoCero, mino, 60, TipoVia("Calle"), Sentido.dobleVia, "58", Some("Oriental")),
     Via(mino, villa, 60, TipoVia("Calle"), Sentido.dobleVia, "58", Some("Oriental")),
     Via(ptoCero, ig65, 60, TipoVia("Calle"), Sentido.dobleVia, "55", Some("Iguaná")),
     Via(ig65, robledo, 60, TipoVia("Calle"), Sentido.dobleVia, "55", Some("Iguaná")),
     Via(ptoCero, colReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(colReg, maca, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(maca, expo, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(expo, reg30, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(reg30, monte, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(monte, agua, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(agua, viva, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(viva, mayor, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
     Via(mino, ferrCol, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
     Via(ferrCol, ferrJuan, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
     Via(ferrJuan, expo, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
     Via(villa, juanOr, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", Some("Oriental")),
     Via(juanOr, sanDiego, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", Some("Oriental")),
     Via(sanDiego, premium, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
     Via(premium, pp, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
     Via(pp, santafe, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
     Via(santafe, pqEnv, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
     Via(pqEnv, mayor, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
     Via(ferrCol, colReg, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
     Via(colReg, col65, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
     Via(col65, col80, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
     Via(juanOr, ferrJuan, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
     Via(ferrJuan, maca, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
     Via(maca, juan65, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
     Via(juan65, juan80, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
     Via(sanDiego, expo, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
     Via(expo, _33_65, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
     Via(_33_65, bule, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
     Via(bule, gema, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
     Via(premium, reg30, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
     Via(reg30, _30_65, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
     Via(_30_65, _30_70, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
     Via(_30_70, _30_80, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
     Via(maca, bol65, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
     Via(bol65, bule, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
     Via(bule, _30_70, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
     Via(juan80, bule, 60, TipoVia("Transversal"), Sentido.dobleVia, "39B", Some("Nutibara")),
     Via(pp, monte, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
     Via(monte, gu10, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
     Via(gu10, terminal, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
     Via(expo, gu30, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
     Via(gu30, gu10, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
     Via(gu10, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
     Via(gu80, gu_37S, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
     Via(lauraAuto, ig65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
     Via(ig65, col65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
     Via(juan65, col65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
     Via(bol65, juan65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
     Via(_33_65, bol65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
     Via(_30_65, _33_65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
     Via(_30_65, terminal, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
     Via(terminal, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("65")),
     Via(robledo, col80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(col80, juan80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(juan80, gema, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(gema, _30_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(_30_80, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(_65_80, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(gu80, agua, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
     Via(agua, santafe, 60, TipoVia("Calle"), Sentido.dobleVia, "12S", Some("80")),
     Via(viva, pqEnv, 60, TipoVia("Calle"), Sentido.dobleVia, "37S", Some("37S")),
     Via(viva, gu_37S, 60, TipoVia("Calle"), Sentido.dobleVia, "63", Some("37S")))
    
    listaVias
  }
}