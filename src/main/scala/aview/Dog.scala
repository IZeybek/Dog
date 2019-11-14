package aview

import controller.ControllerTrait
import controller.controllerComponent.Controller

object Dog {

  def main(args: Array[String]): Unit = {
    val controller = new Controller

    val tui = new Tui(controller)
    var input = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    tui.input("print")
    do {
      System.out.println("What dou you want to do? >> ")
      input = scala.io.StdIn.readLine()
      println(f"${tui.input(input)}")
    } while (input != "exit")

  }
}
