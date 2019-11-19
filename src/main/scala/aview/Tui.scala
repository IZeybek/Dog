package aview

import controller.Controller
import util.Observer

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  def input(input: String): String = {
    val commands = input.split("\\s+")
    var result: String = ""

    commands(0) match {
      case "n" =>
        commands(1) match {
          case "board" =>
            controller.setNewBoard
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
        print(controller.toStringBoard())
        result = "printed board"
      case _ => result = ""
    }
    result
  }

  override def update: Unit = println(controller.toStringBoard())
}


