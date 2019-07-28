package com.unalmed.vehTraffic.util.jsonRead

import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson
import net.liftweb.json._

case class Config(private var _parametrosSimulacion: ParametrosSimulacion)  extends SerializableJson {

  //Getters
  def parametrosSimulacion = _parametrosSimulacion
  
  //Setters
  def parametrosSimulacion_= (parametrosSimulacion: ParametrosSimulacion) = _parametrosSimulacion = parametrosSimulacion

  def getAtributosJson = 
    JField("parametrosSimulacion", JObject(parametrosSimulacion.getAtributosJson)) :: Nil

}

class ConfigSerializer extends Serializer[Config] {
  private val ConfigClass = classOf[Config]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Config] = {
    case (TypeInfo(ConfigClass, _), json) => json match {
      case JObject(JField("parametrosSimulacion", parametrosSimulacion) ::Nil) =>
        new Config(parametrosSimulacion.extract[ParametrosSimulacion])
      case x => throw new MappingException("Can't convert " + x + " to Config")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Config => JObject(x.getAtributosJson)
  }
}