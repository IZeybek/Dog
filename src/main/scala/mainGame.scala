import model.Player

import scala.io.Source

case class mainGame(players: Array[Player]) {

  val parse: String => Unit = (x: String) => parser(x)

  def gameStart(): Unit = {
  }

  def readText(fileName: String): Unit = {
    for (line <- Source.fromFile(fileName).getLines())
      line.split(" ").foreach(parse)
    Source.fromFile(fileName).close()
  }

  def parser(input: String): Unit = {
    if (input contains "player") return players(0).getName
  }
}
