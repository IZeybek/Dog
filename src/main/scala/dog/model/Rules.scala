package dog.model


import dog.controller.Chain.{gameState, inputCard}
import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.{GameState, InputCard, InputCardMaster}
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.util.SelectedState

import scala.util.{Failure, Success, Try}

//Base handler class
abstract class Handler {
  val successor: Option[Handler]
  val level: Int

  def handleEvent(event: Event): Unit
}

case class Event(level: Int,
                 check: (GameState, InputCard) => Boolean,
                 gameState: GameState,
                 inputCard: InputCard,
                 message: Option[String])


class Level(val successor: Option[Handler], val level: Int) extends Handler {
  override def handleEvent(event: Event): Unit = {
    if (event.level <= level) {
      if (!event.check(event.gameState, event.inputCard)) {
        throw new Exception(s"event ${
          event.message match {
            case Some(m) => m
            case None => ""
          }
        }at level ${event.level} failed")
      }
    } else {
      successor match {
        case Some(h) =>
          h.handleEvent(event)
        case None =>
          throw new Exception(s"could not handle event ${
            event.message match {
              case Some(m) => m
              case None => ""
            }
          }at level ${event.level}")
      }
    }
  }
}


object Event {

  //checkers

  val checkWon: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    true
  }

  val checkHandCards: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => gameState.actualPlayer.cardList.nonEmpty

  val checkPiecesOnBoardAndPlayable: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    var isPieceOnBoard: Boolean = false
    gameState.actualPlayer.piece.foreach(x => if (gameState.board.cell(x._2.pos).isFilled) {
      isPieceOnBoard = true
    })
    println("isPieceOnBoard?: " + isPieceOnBoard)
    var isPlayable: Boolean = false
    println("isPlayable?: " + isPlayable)
    gameState.actualPlayer.cardList.foreach(x => if (x.task.contains("play") || x.task.contains("joker")) isPlayable = true)
    isPieceOnBoard || isPlayable
  }

  val checkSelected: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    var isSelected: Boolean = false
    SelectedState.state match {
      case SelectedState.nothingSelected => if (inputCard.selectedCard.task.contains("play") || inputCard.selectedCard.task.contains("joker")) isSelected = true
      case SelectedState.ownPieceSelected => if (!inputCard.selectedCard.task.contains("swap")) isSelected = true else isSelected = false
      case SelectedState.otherPieceSelected => if (inputCard.selectedCard.task.contains("swap")) isSelected = true else isSelected = false
    }
    isSelected
  }
  var gameState: GameState = _
  var inputCard: InputCard = _

  def setAttributes(setGameState: GameState, setInputCard: InputCard): Unit = {
    gameState = setGameState
    inputCard = setInputCard
  }

  def setStrategy(level: Int): Event = {
    val modeAndMsg: ((GameState, InputCard) => Boolean, Option[String]) = getLogic(level)
    Event(level, modeAndMsg._1, gameState, inputCard, modeAndMsg._2)
  }

  def getLogic(mode: Int): ((GameState, InputCard) => Boolean, Option[String]) = {
    mode match {
      case 0 => (checkHandCards, Some("check hand cards size are non zero"))
      case 1 => (checkPiecesOnBoardAndPlayable, Some("check available pieces onBoard "))
      //      case 2 => (checkWon, Some("check if won "))
      case 2 => (checkSelected, Some("check if piece is selected"))
      case _ => throw new UnsupportedOperationException("unsupported level")
    }
  }
}

object CheckRules {

  def checkLevels(gameState: GameState, inputCard: InputCard): Boolean = {
    val pro: Level = new Level(None, 2)
    val advanced: Level = new Level(Some(pro), 1)
    val basic: Level = new Level(None, 0)

    Event.setAttributes(gameState, inputCard)

    val events: List[Event] = List(
      Event.setStrategy(0),
      Event.setStrategy(1),
      Event.setStrategy(2),
    )
    var isValid = true
    events foreach { e: Event =>
      Try(basic.handleEvent(e)) match {
        case Success(value) => isValid = isValid && true
        case Failure(exception) =>
          println(exception)
          isValid = isValid && false
      }
    }
    isValid
  }
}


object Main {
  def main(args: Array[String]) {
    val pro: Level = new Level(None, 2)
    val advanced: Level = new Level(Some(pro), 1)
    val basic: Level = new Level(Some(advanced), 0)

    Event.setAttributes(new Controller(new Board(20)).gameState, InputCardMaster.UpdateCardInput()
      .withPieceNum(1 :: Nil)
      .buildCardInput())

    val events: List[Event] = List(
      Event.setStrategy(0),
      Event.setStrategy(1),
      Event.setStrategy(2),
    )

    events foreach { e: Event =>
      Try(basic.handleEvent(e)) match {
        case Success(value) => true
        case Failure(exception) =>
          println(exception)
          false
      }
    }
  }
}
