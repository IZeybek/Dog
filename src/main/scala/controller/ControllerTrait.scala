package controller

import model.playerComponent.card.CardTrait

trait ControllerTrait {

  def printBoard(): Unit

  def createBoard(): Unit

  def dragCard(): CardTrait
}
