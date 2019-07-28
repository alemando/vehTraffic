package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class Vehiculos(private var _total: Int, private var _carros: Int, private var _motos: Int, private var _buses: Int,
                     private var _camiones: Int, private var _motoTaxis: Int) extends SerializableJson {

  //Getters
  def total = _total
  def carros = _carros
  def motos = _motos
  def buses = _buses
  def camiones = _camiones
  def motoTaxis = _motoTaxis

  //Setters
  def total_=(total: Int) = _total = total
  def carros_=(carros: Int) = _carros = carros
  def motos_=(motos: Int) = _motos = motos
  def buses_=(buses: Int) = _buses = buses
  def camiones_=(camiones: Int) = _camiones = camiones
  def motoTaxis_=(motoTaxis: Int) = _motoTaxis = motoTaxis

  def getAtributosJson = JField("total", JInt(total)) ::
    JField("carros", JInt(carros)) ::
    JField("motos", JInt(motos)) ::
    JField("buses", JInt(buses)) ::
    JField("camiones", JInt(camiones)) ::
    JField("motoTaxis", JInt(motoTaxis)) :: Nil

}

class VehiculosSerializer extends Serializer[Vehiculos] {
  private val VehiculosClass = classOf[Vehiculos]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Vehiculos] = {
    case (TypeInfo(VehiculosClass, _), json) => json match {
      case JObject(JField("total", JInt(total)) :: JField("carros", JInt(carros)) ::
        JField("motos", JInt(motos)) :: JField("buses", JInt(buses)) ::
        JField("camiones", JInt(camiones)) :: JField("motoTaxis", JInt(motoTaxis)) :: Nil) =>
        new Vehiculos(total.intValue, carros.intValue, motos.intValue, buses.intValue, camiones.intValue, motoTaxis.intValue)
      case x => throw new MappingException("Can't convert " + x + " to Vehiculos")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Vehiculos => JObject(x.getAtributosJson)
  }
}