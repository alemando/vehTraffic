package com.unalmed.vehTraffic.util
import net.liftweb.json._
import scala.io.Source
import net.liftweb.json.Serialization.write
import java.io.PrintWriter
import java.io.File
import com.unalmed.vehTraffic.util.jsonRead._
import com.unalmed.vehTraffic.util.jsonWrite._

object JsonRW {

  implicit val formats = Serialization.formats(NoTypeHints) +
    new ConfigSerializer + new ParametrosSimulacionSerializer + new MinimoMaximoSerializer +
    new ProporcionesSerializer + new ResultSerializer + new ResultadosSimulacionSerializer +
    new VehiculosSerializer + new MallaVialSerializer + new VehiculosEnInterseccionSerializer +
    new TiemposSerializer + new MinimoMaximoPromedioSerializer + new SemaforosSerializer +
    new DistanciasFrenadoVehiculosSerializer + new ComparendosSerializer
    // for json handling
    
  private val basePath: String = System.getProperty("user.dir")+ "\\temp\\"
  private val configFile: String = "parametros.json"
  private val resultFile: String = "resultados.json"
    
  private var _config: Config = _
  private var _result: Result = _
  
  //Getters
  def config = _config
  def result = _result
  
  //Setter
  def config_= (config: Config) = _config = config
  def result_= (result: Result) = _result = result

    
  //RW Config methods    
  def readConfig(): Config = {
    val json = JsonParser.parse(Source.fromFile(basePath + configFile).getLines.mkString)
    config_= (json.extract[Config])
    config
  }

  def writeConfig(config: Config) = {
    val pw = new PrintWriter(new File(basePath + configFile))
    pw.write(write(config))
    pw.close
  }
  
  
  //RW Result methods
  def readResult(): Result = {
    val json = JsonParser.parse(Source.fromFile(basePath + resultFile).getLines.mkString)
    result_= (json.extract[Result])
    result
  }

  def writeResult(result: Result) = {
    val pw = new PrintWriter(new File(basePath + resultFile))
    pw.write(write(result))
    pw.close
  }
  
  //Create Result base class
  def getResultBaseClass: Result = {
    var result = new Result(
      new ResultadosSimulacion(
        new Vehiculos(0, 0, 0, 0, 0, 0),
        new MallaVial(0, 0, 0, 0, 0, 0, 0, new VehiculosEnInterseccion(0, 0, 0, 0)),
        new Tiempos(0, 0),
        new MinimoMaximoPromedio(0, 0, 0),
        new MinimoMaximoPromedio(0, 0, 0),
        new Comparendos(0, 0.0)))

    result
  }

}