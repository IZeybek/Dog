package dog.model


import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.{GameState, InputCard, InputCardMaster}
import dog.model.BoardComponent.boardBaseImpl.Board

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

  val checkHandCards: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => gameState.actualPlayer.cardList.nonEmpty

  val checkPiecesOnBoard: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    var isTrue: Boolean = false
    gameState.actualPlayer.piece.foreach(x => if (x._2.pos != gameState.actualPlayer.homePosition) {
      isTrue = true
    })
    isTrue
  }
  val checkCardPlayOnHand: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    var isTrue: Boolean = false
    gameState.actualPlayer.cardList.foreach(x => (0 until 3).foreach(y => if (x.parse(y).task == "play") isTrue = true))
    isTrue
  }
  val checkWon: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    true
  }
  val checkSelected: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    var isTrue: Boolean = false
    inputCard.otherPlayer match {
      case -1 => isTrue = inputCard.selPieceList.head != -1
      case _ => isTrue = inputCard.selPieceList.head != -1 && inputCard.selPieceList(1) != -1
    }
    isTrue
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
      case 0 => (checkHandCards, Some("check if hand cards "))
      case 1 => (checkPiecesOnBoard, Some("check pieces "))
      case 2 => (checkCardPlayOnHand, Some("check there is a card play "))
      case 3 => (checkWon, Some("check if won "))
      case 4 => (checkSelected, Some("check if piece is selected"))
      case _ => throw new UnsupportedOperationException("unsupported level")
    }
  }
}


object Main {
  def main(args: Array[String]) {
    val pro: Level = new Level(None, 2)
    val advanced: Level = new Level(Some(pro), 1)
    val basic: Level = new Level(None, 0)

    Event.setAttributes(new Controller(new Board(20)).gameState, InputCardMaster.UpdateCardInput()
      .withPieceNum(1 :: Nil)
      .buildCardInput())

    val events: List[Event] = List(
      Event.setStrategy(0),
      Event.setStrategy(1),
      Event.setStrategy(2),
      Event.setStrategy(3),
      Event.setStrategy(4),
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
