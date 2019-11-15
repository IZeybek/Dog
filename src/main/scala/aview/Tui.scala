package aview

import controller.controllerComponent.Controller

import scala.io.StdIn

class Tui() {
  val controller = new Controller(Array("Player1", "Player2", "Player3", "Player4"))

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result: String = ""

    commands(0) match {
      case "n" =>
        commands(1) match {
          case "board" =>
            controller.createBoard
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
      //        controller.move(controller.player(0), 6, 0)
      case "p" =>
        print(controller.printBoard)
        result = "printed board"
      case _ => result = ""
    }
    return result
  }


}


