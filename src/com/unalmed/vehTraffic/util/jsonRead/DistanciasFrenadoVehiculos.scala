package com.unalmed.vehTraffic.util.jsonRead

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import scala.math.BigInt.int2bigInt
import com.unalmed.vehTraffic.util.SerializableJson

case class DistanciasFrenadoVehiculos(private var _xSemaforoFrenar: Int, private var _xSemaforoAmarilloContinuar: Int)
  extends SerializableJson {

  //Getters
  def xSemaforoFrenar = _xSemaforoFrenar
  def xSemaforoAmarilloContinuar = _xSemaforoAmarilloContinuar

  //Setters
  def xSemaforoFrenar_=(xSemaforoFrenar: Int) = _xSemaforoFrenar = xSemaforoFrenar
  def xSemaforoAmarilloContinuarloContinuar_=(xSemaforoAmarilloContinuar: Int) = _xSemaforoAmarilloContinuar = xSemaforoAmarilloContinuar

  def getAtributosJson = JField("xSemaforoFrenar", JInt(xSemaforoFrenar)) ::
    JField("xSemaforoAmarilloContinuar", JInt(xSemaforoAmarilloContinuar)) :: Nil
}

class DistanciasFrenadoVehiculosSerializer extends Serializer[DistanciasFrenadoVehiculos] {
  private val DistanciasFrenadoVehiculosClass = classOf[DistanciasFrenadoVehiculos]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DistanciasFrenadoVehiculos] = {
    case (TypeInfo(DistanciasFrenadoVehiculosClass, _), json) => json match {
      case JObject(JField("xSemaforoFrenar", JInt(xSemaforoFrenar)) ::
        JField("xSemaforoAmarilloContinuar", JInt(xSemaforoAmarilloContinuar)) :: Nil) =>
        new DistanciasFrenadoVehiculos(xSemaforoFrenar.intValue, xSemaforoAmarilloContinuar.intValue)
      case x => throw new MappingException("Can't convert " + x + " to DistanciasFrenadoVehiculos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: DistanciasFrenadoVehiculos => JObject(x.getAtributosJson)
  }
}