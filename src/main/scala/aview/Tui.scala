package aview

import controller.{Controller, ControllerTrait}

class Tui() {

  val controller: ControllerTrait = new Controller

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result = ""


    commands(0) match {
      case "create" => result = "empty board created"
      case "set" => result = "Player set"
      case "p" => result = "board printed"
      case "s" => result = "player cell can not be found since its not created yet"
      case _ => result = "wrong command! try again"
    }
    result
  }
  def initGame() : Boolean ={
    controller.createBoard()
  }
}


