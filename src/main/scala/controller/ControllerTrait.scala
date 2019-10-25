package controller

import model.Player

trait ControllerTrait {

  def create(): Boolean

  def setPlayer(player : Player): Boolean

}
