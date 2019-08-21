package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class Comparendos(private var _cantidad: Int, private var _promedioPorcentajeExceso: Double)
  extends SerializableJson {
  
  //Getters
  def cantidad = _cantidad
  def promedioPorcentajeExceso = _promedioPorcentajeExceso
  
  //Setters
  def cantidad_= (cantidad: Int) = _cantidad = cantidad
  def promedioPorcentajeExceso_= (promedioPorcentajeExceso: Double) = _promedioPorcentajeExceso = promedioPorcentajeExceso
  
  def getAtributosJson = JField("cantidad", JInt(cantidad)) ::
    JField("promedioPorcentajeExceso", JDouble(promedioPorcentajeExceso)) :: Nil  
}

class ComparendosSerializer extends Serializer[Comparendos] {
  private val ComparendosClass = classOf[Comparendos]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Comparendos] = {
    case (TypeInfo(ComparendosClass, _), json) => json match {
      case JObject(JField("cantidad", JInt(cantidad)) :: 
          JField("promedioPorcentajeExceso", JDouble(promedioPorcentajeExceso)) :: Nil) =>
        new Comparendos(cantidad.intValue, promedioPorcentajeExceso.doubleValue)
      case x => throw new MappingException("Can't convert " + x + " to Comparendos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Comparendos => JObject(x.getAtributosJson)
  }
}