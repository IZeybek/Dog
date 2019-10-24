package util

import model.Board

import scala.io.Source

case class Read {
  def readText(fileName: String, b: Board): Unit = {
    for (line <- Source.fromFile(fileName).getLines())
      line.split(" ").foreach()
  }
}
