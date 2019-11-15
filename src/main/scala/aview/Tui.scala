package aview

import controller.controllerComponent.Controller

import scala.io.StdIn

class Tui() {
  val controller = new Controller(Array("Player1", "Player2", "Player3", "Player4"))

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result = ""

    commands(0) match {
      case "n" =>
        if (commands.length == 1) {
          commands(1) match {
            case "board" =>
              controller.createBoard
              result = "created a new board"
            case "player" => {
              print("What's your name?\n")
              val playerNames = StdIn.readLine().split("\\s+")
              if (playerNames.size == 4)
                controller.createPlayer(playerNames)
            }
              result = "created new players"
            case _ => result = "creation failed!"
          }
        }
      case "m" =>
        result = "moved a player"
//        controller.move(controller.player(0), 6, 0)
      case "p" =>
        controller.printBoard
        result = "printed board"
      case _ => result = ""
    }
    result
  }


}


