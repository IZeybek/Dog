package dog.util

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.StateComponent.GameState

class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(command: Command): Unit = {
    undoStack = command :: undoStack
    //    command.doStep
  }

  def clear: Boolean = {
    undoStack = Nil
    redoStack = Nil
    redoStack == Nil && undoStack == Nil
  }

  def undoStep(): Unit = {
    undoStack match {
      case Nil =>
      case head :: stack =>
        head.undoStep()
        undoStack = stack
        redoStack = head :: redoStack
    }
  }

  def redoStep(): Unit = {
    redoStack match {
      case Nil =>
      case head :: stack =>
        head.redoStep()
        redoStack = stack
        undoStack = head :: undoStack
    }
  }
}


trait Command {

  //  def doStep: Unit

  def undoStep(): Unit

  def redoStep(): Unit

}

class SolveCommand(controller: ControllerTrait) extends Command {

  var gameState: GameState = controller.gameState

  override def undoStep(): Unit = {
    updateSolveCommand()
  }

  private def updateSolveCommand(): Unit = {

    val newGameState: GameState = controller.gameState
    controller.gameState = gameState
    gameState = newGameState
  }

  override def redoStep(): Unit = {
    updateSolveCommand()
  }

  //  override def doStep: Unit = ???
}
