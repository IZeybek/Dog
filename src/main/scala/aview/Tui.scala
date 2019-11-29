package aview

import controller.Controller
import util.Observer


class Tui(controller: Controller) extends Observer {
  controller.add(this)


  def automatedSequenceForTesting(): Unit = {

    controller.createSetPlayer(List("a1", "a2", "a3", "a4"))
    controller.createCardDeck
    controller.setNewBoard(20)
    controller.initPlayerHandCards(6)
    controller.toStringPlayerHands()
    //      println(controller.playCard(0))
    //      controller.toStringPlayerHands()
  }

  def processInput(input: String): String = {
    var result: String = ""

    input.split("\\s+").toList match {
      case "n" :: "player" :: player =>
        if (player.nonEmpty) {
          controller.createSetPlayer(player)
          result = if (player.size > 1)
            s"created ${player.size} players"
          else
            "created 1 player"
        } else {
          result = "no players created"
        }
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
          case playerNum :: pieceNum :: cardNum :: Nil =>
            controller.parseCard(playerNum, pieceNum, cardNum)
            result = f"moved player $playerNum"
          case _ =>
        }
    }
    result
  }

  override def update: Unit = {
    println(controller.toStringBoard)
    println(controller.toStringPlayerHands())
  }

}

