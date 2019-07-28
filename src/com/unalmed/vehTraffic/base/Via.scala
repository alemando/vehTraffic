package com.unalmed.vehTraffic.base

class Via(or:Interseccion,fn:Interseccion,var velocidadMaxima:Double,tipo:TipoVia,sentido:Sentido,val nombre:String,val numero:String)extends Recta {
  //NOTA: Velocidad máxima no sé si sea realmente una "Velocidad" porque cuando ya creamos las instancias de ella, solo ponen un double,yo hice lo mismo
  type T=Interseccion
  var origen=or
  var fin=fn
}