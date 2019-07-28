package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

class VehiculosEnInterseccion(private var _promedioOrigen: Int, private var _promedioDestino: Int, private var _sinOrigen: Int,
                              private var _sinDestino: Int) extends SerializableJson {

  //Getters
  def promedioOrigen = _promedioOrigen
  def promedioDestino = _promedioDestino
  def sinOrigen = _sinOrigen
  def sinDestino = _sinDestino
  
  //Setters
  def promedioOrigen_= (promedioOrigen: Int) = _promedioOrigen = promedioOrigen
  def promedioDestino_= (promedioDestino: Int) = _promedioDestino = promedioDestino
  def sinOrigen_= (sinOrigen: Int) = _sinOrigen = sinOrigen
  def sinDestino_= (sinDestino: Int) = _sinDestino = sinDestino

  def getAtributosJson = JField("promedioOrigen", JInt(promedioOrigen)) ::
    JField("promedioDestino", JInt(promedioDestino)) ::
    JField("sinOrigen", JInt(sinOrigen)) ::
    JField("sinDestino", JInt(sinDestino)) :: Nil

}

class VehiculosEnInterseccionSerializer extends Serializer[VehiculosEnInterseccion] {
  private val VehiculosEnInterseccionClass = classOf[VehiculosEnInterseccion]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), VehiculosEnInterseccion] = {
    case (TypeInfo(VehiculosEnInterseccionClass, _), json) => json match {
      case JObject(JField("promedioOrigen", JInt(promedioOrigen)) :: JField("promedioDestino", JInt(promedioDestino)) ::
        JField("sinOrigen", JInt(sinOrigen)) :: JField("sinDestino", JInt(sinDestino)) :: Nil) =>
        new VehiculosEnInterseccion(promedioOrigen.intValue, promedioDestino.intValue, sinOrigen.intValue, sinDestino.intValue)
      case x => throw new MappingException("Can't convert " + x + " to Vehiculos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: VehiculosEnInterseccion => JObject(x.getAtributosJson)
  }
}