package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class Result(private var _resultadosSimulacion: ResultadosSimulacion) extends SerializableJson {

  //Getters
  def resultadosSimulacion = _resultadosSimulacion
  
  //Setters
  def resultadoSimulacion_= (resultadosSimulacion: ResultadosSimulacion) = _resultadosSimulacion = resultadosSimulacion

  def getAtributosJson = JField("resultadosSimulacion", JObject(resultadosSimulacion.getAtributosJson)) :: Nil

}

class ResultSerializer extends Serializer[Result] {
  private val ResultClass = classOf[Result]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Result] = {
    case (TypeInfo(ResultClass, _), json) => json match {
      case JObject(JField("resultadosSimulacion", resultadosSimulacion) :: Nil) =>
        new Result(resultadosSimulacion.extract[ResultadosSimulacion])
      case x => throw new MappingException("Can't convert " + x + " to Resultado")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Result => JObject(x.getAtributosJson)
  }
}