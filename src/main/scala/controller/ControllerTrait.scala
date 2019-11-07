package controller

import model.Main.Player

trait ControllerTrait {

  def create(): Boolean

  def setPlayer(player : Player): Boolean

}
