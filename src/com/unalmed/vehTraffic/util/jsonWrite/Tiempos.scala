package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

class Tiempos (private var _simulacion: Int, private var _realidad: Int) extends SerializableJson {
  
  //Getters
  def simulacion = _simulacion
  def realidad = _realidad
  
  //Setters
  def simulacion_= (simulacion: Int) = _simulacion = simulacion
  def realidad_= (realidad: Int) = _realidad = realidad
  
  def getAtributosJson = JField("simulacion", JInt(simulacion)) ::
    JField("realidad", JInt(realidad)) :: Nil  
  
}

class TiemposSerializer extends Serializer[Tiempos] {
  private val TiemposClass = classOf[Tiempos]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Tiempos] = {
    case (TypeInfo(TiemposClass, _), json) => json match {
      case JObject(JField("simulacion", JInt(simulacion)) :: JField("realidad", JInt(realidad)) :: Nil) =>
        new Tiempos(simulacion.intValue, realidad.intValue)
      case x => throw new MappingException("Can't convert " + x + " to Tiempos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Tiempos => JObject(x.getAtributosJson)
  }
}