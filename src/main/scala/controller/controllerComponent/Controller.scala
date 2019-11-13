package controller.controllerComponent

import controller.ControllerTrait
import model.playerComponent.card.CardTrait
import model.{Model, ModelTrait}

class Controller extends ControllerTrait {

  val model: ModelTrait = Model()

  override def printBoard(): Unit = {
    model.getBoard.prettyPrint
  }

  override def createBoard(): Unit = {
    model.createBoard
  }

  override def dragCard(): CardTrait = {
    model.dragCard()
  }
}
