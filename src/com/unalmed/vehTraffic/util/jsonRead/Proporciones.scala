package com.unalmed.vehTraffic.util.jsonRead

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import com.unalmed.vehTraffic.util.SerializableJson

case class Proporciones(private var _carros: Double, private var _motos: Double, private var _buses: Double,
                        private var _camiones: Double, private var _motoTaxis: Double) extends SerializableJson {

  //Getters
  def carros = _carros
  def motos = _motos
  def buses = _buses
  def camiones = _camiones
  def motoTaxis = _motoTaxis
  
  //Setters
  def carros_= (carros: Double) = _carros = carros
  def motos_= (motos: Double) = _motos = motos
  def buses_= (buses: Double) = _buses = buses
  def camiones_= (camiones: Double) = _camiones = camiones
  def motoTaxis_= (motoTaxis: Double) = _motoTaxis = motoTaxis

  def getAtributosJson = JField("carros", JDouble(carros)) ::
    JField("motos", JDouble(motos)) ::
    JField("buses", JDouble(buses)) ::
    JField("camiones", JDouble(camiones)) ::
    JField("motoTaxis", JDouble(motoTaxis)) :: Nil

}

class ProporcionesSerializer extends Serializer[Proporciones] {
  private val ProporcionesoClass = classOf[Proporciones]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Proporciones] = {
    case (TypeInfo(ProporcionesoClass, _), json) => json match {
      case JObject(JField("carros", JDouble(carros)) :: JField("motos", JDouble(motos)) ::
        JField("buses", JDouble(buses)) :: JField("camiones", JDouble(camiones)) ::
        JField("motoTaxis", JDouble(motoTaxis)) :: Nil) =>
        new Proporciones(carros, motos, buses, camiones, motoTaxis)
      case x => throw new MappingException("Can't convert " + x + " to Empleado")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Proporciones => JObject(x.getAtributosJson)
  }
}