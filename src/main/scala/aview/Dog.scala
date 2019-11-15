package aview

import scala.io.StdIn

object Dog {

  def main(args: Array[String]): Unit = {

    val tui = new Tui
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
