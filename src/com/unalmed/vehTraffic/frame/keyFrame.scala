package com.unalmed.vehTraffic.frame

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import com.unalmed.vehTraffic.main.Main

class keyFrame extends KeyListener{
  
  def keyPressed(event:KeyEvent ) = {
    //f1
    if(event.getKeyCode == 112) {
      if (Main.objectSimulacion != null) Main.stop()
      Main.start()
    }
    //f2
    if(event.getKeyCode == 113) {
      if (Main.objectSimulacion != null) Main.stop()
      Main.start()
    }
    //f5
    if(event.getKeyCode == 116) {
      if (Main.objectSimulacion != null) Main.stop()
      Main.start()
    }
    //f6
    if(event.getKeyCode == 117) if (Main.objectSimulacion != null) Main.stop()
 
  }
  
  def keyReleased(event:KeyEvent ) {
  }
  
  def keyTyped(event:KeyEvent ) {
  }
}