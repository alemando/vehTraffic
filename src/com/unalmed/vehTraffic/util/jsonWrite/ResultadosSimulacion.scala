package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class ResultadosSimulacion(private var _vehiculos: Vehiculos, private var _mallaVial: MallaVial, private var _tiempos: Tiempos,
                                private var _velocidades: MinimoMaximoPromedio, private var _distancias: MinimoMaximoPromedio,
                                private var _comparendos: Comparendos)
  extends SerializableJson {

  //Getters
  def vehiculos = _vehiculos
  def mallaVial = _mallaVial
  def tiempos = _tiempos
  def velocidades = _velocidades
  def distancias = _distancias
  def comparendos = _comparendos
  
  //Setters
  def vehiculos_= (vehiculos: Vehiculos) = _vehiculos = vehiculos
  def mallaVial_= (mallaVial: MallaVial) = _mallaVial = mallaVial
  def tiempos_= (tiempos: Tiempos) = _tiempos = tiempos
  def velocidades_= (velocidades: MinimoMaximoPromedio) = _velocidades = velocidades
  def distancias_= (distancias: MinimoMaximoPromedio) = _distancias = distancias
  def comparendos_= (comparendos: Comparendos) = _comparendos = comparendos

  def getAtributosJson = JField("vehiculos", JObject(vehiculos.getAtributosJson)) ::
    JField("mallaVial", JObject(mallaVial.getAtributosJson)) ::
    JField("tiempos", JObject(tiempos.getAtributosJson)) ::
    JField("velocidades", JObject(velocidades.getAtributosJson)) ::
    JField("distancias", JObject(distancias.getAtributosJson)) ::
    JField("comparendos", JObject(comparendos.getAtributosJson)) :: Nil
}

class ResultadosSimulacionSerializer extends Serializer[ResultadosSimulacion] {
  private val ResultadosSimulacionClass = classOf[ResultadosSimulacion]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), ResultadosSimulacion] = {
    case (TypeInfo(ResultadosSimulacionClass, _), json) => json match {
      case JObject(JField("vehiculos", vehiculos) ::
        JField("mallaVial", mallaVial) ::
        JField("tiempos", tiempos) ::
        JField("velocidades", velocidades) ::
        JField("distancias", distancias) :: 
        JField("comparendos", comparendos) ::Nil) =>
        new ResultadosSimulacion(
          vehiculos.extract[Vehiculos],
          mallaVial.extract[MallaVial],
          tiempos.extract[Tiempos],
          velocidades.extract[MinimoMaximoPromedio],
          distancias.extract[MinimoMaximoPromedio],
          comparendos.extract[Comparendos])
      case x => throw new MappingException("Can't convert " + x + " to ResultadosSimulacion")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: ResultadosSimulacion => JObject(x.getAtributosJson)
  }
}