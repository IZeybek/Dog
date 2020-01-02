import dog.controller.{GameState, InputCard}

//Base handler class
abstract class Handler {
  val successor: Option[Handler]

  def handleEvent(event: Event): Boolean

  //  def handleOwnEvent(event: Event): Boolean
}

case class Event(level: Int, check: Boolean)

//Customer service agent
class LevelOne(val successor: Option[Handler]) extends Handler {
  var work: Boolean = true

  def checkClicked: (InputCard, GameState) => Boolean = (input: InputCard, gameState: GameState) => {
    input.selPieceList.nonEmpty
  }

  override def handleEvent(event: Event): Boolean = {
    event match {
      case e if e.level < 2 => e.check
      case e if e.level > 1 => {
        successor match {
          case Some(h: Handler) => e.check && h.handleEvent(e)
          case None => false
        }
      }
    }
  }
}

class LevelTwo(val successor: Option[Handler]) extends Handler {
  var work: Boolean = true

  override def handleEvent(event: Event): Boolean = {
    event match {
      case e if e.level < 3 => e.check
      case e if e.level > 2 => {
        successor match {
          case Some(h: Handler) => e.check && h.handleEvent(e)
          case None => false
        }
      }
    }
  }
}

object Main {
  def main(args: Array[String]) {
    val supervisor = new LevelTwo(None)
    val agent = new LevelOne(Some(supervisor))
  }
}

