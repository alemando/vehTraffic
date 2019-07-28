package com.unalmed.vehTraffic.util

import net.liftweb.json.JsonAST.JField

trait SerializableJson {
  
  def getAtributosJson: List[JField]
}