package com.unalmed.vehTraffic.main
import org.neo4j.driver.v1._
import com.unalmed.vehTraffic.mallaVial.Interseccion
import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.mallaVial.Punto
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.TipoVia
import com.unalmed.vehTraffic.mallaVial.Sentido
import com.unalmed.vehTraffic.mallaVial.CamaraFotoDeteccion
import com.unalmed.vehTraffic.mallaVial.Semaforo
import com.unalmed.vehTraffic.simulacion.Simulacion
import com.unalmed.vehTraffic.grafo.Viaje
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.vehiculo.Comparendo
import com.unalmed.vehTraffic.dimension.Angulo
import com.unalmed.vehTraffic.dimension.Velocidad
import com.unalmed.vehTraffic.grafo.GrafoVia
import scala.collection.mutable.Queue
import com.unalmed.vehTraffic.vehiculo.Comparendo

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
  private def getSession(): (Driver, Session) = {
    val driver = GraphDatabase.driver(url, AuthTokens.basic(user, pass))
    val session = driver.session
    (driver, session)
  }
  
  private def getIntersecciones(session: Session) = {
    
    val script = s"MATCH (x:Interseccion) RETURN x"
    val result = session.run(script)
    val intersecciones = ArrayBuffer.empty[Interseccion]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodo = values.get(0) //Interseccion
      intersecciones += Interseccion(nodo.get("longitud").asDouble(), nodo.get("latitud").asDouble(), Some(nodo.get("nombre").asString()))
    }
    
    intersecciones
  }
  
  private def getVias(session: Session, listaIntersecciones: ArrayBuffer[Interseccion]) = {
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
      
      val interseccionOrigen = listaIntersecciones.find(_ == Punto(nodoInterseccionOrigen.get("longitud").asDouble(), nodoInterseccionOrigen.get("latitud").asDouble())).get
      val interseccionFin = listaIntersecciones.find(_ == Punto(nodoInterseccionFin.get("longitud").asDouble(), nodoInterseccionFin.get("latitud").asDouble())).get
      
      var sentido: Sentido = Sentido.dobleVia
      if(nodoVia.get("sentido").asString() == "unaVia" ){
        sentido = Sentido.unaVia
      } 
      val via = Via(interseccionOrigen, interseccionFin, nodoVia.get("velocidadMaxima").asDouble(), TipoVia(nodoVia.get("tipo").asString()), 
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
  
  def guardarSimulacion(simulacion: Simulacion) = {
    val (driver, session) = getSession()
    limpiarNeo4j(session)
    insertarSimulacion(session, simulacion.t)
    insertarSemaforos(session, simulacion.listaSemaforos)
    insertarVehiculos(session, simulacion.listaVehiculos)
    insertarViajes(session, simulacion.listaViajes)
    insertarComparendos(session, simulacion.listaComparendos)
    session.close()
    driver.close()
  }
  
  private def limpiarNeo4j(session: Session){
    val script = "match(si:Simulacion) match(s:Semaforo) match(v:Vehiculo) match(vi:Viaje) optional match(c:Comparendo) detach delete si,s,v,vi,c"
    val result = session.run(script)
  }
  
  private def insertarSimulacion(session: Session, tiempoSimulacion: Int) = {
    val script = s"CREATE(x:Simulacion{t:$tiempoSimulacion})"
    val result = session.run(script)
  }
  
  private def insertarSemaforos(session: Session, listaSemaforos: ArrayBuffer[Semaforo]) = {
    
    listaSemaforos.foreach(semaforo =>{
      val semaforoInterseccion = semaforo.interseccion
      val semaforoViaFin = semaforo.via.fin
      val semaforoViaOrigen = semaforo.via.origen
      val script = s"""MATCH(:Interseccion{latitud:${semaforoViaFin.latitud},longitud:${semaforoViaFin.longitud}})
      <-[:ES_FIN]-(v:Via)-[:ES_ORIGEN]->(:Interseccion{latitud:${semaforoViaOrigen.latitud},longitud:${semaforoViaOrigen.longitud}})
      MATCH(i:Interseccion{latitud:${semaforoInterseccion.latitud}, longitud:${semaforoInterseccion.longitud}})
      CREATE(s:Semaforo{tiempoVerde:${semaforo.tiempoVerde}, tiempoAmarillo:${semaforo.tiempoAmarillo}}),(s)-[:SEMAFORO_VIA]->(v), (s)-[:SEMAFORO_INTERSECCION]->(i)"""
      val result = session.run(script)
    })
    
  }
  
  private def insertarVehiculos(session: Session, listaVehiculos: ArrayBuffer[Vehiculo]) = {
    listaVehiculos.foreach(vehiculo =>{
      val script = s"""CREATE(:Vehiculo{placa:'${vehiculo.placa}', x:${vehiculo.posicion.x}, y:${vehiculo.posicion.y},
        magnitud:${vehiculo.velocidad.magnitud}, direccion:${vehiculo.velocidad.direccion.valor}, velocidadCrucero:${vehiculo.velocidadCrucero}, 
        aceleracion:${vehiculo.aceleracion}, tipo:'${vehiculo.getClass.getSimpleName}'})"""
      val result = session.run(script)
    })
  }
  
  private def insertarViajes(session: Session, listaViajes: ArrayBuffer[Viaje]) = {
    listaViajes.foreach(viaje =>{
      val viajeOrigen = viaje.origen
      val viajeDestino = viaje.destino
      val script = s"""MATCH(v:Vehiculo{placa:'${viaje.vehiculo.placa}'})
        MATCH(o:Interseccion{latitud:${viajeOrigen.latitud}, longitud:${viajeOrigen.longitud}})
        MATCH(d:Interseccion{latitud:${viajeDestino.latitud}, longitud:${viajeDestino.longitud}})
        CREATE(vi:Viaje),(vi)-[:ORIGEN_VIAJE]->(o), (vi)-[:DESTINO_VIAJE]->(d), (vi)-[:VIAJE_DE]->(v)"""
      val result = session.run(script)
      
      var iv = 1
      viaje.ruta.foreach(via =>{
        val viaFin = via.fin
        val viaOrigen = via.origen
        val script1 = s"""MATCH(vi:Viaje)-[:VIAJE_DE]->(:Vehiculo{placa:'${viaje.vehiculo.placa}'})
          MATCH(:Interseccion{latitud:${viaFin.latitud},longitud:${viaFin.longitud}})
          <-[:ES_FIN]-(v:Via)-[:ES_ORIGEN]->(:Interseccion{latitud:${viaOrigen.latitud},longitud:${viaOrigen.longitud}})
          CREATE(vi)<-[:RUTA_DE{orden:$iv}]-(v)"""
        val result1 = session.run(script1)
        iv += 1
      })
      
      var iin = 1
      viaje.intersecciones.foreach(interseccion =>{ 
        val script1 = s"""MATCH(vi:Viaje)-[:VIAJE_DE]->(:Vehiculo{placa:'${viaje.vehiculo.placa}'})
          MATCH(i:Interseccion{latitud:${interseccion.latitud},longitud:${interseccion.longitud}})
          CREATE(vi)<-[:INTERSECCION_DE{orden:$iin}]-(i)"""
        val result1 = session.run(script1)
        iin += 1
      })
      
    })
  }
  
  private def insertarComparendos(session: Session, listaComparendos: ArrayBuffer[Comparendo]) = {
    listaComparendos.foreach(comparendo =>{
      val script = s"""match(v:Vehiculo{placa:'${comparendo.vehiculo.placa}'})
        CREATE(c:Comparendo{velocidadVehiculo:${comparendo.velocidadVehiculo}, maximaVelocidad:${comparendo.maximaVelocidad}}),
        (c)-[:COMPARENDO_PARA]->(v)"""
      val result = session.run(script)
    })
    
  }
  
  
  def cargarSimulacion(listaVias: ArrayBuffer[Via], listaIntersecciones: ArrayBuffer[Interseccion], listaCamaraFotoDeteccion: ArrayBuffer[CamaraFotoDeteccion]) = {
    val (driver, session) = getSession()
    val t = getSimulacion(session)
    val listaSemaforos = getSemaforos(session, listaVias, listaIntersecciones)
    val listaVehiculos = getVehiculos(session)
    val listaViajes = getViajes(session, listaVias, listaIntersecciones, listaVehiculos)
    val listaComparendos = getComparendos(session, listaVehiculos)
    session.close()
    driver.close()
    Simulacion(listaVias, listaIntersecciones, listaCamaraFotoDeteccion, listaSemaforos, listaVehiculos, listaViajes, listaComparendos, t.get )
    
  }
  
  def comprobarSimulacio() = {
    val (driver, session) = getSession()
    val t = getSimulacion(session)
    session.close()
    driver.close()
    t.isDefined
  }
  
  private def getSimulacion(session: Session): Option[Int] ={
    val script = s"Match(x:Simulacion) return x"
    val result = session.run(script)
    if (result.hasNext()) {
        val nodo = result.next().values().get(0)
        Some(nodo.get("t").asInt())
    } else {
        None
    }
  }
  
  private def getSemaforos(session: Session, listaVias: ArrayBuffer[Via], listaIntersecciones: ArrayBuffer[Interseccion]) = {
    
    val script = """match(s:Semaforo) match(s)-[:SEMAFORO_VIA]->(v) match(s)-[:SEMAFORO_INTERSECCION]->(i) match(v)-[:ES_ORIGEN]->(o)
       match(v)-[:ES_FIN]->(f) return s,i,o,f"""
    val result = session.run(script)
    val semaforos = ArrayBuffer.empty[Semaforo]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodoSemaforo = values.get(0) //Semaforo
      val nodoInterseccion = values.get(1) //interseccion
      val nodoInterseccionOrigen = values.get(2) //interseccion origen
      val nodoInterseccionFin= values.get(3) //interseccion fin
      
      val interseccion = listaIntersecciones.find(_ == Punto(nodoInterseccion.get("longitud").asDouble(), nodoInterseccion.get("latitud").asDouble())).get
      val interseccionOrigen = listaIntersecciones.find(_ == Punto(nodoInterseccionOrigen.get("longitud").asDouble(), nodoInterseccionOrigen.get("latitud").asDouble())).get
      val interseccionFin = listaIntersecciones.find(_ == Punto(nodoInterseccionFin.get("longitud").asDouble(), nodoInterseccionFin.get("latitud").asDouble())).get
      val via = listaVias.find(via=> (via.origen, via.fin) == (interseccionOrigen,interseccionFin)).get
      semaforos += new Semaforo(nodoSemaforo.get("tiempoVerde").asInt(), nodoSemaforo.get("tiempoAmarillo").asInt(), interseccion, via)
    }
    
    semaforos
  }
  
  private def getVehiculos(session: Session) = {
    
    val script = """match(v:Vehiculo) return v"""
    val result = session.run(script)
    val vehiculos = ArrayBuffer.empty[Vehiculo]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodo = values.get(0) //vehiculo
      val punto = Punto(nodo.get("x").asDouble(), nodo.get("y").asDouble())
      val velocidad = Velocidad(nodo.get("magnitud").asDouble(), Angulo(nodo.get("direccion").asDouble()))
      vehiculos += Vehiculo(nodo.get("placa").asString(), punto, velocidad, nodo.get("velocidadCrucero").asDouble(), 
          nodo.get("aceleracion").asDouble(), nodo.get("tipo").asString())
    }
    
    vehiculos
  }
  
  private def getViajes(session: Session, listaVias: ArrayBuffer[Via], listaIntersecciones: ArrayBuffer[Interseccion], 
      listaVehiculos: ArrayBuffer[Vehiculo]) = {
    
    val script = """Match(vi:Viaje) match(vi)-[:VIAJE_DE]->(v:Vehiculo)
      match(d:Interseccion)<-[:DESTINO_VIAJE]-(vi)-[:ORIGEN_VIAJE]->(o:Interseccion) return v,o,d"""
    val result = session.run(script)
    val viajes = ArrayBuffer.empty[Viaje]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodoVehiculo = values.get(0) //Vehiculo
      val nodoInterseccionOrigen = values.get(1) //interseccion origen
      val nodoInterseccionFin= values.get(2) //interseccion fin
      
      val vehiculo = listaVehiculos.find(_.placa == nodoVehiculo.get("placa").asString()).get
      
      val interseccionOrigen = listaIntersecciones.find(_ == Punto(nodoInterseccionOrigen.get("longitud").asDouble(), nodoInterseccionOrigen.get("latitud").asDouble())).get
      val interseccionFin = listaIntersecciones.find(_ == Punto(nodoInterseccionFin.get("longitud").asDouble(), nodoInterseccionFin.get("latitud").asDouble())).get
      
      val script1 = s"""Match(vi:Viaje) match(:Vehiculo{placa:'${vehiculo.placa}'})<-[:VIAJE_DE]-(vi)<-[r:INTERSECCION_DE]-(i:Interseccion) return i,r.orden order by r.orden"""
      val result1 = session.run(script1)
      val intersecciones = Queue[Interseccion]()
      while (result1.hasNext()) {
        val nodo = result1.next().values().get(0)
        val interseccion = listaIntersecciones.find(_ == Punto(nodo.get("longitud").asDouble(), nodo.get("latitud").asDouble())).get
        intersecciones += interseccion
      }
      
      val script2 = s"""Match(vi:Viaje) match(:Vehiculo{placa:'${vehiculo.placa}'})<-[:VIAJE_DE]-(vi)<-[r:RUTA_DE]-(v:Via) match(f)<-[:ES_FIN]-(v)-[:ES_ORIGEN]->(o) 
        return o,f,r.orden order by r.orden"""
      val result2 = session.run(script2)
      val vias = Queue[Via]()
      while (result2.hasNext()) {
        val values = result2.next().values()
        
        val nodoInterseccionOrigen = values.get(0) //interseccion origen
        val nodoInterseccionFin= values.get(1) //interseccion fin
        
        val interseccionOrigen = listaIntersecciones.find(_ == Punto(nodoInterseccionOrigen.get("longitud").asDouble(), nodoInterseccionOrigen.get("latitud").asDouble())).get
        val interseccionFin = listaIntersecciones.find(_ == Punto(nodoInterseccionFin.get("longitud").asDouble(), nodoInterseccionFin.get("latitud").asDouble())).get
        
        val via = listaVias.find(via=> (via.origen, via.fin) == (interseccionOrigen,interseccionFin)).get
        vias += via
      }
      
      
      viajes += Viaje(vehiculo, interseccionOrigen, interseccionFin, vias, intersecciones )
    }
    
    viajes
  }
  
  private def getComparendos(session: Session, listaVehiculos: ArrayBuffer[Vehiculo]) = {
    
    val script = """Match(c:Comparendo) match(c)-[:COMPARENDO_PARA]->(v:Vehiculo) return c, v"""
    val result = session.run(script)
    val comparendos = ArrayBuffer.empty[Comparendo]
    while (result.hasNext()) {
      val values = result.next().values()
      val nodoComparendo = values.get(0) //comparendo
      val nodoVehiculo = values.get(1) //vehiculo
      
      val vehiculo = listaVehiculos.find(_.placa == nodoVehiculo.get("placa").asString()).get
      
      comparendos += new Comparendo(vehiculo, nodoComparendo.get("velocidadVehiculo").asDouble(), nodoComparendo.get("maximaVelocidad").asDouble())
    }
    
    comparendos
  }
  
}