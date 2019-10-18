import model.Player

import scala.io.Source

object Dog {

  def main(args: Array[String]): Unit = {
    val player = Player(scala.io.StdIn.readLine())
    print(player.toString())
  }


  def readText(fileName: String): Unit = {
    for (line <- Source.fromFile(fileName).getLines())
      line.split(" ")
    Source.fromFile(fileName).close()
  }
}
