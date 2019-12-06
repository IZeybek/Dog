package util

import controller.Controller
import model.CardComponent.Card
import model.{Board, Player}

class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(command: Command) = {
    undoStack = command :: undoStack
    command.doStep
  }

  def clear(): Boolean = {
    undoStack = Nil
    redoStack = Nil
    redoStack == Nil && undoStack == Nil
  }

  def undoStep = {
    undoStack match {
      case Nil =>
      case head :: stack => {
        head.undoStep
        undoStack = stack
        redoStack = head :: redoStack
      }
    }
  }

  def redoStep = {
    redoStack match {
      case Nil =>
      case head :: stack => {
        head.redoStep
        redoStack = stack
        undoStack = head :: undoStack
      }
    }
  }
}


trait Command {

  def doStep: Unit

  def undoStep: Unit

  def redoStep: Unit

}

class SolveCommand(controller: Controller) extends Command {
  var mementoBoard: Board = controller.board
  var mementoPlayer: Array[Player] = controller.player
  var mementoCardDeck: (Array[Card], Int) = controller.cardDeck

  override def undoStep(): Unit = {
    updateSolveCommand
  }

  def updateSolveCommand(): Unit = {
    val newMementoBoard: Board = controller.board
    val newMementoPlayer: Array[Player] = controller.player
    val newMementoCardDeck: (Array[Card], Int) = controller.cardDeck

    controller.replaceController(mementoBoard, mementoPlayer, mementoCardDeck)

    mementoBoard = newMementoBoard
    mementoPlayer = newMementoPlayer
    mementoCardDeck = newMementoCardDeck
  }

  override def redoStep(): Unit = {
    updateSolveCommand
  }

  override def doStep: Unit = Unit
}
