package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class MinimoMaximoPromedio(private var _minima: Int, private var _maxima: Int, private var _promedio: Double)
  extends SerializableJson {

  //Getters
  def minima = _minima
  def maxima = _maxima
  def promedio = _promedio

  //Setters
  def minima_=(minima: Int) = _minima = minima
  def maxima_=(maxima: Int) = _maxima = maxima
  def promedio_=(promedio: Double) = _promedio = promedio

  def getAtributosJson = JField("minima", JInt(minima)) ::
    JField("maxima", JInt(maxima)) ::
    JField("promedio", JDouble(promedio)) :: Nil

}

class MinimoMaximoPromedioSerializer extends Serializer[MinimoMaximoPromedio] {
  private val MinimoMaximoPromedioClass = classOf[MinimoMaximoPromedio]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), MinimoMaximoPromedio] = {
    case (TypeInfo(MinimoMaximoPromedioClass, _), json) => json match {
      case JObject(JField("minima", JInt(minima)) :: JField("maxima", JInt(maxima)) ::
        JField("promedio", JDouble(promedio)) :: Nil) =>
        new MinimoMaximoPromedio(minima.intValue, maxima.intValue, promedio.intValue)
      case x => throw new MappingException("Can't convert " + x + " to MinimoMaximoPromedio")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Tiempos => JObject(x.getAtributosJson)
  }
}