package aview

import controller.controllerComponent.Controller

import scala.io.StdIn

class Tui(controller: Controller) {

  def input(input: String): String = {
    val commands = input.split(" ")
    var result = ""

    commands(0) match {
      case "board" =>
        result = "print board"
        controller.printBoard()
      case "create" =>
        if (commands.length > 1) {
          commands(1) match {
            case "board" =>
              result = "create a new board"
              controller.createBoard
            case "player" => {
              result = "create a new player\n"
              print("What's your name?\n")
              controller.createPlayer(StdIn.readLine().split("\\s+")(0))
            }
            case _ => result = "create nothing"
          }
        }
      case "cards" =>
        result = "print your hand cards"
        controller.printCards
      case "drag" =>
        result = "drag a card"
        controller.dragCard
      case _ => result = ""
    }
    result
  }


}


