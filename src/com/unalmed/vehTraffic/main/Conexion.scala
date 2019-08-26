package com.unalmed.vehTraffic.main
import org.neo4j.driver.v1._
import com.unalmed.vehTraffic.mallaVial.Interseccion
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.TipoVia
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.CamaraFotoDeteccion

object Conexion {
  
  val url = "bolt://localhost/7687"
  val user = "neo4j" //Usuario por defecto
  val pass = "123" //Pass de la bd activa

  
  def getInterseccionesViasCamaras() = {
    val (driver, session) = getSession()
    val intersecciones = getIntersecciones(session)
    val (vias, camaras) = getVias(session, intersecciones)
    session.close()
    driver.close()
    
    (intersecciones, vias, camaras)
  }
  def getSession(): (Driver, Session) = {
    val driver = GraphDatabase.driver(url, AuthTokens.basic(user, pass))
    val session = driver.session
    (driver, session)
  }
  
  def getIntersecciones(session: Session) = {
    
    val script = s"MATCH (x:Interseccion) RETURN x"
    val result = session.run(script)
    val intersecciones = ArrayBuffer.empty[Interseccion]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodo = values.get(0) //Interseccion
      intersecciones += Interseccion(nodo.get("longitud").asInt(), nodo.get("latitud").asInt(), Some(nodo.get("nombre").asString()))
    }
    
    intersecciones
  }
  
  def getVias(session: Session, listaIntersecciones: ArrayBuffer[Interseccion]) = {
    val script = s"match(n:Via) match(n)-[:ES_ORIGEN]->(o) match(n)-[:ES_FIN]->(f) OPTIONAL match (n)-[:FOTO_DETECTA]->(c) return n,o,f,c"
    val result = session.run(script)
    val vias = ArrayBuffer.empty[Via]
    val camaras = ArrayBuffer.empty[CamaraFotoDeteccion]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodoVia = values.get(0) //Via
      val nodoInterseccionOrigen = values.get(1) //Interseccion origen
      val nodoInterseccionFin = values.get(2) //Interseccion fin
      val nodoCamara = values.get(3) //Camara
      
      val interseccionOrigen = listaIntersecciones.find(_ == Punto(nodoInterseccionOrigen.get("longitud").asInt(), nodoInterseccionOrigen.get("latitud").asInt())).get
      val interseccionFin = listaIntersecciones.find(_ == Punto(nodoInterseccionFin.get("longitud").asInt(), nodoInterseccionFin.get("latitud").asInt())).get
      
      var sentido: Sentido = Sentido.dobleVia
      if(nodoVia.get("sentido").asString() == "unaVia" ){
        sentido = Sentido.unaVia
      } 
      val via = Via(interseccionOrigen, interseccionFin, nodoVia.get("velocidadMaxima").asInt(), TipoVia(nodoVia.get("tipo").asString()), 
          sentido, nodoVia.get("numero").asString(), Some(nodoVia.get("nombre").asString()))
      vias += via
      
      var camara: Option[CamaraFotoDeteccion] = None
      if (!nodoCamara.isNull()){
        camara = Some(CamaraFotoDeteccion(via, nodoCamara.get("longitudUbicacion").asInt()))
        camaras += camara.get
      }
      via.fotomulta = camara
      
    }
    (vias,camaras)
  }
  
}