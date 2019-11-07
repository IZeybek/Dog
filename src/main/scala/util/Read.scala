package util

import model.BoardTrait
import model.Main.{Board, Cell}

import scala.io.Source

case class Read() extends ReadTrait {


  def prettyPrint(b: BoardTrait): Unit = {
    val x = b.xy(0)
    val y = b.xy(1)
    var j = 0;
    var h = 0

    val checkPos = (i: Int, j: Int, c: Array[Cell]) => i == c(h).xy(1) && j == c(h).xy(0)

    for (i <- 0 until y) {
      for (j <- 0 until x) {
        if (b.cells.last.absPos + 1 != h && checkPos(i, j, b.cells)) {
          print(Console.BLUE + "i" + Console.WHITE)
          h += 1
        } else
          print(" ")
      }
      println("")
      j = 0
    }
  }


  def readIn(path: String): Board = {
    val cells = scala.collection.mutable.ArrayBuffer.empty[Cell]
    val file = Source.fromFile(path)
    var x = 0;
    var y = 0;
    var z = 0
    for (line <- file.getLines()) {
      x = 0
      for (e <- line) {
        if (e == '1') {
          cells += Cell(z, Array(x, y), true)
          z += 1
        }
        x += 1
      }
      y += 1
    }
    new Board(Array(x, y), cells.toArray)
  }

}
