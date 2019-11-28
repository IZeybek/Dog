package aview

import controller.Controller
import util.Observer

import scala.util.Random

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  def printHands(): Unit ={
    controller.setNewBoard(64)
    controller.createPlayer(Array("a1","a2","a3","a4"))
    controller.createCardDeck
    controller.initPlayerHandCards(6)
    controller.toStringPlayerHands()
    println(controller.playCard(1))
  }

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result: String = ""

    commands(0) match {
      case "n" =>
        commands(1) match {
          case "board" =>

            controller.setNewBoard(commands(1).toInt)
            result = "created a new board"

          case "player" => {
            if (commands.length == 6) {
              controller.createPlayer(Array(commands(2), commands(3), commands(4), commands(5)))
              result = "created new players"
            } else {
              result = "creation failed!"
            }
          }
          case _ => result = "creation failed!"
        }
      case "m" =>
        result = "moved a player"
        val playerNum = Random.nextInt(4)
        val pieceNum = Random.nextInt(4)
        val moveBy = Random.nextInt(5) + 1
        controller.movePlayer(playerNum, pieceNum, moveBy)
      case "p" =>
        print(controller.toStringBoard)
        print(controller.toStringCardDeck)
        result = "printed board"
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


