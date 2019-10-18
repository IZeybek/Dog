import scala.io.Source

class Game {

  val parse = (x: String) => parser(x)

  def gameStart: Unit = {
  }

  def readText(fileName: String) = {
    for (line <- Source.fromFile(fileName).getLines()) {
      line.split(" ").foreach(parse)
    }
  }

  def parser(input: String): Unit = {
    if (input contains ("player")) {

    }
  }
}
