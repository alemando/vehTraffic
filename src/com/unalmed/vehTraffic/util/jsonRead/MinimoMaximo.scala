package com.unalmed.vehTraffic.util.jsonRead

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import scala.math.BigInt.int2bigInt
import com.unalmed.vehTraffic.util.SerializableJson

case class MinimoMaximo(private var _minimo: Int, private var _maximo: Int)  extends SerializableJson {

  //Getters
  def minimo = _minimo
  def maximo = _maximo
  
  //Setters
  def minimo_= (minimo: Int) = _minimo = minimo
  def maximo_= (maximo: Int) = _maximo = maximo

  def getAtributosJson = JField("minimo", JInt(minimo)) ::
    JField("maximo", JInt(maximo)) :: Nil

}

class MinimoMaximoSerializer extends Serializer[MinimoMaximo] {
  private val MinimoMaximoClass = classOf[MinimoMaximo]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), MinimoMaximo] = {
    case (TypeInfo(MinimoMaximoClass, _), json) => json match {
      case JObject(JField("minimo", JInt(minimo)) :: JField("maximo", JInt(maximo)) :: Nil) =>
        new MinimoMaximo(minimo.intValue, maximo.intValue)
      case x => throw new MappingException("Can't convert " + x + " to MinimoMaximo")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: MinimoMaximo => JObject(x.getAtributosJson)
  }
}