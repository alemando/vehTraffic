package com.unalmed.vehTraffic.util.jsonRead

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class ParametrosSimulacion(private var _dt: Double, private var _tRefresh: Double, private var _vehiculos: MinimoMaximo,
                                private var _velocidad: MinimoMaximo, private var _proporciones: Proporciones,
                                private var _semaforos: Semaforos)
  extends SerializableJson {

  //Getters
  def dt = _dt
  def tRefresh = _tRefresh
  def vehiculos = _vehiculos
  def velocidad = _velocidad
  def proporciones = _proporciones
  def semaforos = _semaforos
  
  //Setters
  def dt_= (dt: Double) = _dt = dt
  def tRefresh_= (tRefresh: Double) = _tRefresh = tRefresh
  def vehiculos_= (vehiculos: MinimoMaximo) = _vehiculos = vehiculos
  def velocidad_= (velocidad: MinimoMaximo) = _velocidad = velocidad
  def proporciones_= (proporciones: Proporciones) = _proporciones = proporciones
  def semaforos_= (semaforos: Semaforos) = _semaforos = semaforos

  def getAtributosJson = JField("dt", JDouble(dt.toDouble)) ::
    JField("tRefresh", JDouble(tRefresh.toDouble)) ::
    JField("vehiculos", JObject(vehiculos.getAtributosJson)) ::
    JField("velocidad", JObject(velocidad.getAtributosJson)) ::
    JField("proporciones", JObject(proporciones.getAtributosJson)) ::
    JField("semaforos", JObject(semaforos.getAtributosJson)) :: Nil

}

class ParametrosSimulacionSerializer extends Serializer[ParametrosSimulacion] {
  private val ParametrosSimulacionClass = classOf[ParametrosSimulacion]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), ParametrosSimulacion] = {
    case (TypeInfo(ParametrosSimulacionClass, _), json) => json match {
      case JObject(JField("dt", JDouble(dt)) :: JField("tRefresh", JDouble(tRefresh)) ::
        JField("vehiculos", vehiculos) :: JField("velocidad", velocidad) ::
        JField("proporciones", proporciones) :: JField("semaforos", semaforos) :: Nil) =>
        new ParametrosSimulacion(dt.doubleValue, tRefresh.doubleValue, vehiculos.extract[MinimoMaximo], velocidad.extract[MinimoMaximo],
          proporciones.extract[Proporciones], semaforos.extract[Semaforos])
      case JObject(JField("dt", JInt(dt)) :: JField("tRefresh", JDouble(tRefresh)) ::
        JField("vehiculos", vehiculos) :: JField("velocidad", velocidad) ::
        JField("proporciones", proporciones) :: JField("semaforos", semaforos) :: Nil) =>
        new ParametrosSimulacion(dt.doubleValue, tRefresh.doubleValue, vehiculos.extract[MinimoMaximo], velocidad.extract[MinimoMaximo],
          proporciones.extract[Proporciones], semaforos.extract[Semaforos])
      case JObject(JField("dt", JDouble(dt)) :: JField("tRefresh", JInt(tRefresh)) ::
        JField("vehiculos", vehiculos) :: JField("velocidad", velocidad) ::
        JField("proporciones", proporciones) :: JField("semaforos", semaforos) :: Nil) =>
        new ParametrosSimulacion(dt.doubleValue, tRefresh.doubleValue, vehiculos.extract[MinimoMaximo], velocidad.extract[MinimoMaximo],
          proporciones.extract[Proporciones], semaforos.extract[Semaforos])
      case JObject(JField("dt", JInt(dt)) :: JField("tRefresh", JInt(tRefresh)) ::
        JField("vehiculos", vehiculos) :: JField("velocidad", velocidad) ::
        JField("proporciones", proporciones) :: JField("semaforos", semaforos) :: Nil) =>
        new ParametrosSimulacion(dt.doubleValue, tRefresh.doubleValue, vehiculos.extract[MinimoMaximo], velocidad.extract[MinimoMaximo],
          proporciones.extract[Proporciones], semaforos.extract[Semaforos])
      case x => throw new MappingException("Can't convert " + x + " to ParametrosSimulacion")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: ParametrosSimulacion => JObject(x.getAtributosJson)
  }
}