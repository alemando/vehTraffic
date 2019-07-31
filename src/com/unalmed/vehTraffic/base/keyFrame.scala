package com.unalmed.vehTraffic.base

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import com.unalmed.vehTraffic.simulacion.Simulacion

class keyFrame extends KeyListener{
  
  def keyPressed(event:KeyEvent ) {

    if(event.getKeyCode == 116) {
      Simulacion.stop()
      Simulacion.start()
      }
    if(event.getKeyCode == 117) Simulacion.stop()
 
  }
  
  def keyReleased(event:KeyEvent ) {
  }
  
  def keyTyped(event:KeyEvent ) {
  }
}