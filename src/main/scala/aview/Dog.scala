package aview

import controller.controllerComponent.Controller

object Dog {

  def main(args: Array[String]): Unit = {

    val tui = new Tui
    var input = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    tui.input("n board")
    tui.input("n player")
    tui.input("p")
    do {
      System.out.println("\npromtpt >> ")
      input = scala.io.StdIn.readLine()
      println(f"${tui.input(input)}")
    } while (input != "exit")
  }
}
