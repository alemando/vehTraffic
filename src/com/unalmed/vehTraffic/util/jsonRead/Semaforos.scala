package com.unalmed.vehTraffic.util.jsonRead

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import scala.math.BigInt.int2bigInt
import com.unalmed.vehTraffic.util.SerializableJson

case class Semaforos(private var _minTiempoVerde: Int, private var _maxTiempoVerde: Int, private var _tiempoAmarillo: Int) 
extends SerializableJson {

  //Getters
  def minTiempoVerde = _minTiempoVerde
  def maxTiempoVerde = _maxTiempoVerde
  def tiempoAmarillo = _tiempoAmarillo

  //Setters
  def minTiempoVerde_=(minTiempoVerde: Int) = _minTiempoVerde = minTiempoVerde
  def maxTiempoVerde_=(maxTiempoVerde: Int) = _maxTiempoVerde = maxTiempoVerde
  def tiempoAmarillo_=(tiempoAmarillo: Int) = _tiempoAmarillo = tiempoAmarillo

  def getAtributosJson = JField("minTiempoVerde", JInt(minTiempoVerde)) ::
    JField("maxTiempoVerde", JInt(maxTiempoVerde)) :: JField("tiempoAmarillo", JInt(tiempoAmarillo)) :: Nil

}

class SemaforosSerializer extends Serializer[Semaforos] {
  private val SemaforosClass = classOf[Semaforos]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Semaforos] = {
    case (TypeInfo(SemaforosClass, _), json) => json match {
      case JObject(JField("minTiempoVerde", JInt(minTiempoVerde)) :: JField("maxTiempoVerde", JInt(maxTiempoVerde)) :: 
          JField("tiempoAmarillo", JInt(tiempoAmarillo)) ::Nil) =>
        new Semaforos(minTiempoVerde.intValue, maxTiempoVerde.intValue, tiempoAmarillo.intValue)
      case x => throw new MappingException("Can't convert " + x + " to Semaforos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Semaforos => JObject(x.getAtributosJson)
  }
}