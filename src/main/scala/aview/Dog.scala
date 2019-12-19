package aview

import aview.gui.Gui
import controller.Component.ControllerTrait
import controller.Component.controllerBaseImpl.Controller

object Dog {

  val controller: ControllerTrait = new Controller()
  val tui = new Tui(controller)
  val gui = new Gui(controller)
  controller.notifyObservers

  def main(args: Array[String]): Unit = {
    tui.showMenu()
    gui.main(args)

    var input: String = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    do {
      print("\n>> ")
      input = scala.io.StdIn.readLine()
      println(tui.processInput(input))
    } while (input != "exit")
  }
}
