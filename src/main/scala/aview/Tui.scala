package aview

import controller.Controller
import util.Observer

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  def processInput(input: String): String = {
    var result: String = ""

    input.split("\\s+").toList match {
      case "n" :: "player" :: player =>
        controller.createPlayer(player)
        result = s"created players ${player}"
      case "p" :: "card" :: Nil =>
        print(controller.toStringCardDeck)
        result = "printed cards"
      case "p" :: "board" :: Nil =>
        print(controller.toStringBoard)
        result = "printed board"
      case "p" :: Nil =>
        print(controller.toStringCardDeck)
        print(controller.toStringBoard)
        result = "printed game"
      case _ =>
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          case playerNum :: pieceNum :: moveBy :: Nil =>
            controller.movePlayer(playerNum, pieceNum, moveBy)
            result = f"move player $playerNum by $moveBy"
          case _ =>
        }
    }
    result
  }


  override def update: Unit = {
    println(controller.toStringBoard)
  }
}


