package aview

import controller.controllerComponent.Controller

class Tui(controller: Controller) {

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result = ""

    commands(0) match {
      case "p" =>
        result = "print your board"
        controller.printBoard()
      case "create" =>
        result = "create a new board"
        controller.createBoard()
      case _ => result = ""
    }
    result
  }


}


