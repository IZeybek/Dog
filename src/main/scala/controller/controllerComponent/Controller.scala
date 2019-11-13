package controller.controllerComponent

import controller.ControllerTrait
import model.playerComponent.card.CardTrait
import model.{Model, ModelTrait}

class Controller extends ControllerTrait {

  val model: ModelTrait = Model()

  override def createPlayer(s: String): Unit = {
    model.createPlayer(s)
  }

  override def createBoard(): Unit = {
    model.createBoard
  }

  override def printBoard(): Unit = {
    model.getBoard.prettyPrint
  }

  override def dragCard(): CardTrait = {
    model.dragCard
  }

  override def printCards(): Unit = {
    model.printCard
  }
}
