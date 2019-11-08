package model.Main

import scala.io.Source

case class Board(xy: Array[Int], cells: Array[Cell]) {


  def prettyPrint(): Unit = {
    val x = xy(0)
    val y = xy(1)
    var j = 0
    var cellPos = 0

    val checkPos = (i: Int, j: Int, c: Array[Cell]) => i == c(cellPos).xy(1) && j == c(cellPos).xy(0)

    for (i <- 0 until y) {
      for (j <- 0 until x) {
        if (cells.last.absPos + 1 != cellPos && checkPos(i, j, cells)) {
          print(Console.BLUE + "i" + Console.WHITE)
          cellPos += 1
        } else
          print(" ")
      }
      println(" ")
      j = 0
    }
  }
}

object Read {
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
    Board(Array(x, y), cells.toArray)
  }
}
