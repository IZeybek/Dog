import scala.io.Source

class Game {
  def gameStart: Unit = {
  }

  def readText(fileName: String) = {
    for (line <- Source.fromFile(fileName).getLines()) {
      line.split(" ")
    }
  }

  def parser(input: String): Unit = {
    if (input contains ("player")) {

    }
  }
}
