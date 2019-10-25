package aview

import java.io.IOException
import java.util.Scanner
import scala.io.StdIn.{readLine, readInt}

object Base {

  def main(args: Array[String]): Unit = {
    var tui = Tui
    var input = ""
    //    tui.printMenu()
    do {
      print("say something ")
      System.out.print(">> ")
      input = readLine
      tui.println(input)
      tui.input(input)
    } while (input != "exit")
  }
}
