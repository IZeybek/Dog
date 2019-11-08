package aview

import controller.{Controller, ControllerTrait}

class Tui() {
  def print(input: String): Unit = {
    println(input)
  }


  val controller: ControllerTrait = new Controller

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result = ""

    commands(0) match {
      case "set" => result = "not developed"
      case "p" =>
        result = "print your board"
        controller.printBoard()
      case "s" => result = "search for a cell?"
      case _ => result = "do absolutely nothing"
    }
    result
  }

}


