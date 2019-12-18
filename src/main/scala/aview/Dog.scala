package aview

import controller.Component.ControllerTrait
import controller.Component.controllerBaseImpl.Controller

object Dog {

  val controller: ControllerTrait = new Controller()
  val tui = new Tui(controller)
  controller.notifyObservers

  def main(args: Array[String]): Unit = {
    tui.showMenu()

    var input: String = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    do {
      print("\n>> ")
      input = scala.io.StdIn.readLine()
      println(tui.processInput(input))
    } while (input != "exit")
  }
}
