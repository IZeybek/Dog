package aview

import controller.Controller

object Dog {

  val controller = new Controller()
  val tui = new Tui(controller)
  controller.notifyObservers

  def main(args: Array[String]): Unit = {
    tui.showMenu

    var input: String = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    do {
      print("\n>> ")
      input = scala.io.StdIn.readLine()
      println(tui.processInput(input))
    } while (input != "exit")
  }
}
