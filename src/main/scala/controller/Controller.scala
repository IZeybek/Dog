package controller

import model.Main.Model
import model.ModelTrait

class Controller extends ControllerTrait{

  val model: ModelTrait = Model()

  override def printBoard(): Unit = {
    model.getBoard.prettyPrint()
  }
}
