package aview

import model.Main.Player

object Base {

  def main(args: Array[String]): Unit = {
    val tui = new Tui
    var input = ""
    print("Your name is ")
    val player = Player(scala.io.StdIn.readLine(), null)
    print(f"Welcome ${Console.UNDERLINED}${player.toString()}${Console.RESET}! ")
    do {

      System.out.println("What dou you want to do? >> ")
      input = scala.io.StdIn.readLine()
      tui.print(input)
      println(f"Oh, so you want to ${tui.input(input)}")
    } while (input != "exit")
  }
}
