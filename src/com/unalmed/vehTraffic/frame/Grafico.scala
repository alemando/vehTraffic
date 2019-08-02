package com.unalmed.vehTraffic.frame

//Libreria para frames
import javax.swing.JFrame
//Libreria para lineas
import java.awt.BasicStroke;
//Libreria para colores
import java.awt.Color;
//JfreeChart
import org.jfree.chart.ChartFrame
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.chart.ChartFrame
import org.jfree.chart.axis.ValueAxis
import org.jfree.chart.annotations.XYTextAnnotation

//Figuras geometricas
import java.awt.geom.Ellipse2D
import java.awt.Rectangle
import java.awt.Polygon

import scala.collection.mutable.ArrayBuffer
import com.unalmed.vehTraffic.vehiculo.MotoTaxi
import com.unalmed.vehTraffic.vehiculo.Vehiculo
import com.unalmed.vehTraffic.vehiculo.Moto
import com.unalmed.vehTraffic.vehiculo.Carro
import com.unalmed.vehTraffic.vehiculo.Bus
import com.unalmed.vehTraffic.vehiculo.Camion
import com.unalmed.vehTraffic.mallaVial.Via
import com.unalmed.vehTraffic.mallaVial.Interseccion

object Grafico{
  
  val cuadrado = new Rectangle(-4,-4,6,6)
  
  val circulo = new Ellipse2D.Double(-4,-4,8,10)
  
  val rectangulo = new Rectangle(-2,-4,4,14)
  
  val triangulo = new Polygon(Array(-5,0,5),Array(-5,5,-5),3)
  
  val pentagono = new Polygon(Array(-3,3,5,0,-5),Array(-6,-6,0,4,0),5)
  
  val dataset: XYSeriesCollection = new XYSeriesCollection();
  
  val xyScatterChart: JFreeChart = ChartFactory.createScatterPlot(
  	null, 
  	null, 
  	null, 
  	dataset,
  	PlotOrientation.VERTICAL, false, false, false)
  	
	val plot: XYPlot = xyScatterChart.getXYPlot()
  
	plot.setBackgroundPaint(Color.WHITE)
	plot.getRangeAxis().setVisible(false)
  plot.getDomainAxis().setVisible(false)
	
  val renderer: XYLineAndShapeRenderer = new XYLineAndShapeRenderer()
	renderer.setAutoPopulateSeriesStroke(false)
	renderer.setAutoPopulateSeriesPaint(false)
  renderer.setBaseStroke(new BasicStroke(4))
  renderer.setBasePaint(Color.decode("#cccccc"))
  plot.setRenderer(renderer)
  
  val ventana: ChartFrame = new ChartFrame("vehTraffic", xyScatterChart)
	ventana.setVisible(true)
	ventana.setSize(1300, 700)
	ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  ventana.addKeyListener(new keyFrame())
  
	def iniciarGrafico(vias: ArrayBuffer[Via], intersecciones: ArrayBuffer[Interseccion]) = {
    var c: Int = 0
    vias.foreach(v => {
      dataset.addSeries(new XYSeries((c)))
      renderer.setSeriesShapesVisible(c, false)
      c +=1
    })
    intersecciones.foreach(i => { 
      val anotacion: XYTextAnnotation = new XYTextAnnotation(i.nombre.getOrElse("Desconocida"), i.x, i.y)
    	anotacion.setPaint(Color.decode(i.color))
    	plot.addAnnotation(anotacion)
    })
  }
  
  def removerVehiculos(vehiculos: ArrayBuffer[Vehiculo]) = {
    vehiculos.foreach(ve => {
      dataset.removeSeries(dataset.getSeriesIndex(ve.placa))
    })
    
  }
  
  def iniciarVehiculos(vehiculos: ArrayBuffer[Vehiculo]) = {
    vehiculos.foreach(ve => {
      dataset.addSeries(new XYSeries((ve.placa)))
      val vehiculoIndex = dataset.getSeriesIndex(ve.placa)
      
      renderer.setSeriesShape(vehiculoIndex, figuraGeometrica(ve))
      renderer.setSeriesPaint(vehiculoIndex, Color.decode(ve.recorrido.destino.color))
      
    })
    graficarVehiculos(vehiculos)
    
  }
	
	def figuraGeometrica(n: Vehiculo) = n match {
	  
	  case n: Carro =>  cuadrado
	  
	  case n: Moto =>  circulo
	  
	  case n: Bus =>  rectangulo
	  
	  case n: Camion =>  triangulo
	  
	  case _ =>  pentagono
	  
	}
	
  def graficarVias(vias: ArrayBuffer[Via]) = {
    var c: Int = 0
    vias.foreach(v => {
      val via =  dataset.getSeries(c)
      via.add(v.origen.x, v.origen.y)
	    via.add(v.fin.x, v.fin.y)
      c +=1
    })
  }
  
  def graficarVehiculos(vehiculos: ArrayBuffer[Vehiculo]) = {
    vehiculos.foreach(ve => {
      val vehiculo =  dataset.getSeries(ve.placa)
      vehiculo.clear()
      vehiculo.add(ve.posicion.x, ve.posicion.y)
    })
  }
}