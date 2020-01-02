import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.{GameState, InputCard, InputCardMaster}
import dog.model.BoardComponent.boardBaseImpl.Board

//Base handler class
abstract class Handler {
  val successor: Option[Handler]

  def handleEvent(event: Event): Boolean
}

case class Event(level: Int,
                 check: (GameState, InputCard) => Boolean,
                 gameState: GameState,
                 inputCard: InputCard)

//Customer service agent
class LevelOne(val successor: Option[Handler]) extends Handler {
  override def handleEvent(event: Event): Boolean = {
    event match {
      case event if event.level > 1 =>
        println(event.level)
        (event check(event.gameState, event.inputCard)) && (successor match {
          case Some(h: Handler) => h.handleEvent(event)
          case None => true
        })
      case event if event.level < 2 =>
        println(event.level)
        event.check(event.gameState, event.inputCard)
    }
  }
}

class LevelTwo(val successor: Option[Handler]) extends Handler {
  override def handleEvent(event: Event): Boolean = {
    event match {
      case event if event.level > 2 =>
        println(event.level)
        (event check(event.gameState, event.inputCard)) && (successor match {
          case Some(h: Handler) => h.handleEvent(event)
          case None => true
        })
      case event if event.level < 3 =>
        println(event.level)
        event.check(event.gameState, event.inputCard)
    }
  }
}

object Event {

  val checkClicked: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    inputCard.selPieceList.nonEmpty
  }
  val checkPlayer: (GameState, InputCard) => Boolean = (gameState: GameState, inputCard: InputCard) => {
    inputCard.actualPlayer == -1
  }

  def setStrategy(callback: (GameState, InputCard) => Boolean,
                  gameState: GameState,
                  inputCard: InputCard): Boolean = {
    callback(gameState, inputCard)
  }
}

object Main {
  def main(args: Array[String]) {
    val checkPlayer = new LevelTwo(None)
    val checkClicked = new LevelOne(Some(checkPlayer))

    val controller = new Controller(new Board(20))
    val gameState: GameState = controller.gameState
    val inputCard: InputCard = InputCardMaster.UpdateCardInput().withPieceNum(1 :: Nil).buildCardInput()

    val events: List[Event] = List(
      Event(0, Event.checkClicked, gameState, inputCard),
      Event(2, Event.checkClicked, gameState, inputCard),
      Event(1, Event.checkPlayer, gameState, inputCard)
    )

    events foreach { e: Event =>
      println(checkClicked.handleEvent(e))
    }
  }
}
