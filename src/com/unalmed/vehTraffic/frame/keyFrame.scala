package com.unalmed.vehTraffic.frame

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import com.unalmed.vehTraffic.main.Main

class keyFrame extends KeyListener{
  
  def keyPressed(event:KeyEvent ) = {

    if(event.getKeyCode == 116) {
      Main.objectSimulacion.stop()
      Main.objectSimulacion.start()
      }
    if(event.getKeyCode == 117) Main.objectSimulacion.stop()
 
  }
  
  def keyReleased(event:KeyEvent ) {
  }
  
  def keyTyped(event:KeyEvent ) {
  }
}