package aview

import controller.controllerComponent.Controller
import model.playerComponent.Player

object Dog {

  def main(args: Array[String]): Unit = {
    val controller = new Controller()
    val tui = new Tui(controller)
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
