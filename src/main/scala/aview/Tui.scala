package aview

import controller.controllerComponent.Controller

import scala.io.StdIn

class Tui(controller: Controller) {


  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result = ""

    commands(0) match {
      case "p" =>
        result = "print board"
        controller.printBoard
      case "n" =>
        if (commands.length > 1) {
          commands(1) match {
            case "board" =>
              result = "create a new board"
              controller.createBoard(4)
            case "player" => {
              result = "create a new player\n"
              print("What's your name?\n")
              controller.createPlayer(StdIn.readLine().split("\\s+"))
            }
            case _ => result = "create nothing"
          }
        }
      case _ => result = ""
    }
    result
  }


}


