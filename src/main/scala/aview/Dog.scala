package aview

import controller.Controller

object Dog {

  val controller = new Controller()
  val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {


    var input = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    tui.input("n board")
    tui.input("n player Isy Josy Isa Marco")
    tui.input("p")
    do {
      System.out.println("\nprompt >> ")
      input = scala.io.StdIn.readLine()


      println(tui.input(input))
    } while (input != "exit")
  }
}
