package com.unalmed.vehTraffic.frame

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import com.unalmed.vehTraffic.simulacion.Simulacion

class keyFrame extends KeyListener{
  
  def keyPressed(event:KeyEvent ) = {

    if(event.getKeyCode == 116) {
      if (Main.objectSimulacion != null) Main.stop()
      Main.start()
      }
    if(event.getKeyCode == 117) if (Main.objectSimulacion != null) Main.stop()
 
  }
  
  def keyReleased(event:KeyEvent ) {
  }
  
  def keyTyped(event:KeyEvent ) {
  }
}