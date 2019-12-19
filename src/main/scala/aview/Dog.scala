package aview

import aview.gui.Gui
import controller.Controller

object Dog {

  val controller = new Controller()
  val gui = new Gui(controller)

  val tui = new Tui(gui, controller)
  //  controller.notifyObservers

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
