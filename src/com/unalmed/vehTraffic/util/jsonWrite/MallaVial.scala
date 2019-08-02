package com.unalmed.vehTraffic.util.jsonWrite

import net.liftweb.json._
import scala.math.BigInt.int2bigInt
import scala.reflect.ManifestFactory.classType
import com.unalmed.vehTraffic.util.SerializableJson

case class MallaVial(private var _vias: Int, private var _intersecciones: Int, private var _viasUnSentido: Int,
                     private var _viasDobleSentido: Int, private var _velocidadMinima: Int, private var _velocidadMaxima: Int,
                     private var _longitudPromedio: Double, private var _vehiculosEnInterseccion: VehiculosEnInterseccion)
  extends SerializableJson {

  //Getters
  def vias = _vias
  def intersecciones = _intersecciones
  def viasUnSentido = _viasUnSentido
  def viasDobleSentido = _viasDobleSentido
  def velocidadMinima = _velocidadMinima
  def velocidadMaxima = _velocidadMaxima
  def longitudPromedio = _longitudPromedio
  def vehiculosEnInterseccion = _vehiculosEnInterseccion
  
  //Setters
  def vias_= (vias: Int) = _vias = vias
  def intersecciones_= (intersecciones: Int) = _intersecciones = intersecciones
  def viasUnSentido_= (viasUnSentido: Int) = _viasUnSentido = viasUnSentido
  def viasDobleSentido_= (viasDobleSentido : Int) = _viasDobleSentido = viasDobleSentido 
  def velocidadMinima_= (velocidadMinima: Int) = _velocidadMinima = velocidadMinima
  def velocidadMaxima_= (velocidadMaxima: Int) = _velocidadMaxima = velocidadMaxima
  def longitudPromedio_= (longitudPromedio: Double) = _longitudPromedio = longitudPromedio
  def vehiculosEnInterseccion_= (vehiculosEnInterseccion: VehiculosEnInterseccion) = _vehiculosEnInterseccion = vehiculosEnInterseccion

  def getAtributosJson = JField("vias", JInt(vias)) ::
    JField("intersecciones", JInt(intersecciones)) ::
    JField("viasUnSentido", JInt(viasUnSentido)) ::
    JField("viasDobleSentido", JInt(viasDobleSentido)) ::
    JField("velocidadMinima", JInt(velocidadMinima)) ::
    JField("velocidadMaxima", JInt(velocidadMaxima)) ::
    JField("longitudPromedio", JDouble(longitudPromedio)) ::
    JField("vehiculosEnInterseccion", JObject(vehiculosEnInterseccion.getAtributosJson)) :: Nil

}

class MallaVialSerializer extends Serializer[MallaVial] {
  private val MallaVialClass = classOf[MallaVial]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), MallaVial] = {
    case (TypeInfo(MallaVialClass, _), json) => json match {
      case JObject(JField("vias", JInt(vias)) :: JField("intersecciones", JInt(intersecciones)) ::
        JField("viasUnSentido", JInt(viasUnSentido)) :: JField("viasDobleSentido", JInt(viasDobleSentido)) ::
        JField("velocidadMinima", JInt(velocidadMinima)) :: JField("velocidadMaxima", JInt(velocidadMaxima)) ::
        JField("longitudPromedio", JDouble(longitudPromedio)) :: JField("vehiculosEnInterseccion", vehiculosEnInterseccion) :: Nil) =>
        new MallaVial(vias.intValue, intersecciones.intValue, viasUnSentido.intValue, viasDobleSentido.intValue,
          velocidadMinima.intValue, velocidadMaxima.intValue, longitudPromedio, vehiculosEnInterseccion.extract[VehiculosEnInterseccion])
      case x => throw new MappingException("Can't convert " + x + " to MallaVial")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: MallaVial => JObject(x.getAtributosJson)
  }
}